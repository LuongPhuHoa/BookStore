# Lab Phase 6: Spring Core

Lab này đi theo chu trình trong roadmap:

> Question → Prediction → Code → Implementation → Interview → Best Practice

Mục tiêu là hiểu bốn khái niệm Spring Core bằng một application context nhỏ, không cần
khởi động web server hay PostgreSQL:

- IoC (Inversion of Control)
- Dependency Injection
- Bean Lifecycle
- Bean Scope

## 1. Bản đồ code

```text
org.example.springcorelab
├── SpringCoreLabConfig  cấu hình và quét component
├── MessageGateway      abstraction cần được inject
├── OrderService        singleton bean nhận dependency qua constructor
├── LifecycleProbe      quan sát @PostConstruct và @PreDestroy
├── PrototypeTask       mỗi lần yêu cầu là một instance mới
├── TaskCoordinator     singleton lấy prototype qua ObjectProvider
└── SpringCoreLab       chương trình demo có thể chạy độc lập
```

Test kiểm chứng nằm tại
`src/test/java/org/example/springcorelab/SpringCoreLabTest.java`.

## 2. Chạy lab

Yêu cầu Java 21.

Chạy các thí nghiệm tự động:

```powershell
.\mvnw.cmd -Dtest=SpringCoreLabTest test
```

Chạy chương trình demo từ terminal:

```powershell
.\mvnw.cmd -q org.codehaus.mojo:exec-maven-plugin:3.5.0:java `
  '-Dexec.mainClass=org.example.springcorelab.SpringCoreLab'
```

Bạn cũng có thể mở `SpringCoreLab.java` trong IDE và chạy method `main`. Kết quả có
dạng:

```text
IoC + DI: Sent confirmation for ORDER-001
Singleton returns same object: true
Prototype task 1: <một UUID>
Prototype task 2: <một UUID khác>
Prototype returns different objects: true
Before closing context: [1. constructor, 2. @PostConstruct, 3. bean is being used]
After closing context: [1. constructor, 2. @PostConstruct, 3. bean is being used, 4. @PreDestroy]
```

UUID thay đổi ở mỗi lần chạy, nhưng hai UUID phải khác nhau.

## 3. Thí nghiệm 1: IoC và Dependency Injection

### Question

Ai tạo `OrderService` và ai truyền `MessageGateway` vào constructor?

### Prediction

Không có dòng `new OrderService(...)` trong code demo. Sau khi context khởi động,
`context.getBean(OrderService.class)` vẫn trả về một object đã có đủ dependency.

### Code

`@ComponentScan` yêu cầu Spring tìm các stereotype như `@Component` và `@Service`.
`@Bean` đăng ký object do method cấu hình tạo:

```java
@Configuration
@ComponentScan(basePackageClasses = SpringCoreLabConfig.class)
class SpringCoreLabConfig {
    @Bean
    MessageGateway messageGateway() {
        return orderCode -> "Sent confirmation for " + orderCode;
    }
}
```

`OrderService` chỉ khai báo điều nó cần:

```java
@Service
class OrderService {
    private final MessageGateway messageGateway;

    OrderService(MessageGateway messageGateway) {
        this.messageGateway = messageGateway;
    }
}
```

### Giải thích

IoC là sự đảo chiều quyền điều khiển việc tạo và liên kết object. Trong Java thuần,
code ứng dụng tự `new`, tự chọn implementation và tự quản lý vòng đời. Trong lab,
`ApplicationContext` làm các việc đó.

DI là kỹ thuật Spring dùng để thực hiện IoC: container tìm một bean có type
`MessageGateway`, rồi truyền nó vào constructor của `OrderService`.

Hai khái niệm liên quan nhưng không đồng nghĩa:

- IoC là nguyên lý tổng quát: quyền điều khiển chuyển sang framework/container.
- DI là cơ chế cụ thể: dependency được đưa từ bên ngoài vào object.

Constructor injection được ưu tiên vì dependency bắt buộc được biểu diễn rõ, field có
thể là `final`, object dễ test và không tồn tại trạng thái "được tạo rồi nhưng chưa
inject".

### Spring làm gì bên dưới?

Ở mức đơn giản hóa:

1. Đọc `SpringCoreLabConfig` và component scan.
2. Tạo các `BeanDefinition` — metadata mô tả cách tạo bean.
3. Đăng ký metadata vào `BeanFactory`.
4. Khi tạo `OrderService`, resolve constructor và tìm bean theo type
   `MessageGateway`.
5. Tạo hoặc lấy dependency từ singleton cache, rồi gọi constructor.
6. Chạy các bean post-processor và callback vòng đời.

`ApplicationContext` xây trên `BeanFactory` và bổ sung các khả năng như event,
resource loading, message resolution và tự động đăng ký post-processor.

## 4. Thí nghiệm 2: Bean Lifecycle

### Question

`@PostConstruct` chạy trước hay sau constructor? `@PreDestroy` chạy khi nào?

### Prediction

Thứ tự quan sát được:

```text
constructor → @PostConstruct → sử dụng bean → @PreDestroy
```

### Code và kết quả

`LifecycleProbe` ghi lại sự kiện thay vì chỉ log, nên test có thể assert chính xác.
Khi context còn mở:

```text
[1. constructor, 2. @PostConstruct, 3. bean is being used]
```

Sau `context.close()`:

```text
[1. constructor, 2. @PostConstruct, 3. bean is being used, 4. @PreDestroy]
```

### Mô hình đầy đủ hơn

Vòng đời bean thường được hình dung như sau:

```text
instantiate
  → populate dependencies
  → aware callbacks (nếu có)
  → BeanPostProcessor before-initialization
  → @PostConstruct
  → BeanPostProcessor after-initialization
  → bean sẵn sàng sử dụng
  → @PreDestroy khi context đóng
