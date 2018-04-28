package com.brewmapp.data.entity.wrapper;

import eu.davidea.flexibleadapter.items.IFilterable;
import com.brewmapp.R;

import com.brewmapp.data.entity.contract.InfoItem_view;
import com.brewmapp.presentation.view.impl.widget.InfoItemView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class InfoItem extends AdapterItem<String, InfoItemView> implements IFilterable, InfoItem_view {

    private int status;

    public InfoItem(String model, int status) {
        super(model);
        this.status=status;
    }

    @Override
    public int getLayoutRes() {
        switch (status){
            case FRIENDS_REQUEST_IN:
            case FRIENDS_REQUEST_OUT:
                return R.layout.view_friends_requests_title;
            case WHILE_NOT_EXIST_SUBSCRIBE:
                return  R.layout.while_subscrible_not_exisit;
            case SEPARATOR:
                return  R.layout.view_friends_title;
            default:
                return R.layout.view_friends_title;

        }

    }

    @Override
    public boolean filter(String constraint) {
        return false;
    }

    public int getStatus() {
        return status;
    }
}
