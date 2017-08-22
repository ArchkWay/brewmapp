package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.data.entity.SocialContact;
import com.brewmapp.presentation.view.contract.InviteListView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface InviteListPresenter extends LivePresenter<InviteListView> {
    void loadContactsForNetwork(int id);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestInvite(SocialContact contact);
}