```

Một post-processor có thể bọc bean bằng proxy, vì vậy object lấy từ context đôi khi
không phải instance gốc. Đây là nền tảng để học Spring AOP ở phần tiếp theo.

`@PostConstruct` phù hợp với validation hoặc khởi tạo tài nguyên sau khi dependency đã
được inject. `@PreDestroy` phù hợp để giải phóng tài nguyên. Không đặt công việc chậm,
không ổn định hoặc business logic lớn trong callback khởi tạo vì nó làm cả application
không start được.

## 5. Thí nghiệm 3: Bean Scope

### Question

Hai lần gọi `getBean` có luôn tạo hai object không?

### Prediction

- `OrderService` không ghi scope nên dùng singleton mặc định: hai lần lấy là cùng
  một object trong cùng `ApplicationContext`.
- `PrototypeTask` dùng `prototype`: hai lần lấy là hai object khác nhau.

Test dùng `assertSame` và `assertNotSame`, tức là so sánh identity bằng `==`, không
phải so sánh nội dung bằng `equals`.

### Các scope thường gặp

| Scope         | Ý nghĩa                                                             |
|---------------|---------------------------------------------------------------------|
| `singleton`   | Một instance cho mỗi bean definition trong mỗi `ApplicationContext` |
| `prototype`   | Container tạo instance mới mỗi lần bean được yêu cầu                |
| `request`     | Một instance cho mỗi HTTP request                                   |
| `session`     | Một instance cho mỗi HTTP session                                   |
| `application` | Một instance cho mỗi `ServletContext`                               |

Spring singleton không có nghĩa là "một object duy nhất trong toàn JVM" như cách
Singleton GoF thường được mô tả. Hai application context có thể có hai instance.

Singleton bean được dùng đồng thời bởi nhiều request, nên đừng lưu dữ liệu thay đổi
theo user/request trong field của nó. Hãy giữ service stateless hoặc dùng cơ chế
đồng bộ phù hợp.

Với prototype, Spring tạo và khởi tạo bean nhưng không tự gọi destroy callback cho
từng instance sau khi giao nó cho caller. Caller phải quản lý việc cleanup tài nguyên
mà prototype sở hữu.

## 6. Bẫy: prototype được inject vào singleton

`TaskCoordinator` là singleton. Nếu constructor nhận trực tiếp `PrototypeTask`, Spring
chỉ resolve dependency một lần khi tạo coordinator; mọi lần sử dụng sau đó sẽ thấy
cùng task.

Lab inject `ObjectProvider<PrototypeTask>`:

```java
@Component
class TaskCoordinator {
    private final ObjectProvider<PrototypeTask> taskProvider;

    TaskCoordinator(ObjectProvider<PrototypeTask> taskProvider) {
        this.taskProvider = taskProvider;
    }

    PrototypeTask createTask() {
        return taskProvider.getObject();
    }
}
```

Mỗi `getObject()` là một yêu cầu mới gửi tới container, nên prototype scope thực sự
tạo instance mới. Chỉ dùng kiểu lookup này khi lifetime khác nhau là yêu cầu thật;
đừng biến service locator thành cách mặc định để che giấu dependency.

## 7. Câu hỏi phỏng vấn

1. **IoC khác DI thế nào?**  
   IoC là nguyên lý đảo quyền điều khiển; DI là một cách thực thi IoC bằng cách cung
   cấp dependency từ bên ngoài.

2. **Vì sao ưu tiên constructor injection?**  
   Dependency bắt buộc rõ ràng, hỗ trợ immutable field, fail fast và dễ unit test.

3. **Spring singleton có thread-safe không?**  
   Không. Scope chỉ quy định số instance; thread safety phụ thuộc thiết kế bean.

4. **`@PostConstruct` có chạy trước khi inject dependency không?**  
   Không. Nó chạy sau khi object được tạo và dependency đã được populate.

5. **Spring có gọi `@PreDestroy` cho prototype không?**  
   Thông thường không; container không theo dõi toàn bộ lifecycle sau khi trả
   prototype cho caller.

6. **Có hai implementation cùng implement một interface thì sao?**  
   Injection theo type trở nên mơ hồ. Dùng `@Primary`, `@Qualifier`, hoặc inject một
   collection nếu thực sự cần tất cả implementations.

## 8. Best practices

- Ưu tiên constructor injection và dependency theo interface ở boundary phù hợp.
- Giữ singleton service stateless.
- Dùng scope hẹp hơn chỉ khi lifetime của dữ liệu thực sự yêu cầu.
- Không gọi `ApplicationContext.getBean()` rải rác trong business code.
- Giữ lifecycle callback nhỏ, nhanh, có thể dự đoán và đối xứng init/cleanup.
- Viết context test nhỏ để kiểm tra wiring; business logic thuần nên dùng unit test
  không khởi động Spring.

## 9. Bài tập tự làm

1. Tạo thêm `SmsMessageGateway`. Quan sát lỗi ambiguous dependency, rồi giải bằng
   `@Qualifier`.
2. Thay `ObjectProvider<PrototypeTask>` bằng inject trực tiếp `PrototypeTask`. Dự đoán
   test nào fail trước khi chạy.
3. Tạo hai `AnnotationConfigApplicationContext` và chứng minh mỗi context có một
   singleton `OrderService` riêng.
4. Thêm một mutable counter vào `OrderService`, gọi đồng thời từ nhiều thread và giải
   thích vì sao singleton scope không đảm bảo thread safety.
