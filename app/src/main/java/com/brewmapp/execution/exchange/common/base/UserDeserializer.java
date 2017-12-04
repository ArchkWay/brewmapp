package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.wrapper.UserInfo;
import com.brewmapp.execution.exchange.common.base.custom.AdapterItemDeserializerUsers;


import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by xpusher on 11/28/2017.
 */

public class UserDeserializer extends AdapterItemDeserializerUsers<User, UserInfo> {
    @Override
    protected Class<User> provideType() {
        return User.class;
    }

    @Override
    protected Class<UserInfo> provideWrapperType() {
        return UserInfo.class;
    }
}
