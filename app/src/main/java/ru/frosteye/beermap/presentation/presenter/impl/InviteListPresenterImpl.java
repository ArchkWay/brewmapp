package ru.frosteye.beermap.presentation.presenter.impl;

import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.SocialContact;
import ru.frosteye.beermap.data.entity.container.Contacts;
import ru.frosteye.beermap.data.entity.wrapper.ContactInfo;
import ru.frosteye.beermap.data.entity.wrapper.SocialContactInfo;
import ru.frosteye.beermap.execution.social.SocialManager;
import ru.frosteye.beermap.execution.social.SocialNetwork;
import ru.frosteye.beermap.presentation.view.contract.InviteListView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.InviteListPresenter;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

public class InviteListPresenterImpl extends BasePresenter<InviteListView> implements InviteListPresenter {

    private SocialManager socialManager;
    private SocialNetwork socialNetwork;

    @Inject
    public InviteListPresenterImpl(SocialManager socialManager) {

        this.socialManager = socialManager;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadContactsForNetwork(int id) {
        enableControls(false);
        socialNetwork = socialManager.getSocialNetwork(id);
        view.getActivity().setTitle(socialNetwork.getNetworkName());
        if(!socialNetwork.isAuthorized()) {
            socialNetwork.auth(view.getActivity(), credentials -> {
                loadFriends();
            }, exception -> {
                enableControls(true);
                showMessage(exception.getMessage());
            });
        } else loadFriends();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        socialNetwork.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestInvite(SocialContact contact) {
        enableControls(false);
        socialNetwork.notyfy(contact, getString(R.string.invite_message), result -> {
            enableControls(true);
            if(result.getPayload() != null) {
                showMessage(result.getPayload());
            }
            if(result.isSuccesful()) {
                contact.setInviteSent(true);
                view.refresh();
            }
        });
    }

    private void loadFriends() {
        socialNetwork.getFriends((network, contacts1) -> {
            enableControls(true);
            view.showContacts(compose(contacts1));
        }, exception -> {
            enableControls(true);
            showMessage(exception.getMessage());
        });
    }

    private List<SocialContactInfo> compose(Contacts contacts) {
        List<SocialContactInfo> out = new ArrayList<>();
        for(SocialContact contact: contacts.getContacts()) {
            out.add(new SocialContactInfo(contact));
        }
        return out;
    }
}
