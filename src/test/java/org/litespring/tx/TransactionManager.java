package org.litespring.tx;

import org.litespring.util.MessageTracker;

public class TransactionManager {

    public void start() {
        System.out.println("tx start");
        MessageTracker.add("tx start");
    }

    public void rollback() {
        System.out.println("tx rollback");
        MessageTracker.add("tx rollback");
    }

    public void commit() {
        System.out.println("tx commit");
        MessageTracker.add("tx commit");
    }
}
