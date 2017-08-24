package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.wrapper.EventInfo;
import com.brewmapp.data.entity.wrapper.PhotoInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by oleg on 16.08.17.
 */

public class EventsDeserializer extends AdapterItemDeserializer<Event, EventInfo> {
    @Override
    protected Class<Event> provideType() {
        return Event.class;
    }

    @Override
    protected Class<EventInfo> provideWrapperType() {
        return EventInfo.class;
    }
}
