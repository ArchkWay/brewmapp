package com.brewmapp.data.entity.wrapper;

import eu.davidea.flexibleadapter.items.IFilterable;
import com.brewmapp.R;

import com.brewmapp.data.entity.SocialContact;
import com.brewmapp.presentation.view.impl.widget.InviteView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class SocialContactInfo extends AdapterItem<SocialContact, InviteView> implements IFilterable {

    public SocialContactInfo(SocialContact model) {
        super(model);
    }

    public SocialContactInfo() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_invite_row;
    }

    @Override
    public boolean filter(String constraint) {
        try {
            return getModel().getFormattedName().toLowerCase().contains(constraint.toLowerCase());
        } catch (Exception e) {
            return false;
        }
    }
}
