package com.brewmapp.data.entity.wrapper;

import eu.davidea.flexibleadapter.items.IFilterable;
import com.brewmapp.R;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.presentation.view.impl.widget.ContactView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class ContactInfo extends AdapterItem<Contact, ContactView> implements IFilterable {

    public ContactInfo(Contact model) {
        super(model);
    }

    public ContactInfo() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_contact_row;
    }

    @Override
    public boolean filter(String constraint) {
        try {
            return getModel().getUser().getFormattedName()
                    .toLowerCase().contains(constraint.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }
}
