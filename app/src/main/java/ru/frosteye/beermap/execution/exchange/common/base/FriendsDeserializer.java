package ru.frosteye.beermap.execution.exchange.common.base;

import ru.frosteye.beermap.data.entity.Contact;
import ru.frosteye.beermap.data.entity.Photo;
import ru.frosteye.beermap.data.entity.wrapper.ContactInfo;
import ru.frosteye.beermap.data.entity.wrapper.PhotoInfo;
import ru.frosteye.beermap.data.model.IPerson;
import ru.frosteye.beermap.presentation.view.impl.widget.ContactView;
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
