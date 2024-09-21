package org.litespring.util;

import java.util.ArrayList;
import java.util.List;

public class MessageTracker {

    public static List<String> MESSAGES = new ArrayList<>();

    public static void add(String msg) {
        MESSAGES.add(msg);
    }

    public static void clear() {
        MESSAGES.clear();
    }

    public static List<String> get() {
        return MESSAGES;
    }
}
