package org.apache.dolphinscheduler.server.master.events;

import java.util.List;

/**
 * The event repository interface used to store event.
 */
public interface IEventRepository {

    void storeEventToTail(IEvent event);

    void storeEventToHead(IEvent event);

    IEvent poolEvent();

    int getEventSize();

    List<IEvent> getAllEvent();

}
