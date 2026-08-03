# Lab Phase 1 / Module 2: Collections Framework

Chạy toàn bộ thí nghiệm:

```powershell
.\mvnw.cmd -Dtest=CollectionsLabTest test
```

Test nằm trong `org.example.collectionslab.CollectionsLabTest`; ba mini project nằm trong cùng package.

## Bản đồ chọn collection

| Nhu cầu | Lựa chọn | Điểm cần nhớ |
|---|---|---|
| Truy cập theo chỉ số | `ArrayList` | mảng liên tiếp; `get(i)` O(1), chèn giữa O(n) |
| Thêm/xóa hai đầu | `ArrayDeque` | circular array, không nhận `null` |
| Đọc nhiều, ghi hiếm, đa luồng | `CopyOnWriteArrayList` | mỗi lần ghi copy mảng; iterator là snapshot |
| Không trùng | `HashSet` | không cam kết thứ tự |
| Không trùng theo thứ tự thêm | `LinkedHashSet` | hash table + linked order |
| Không trùng và được sắp xếp | `TreeSet` | red-black tree, O(log n) |
| Lấy phần tử ưu tiên nhất | `PriorityQueue` | heap; chỉ `peek`/`poll` có thứ tự ưu tiên |
| Tra cứu key/value | `HashMap` | expected O(1) |
| Cần insertion/access order | `LinkedHashMap` | phù hợp xây LRU |
| Cần key sorted/range | `TreeMap` | O(log n) |
| Map chia sẻ giữa các thread | `ConcurrentHashMap` | `compute`/`merge` atomic theo key, không nhận null |

`LinkedList` tiện cho deque operation nhưng `ArrayDeque` thường là deque mặc định tốt hơn. `Vector` và `Hashtable` là legacy synchronized classes: thread-safe ở từng operation, nhưng không biến một chuỗi thao tác thành atomic.

## Internals cần nắm

`HashMap`: spread hash → index bucket → so sánh `equals` trong bucket → insert/replace. Key bằng nhau phải có cùng `hashCode`. Collision tạo chain; JDK hiện đại có thể treeify bucket lớn. Khi `size > capacity × loadFactor` (mặc định .75), table resize.

Đừng dựa vào thứ tự lặp của `HashMap`/`HashSet`. `TreeMap`/`TreeSet` sắp xếp bằng comparator hoặc natural ordering. `PriorityQueue` không đảm bảo iterator đã sort; chỉ repeated `poll()` mới cho priority order.

Fail-fast iterator lưu modification count và thường ném `ConcurrentModificationException` nếu collection bị structural modification ở ngoài iterator. Đây chỉ là cơ chế phát hiện lỗi best-effort, không phải synchronization. Dùng `iterator.remove`, collection concurrent, hoặc external locking tùy bài toán.

## Mini projects

- `SimpleHashMap<K,V>`: separate chaining, collision lookup, replacement, hash spread và resize. Nó cố ý nhỏ hơn JDK `HashMap` để thấy rõ cơ chế.
- `LruCache<K,V>`: `LinkedHashMap` access-order; đọc một entry sẽ đưa nó thành mới nhất, còn `removeEldestEntry` evict entry ít dùng nhất.
- `RingBuffer<E>`: custom bounded FIFO bằng circular array; `poll` xóa reference để GC có thể thu hồi object.

## Câu hỏi phỏng vấn

1. **Vì sao HashMap cần cả `hashCode` và `equals`?** Hash chọn bucket, `equals` tìm đúng key trong bucket.
2. **Vì sao mutable key nguy hiểm?** Nếu field dùng cho hash/equality đổi sau khi put, entry có thể không còn tìm được.
3. **`synchronizedMap` khác `ConcurrentHashMap`?** Wrapper khóa từng operation và cần tự khóa khi iterate; `ConcurrentHashMap` scale tốt hơn và có atomic map operations.
4. **Khi nào CopyOnWriteArrayList không phù hợp?** Ghi nhiều hoặc list lớn, vì mỗi write copy toàn bộ backing array.
5. **Vì sao re-iterate PriorityQueue không sorted?** Heap chỉ giữ root là phần tử kế tiếp để insert/remove hiệu quả O(log n).

Best practice: khai báo bằng interface (`List`, `Set`, `Map`, `Deque`), chọn implementation theo contract cần thiết, và không dựa vào thứ tự tình cờ.
