package ru.frosteye.beermap.presentation.presenter.impl;

import android.net.Uri;

import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import javax.inject.Inject;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.presentation.view.contract.InviteView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.InvitePresenter;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

public class InvitePresenterImpl extends BasePresenter<InviteView> implements InvitePresenter {

    @Inject
    public InvitePresenterImpl() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onFacebookShare() {
        ShareDialog shareDialog = new ShareDialog(view.getActivity());
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setQuote(getString(R.string.invite_message))
                .setContentUrl(Uri.parse(getString(R.string.config_site_url))).build();
        shareDialog.show(linkContent);
    }
}
