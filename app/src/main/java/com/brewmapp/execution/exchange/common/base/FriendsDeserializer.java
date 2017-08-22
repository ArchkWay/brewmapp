package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.entity.wrapper.ContactInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by oleg on 16.08.17.
 */

public class FriendsDeserializer extends AdapterItemDeserializer<Contact, ContactInfo> {
    @Override
    protected Class<Contact> provideType() {
        return Contact.class;
    }

    @Override
    protected Class<ContactInfo> provideWrapperType() {
        return ContactInfo.class;
    }
}
