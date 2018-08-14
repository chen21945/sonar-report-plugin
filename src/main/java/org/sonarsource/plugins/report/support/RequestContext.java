package org.sonarsource.plugins.report.support;

import java.util.Optional;


public class RequestContext {

    private static ThreadLocal<Optional<String>> localCookie = new ThreadLocal<>();

    public static void setCookie(Optional<String> cookie) {
        localCookie.set(cookie);
    }

    public static Optional<String> getCookie() {
        return localCookie.get();
    }

    public static void clean(){
        localCookie.remove();
    }

}
