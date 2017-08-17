package ru.frosteye.beermap.presentation.presenter.contract;

import android.content.Intent;

import ru.frosteye.beermap.data.entity.SocialContact;
import ru.frosteye.beermap.presentation.view.contract.InviteListView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public interface InviteListPresenter extends LivePresenter<InviteListView> {
    void loadContactsForNetwork(int id);
    void onActivityResult(int requestCode, int resultCode, Intent data);
    void onRequestInvite(SocialContact contact);
}
