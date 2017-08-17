package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import butterknife.BindView;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.social.SocialNetwork;
import ru.frosteye.beermap.presentation.presenter.contract.InvitePresenter;
import ru.frosteye.beermap.presentation.view.contract.InviteView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import ru.frosteye.beermap.R;

public class InviteActivity extends BaseActivity implements InviteView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_invite_facebook) ImageView facebook;
    @BindView(R.id.activity_invite_phone) ImageView phone;
    @BindView(R.id.activity_invite_switter) ImageView twitter;
    @BindView(R.id.activity_invite_vk) ImageView vk;

    @Inject InvitePresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enableBackButton();
        facebook.setOnClickListener(v -> presenter.onFacebookShare());
        vk.setOnClickListener(listener);
        twitter.setOnClickListener(listener);
        phone.setOnClickListener(listener);
    }

    private View.OnClickListener listener = v -> {
        Intent intent = new Intent(this, InviteListActivity.class);
        if(v == vk) intent.putExtra(Keys.NETWORK, SocialNetwork.VK);
        if(v == twitter) intent.putExtra(Keys.NETWORK, SocialNetwork.TW);
        if(v == phone) intent.putExtra(Keys.NETWORK, SocialNetwork.PH);
        startActivity(intent);
    };

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public Activity getActivity() {
        return this;
    }
}
