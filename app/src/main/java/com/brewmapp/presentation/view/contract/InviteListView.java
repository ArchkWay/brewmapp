package com.brewmapp.presentation.view.contract;

import java.util.List;

import com.brewmapp.data.entity.wrapper.SocialContactInfo;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface InviteListView extends BasicView {
    void showContacts(List<SocialContactInfo> contactInfos);
    BaseActivity getActivity();
    void refresh();
}
