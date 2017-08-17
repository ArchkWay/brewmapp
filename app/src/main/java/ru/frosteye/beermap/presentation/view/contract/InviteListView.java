package ru.frosteye.beermap.presentation.view.contract;

import java.util.List;

import ru.frosteye.beermap.data.entity.wrapper.ContactInfo;
import ru.frosteye.beermap.data.entity.wrapper.SocialContactInfo;
import ru.frosteye.beermap.presentation.view.impl.activity.BaseActivity;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface InviteListView extends BasicView {
    void showContacts(List<SocialContactInfo> contactInfos);
    BaseActivity getActivity();
    void refresh();
}
