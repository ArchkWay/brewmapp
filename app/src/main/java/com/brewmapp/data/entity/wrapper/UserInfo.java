package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.impl.widget.InterestAddViewUser;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by xpusher on 11/28/2017.
 */

public class UserInfo extends AdapterItem<User, InterestAddViewUser> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_user;
    }
}
