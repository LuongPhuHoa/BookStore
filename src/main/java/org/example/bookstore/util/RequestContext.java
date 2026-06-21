package org.example.bookstore.util;

public record RequestContext(String requestId, String currentUser) {

    public static final ScopedValue<RequestContext> CURRENT = ScopedValue.newInstance();

    public static RequestContext current() {
        return CURRENT.get();
    }
}
