package org.apache.dolphinscheduler.server.master.dag;

import org.apache.dolphinscheduler.server.master.events.IEvent;
import org.apache.dolphinscheduler.server.master.events.IEventRepository;

public interface IEventfulExecutionRunnable {

    IEventRepository getEventRepository();

    default void storeEventToTail(IEvent event) {
        getEventRepository().storeEventToTail(event);
    }

    default void storeEventToHead(IEvent event) {
        getEventRepository().storeEventToHead(event);
    }
}
