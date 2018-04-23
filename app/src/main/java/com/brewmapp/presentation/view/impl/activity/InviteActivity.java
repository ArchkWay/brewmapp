package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.social.SocialNetwork;
import com.brewmapp.presentation.presenter.contract.InvitePresenter;
import com.brewmapp.presentation.view.contract.InviteView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;

public class InviteActivity extends BaseActivity
        implements InviteView
{

    //region BindView
    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_invite_facebook) ImageView facebook;
    @BindView(R.id.activity_invite_phone) ImageView phone;
    @BindView(R.id.activity_invite_switter) ImageView twitter;
    @BindView(R.id.activity_invite_vk) ImageView vk;
    @BindView(R.id.activity_invite_brew) ImageView brew;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;

    //endregion

    //region Inject
    @Inject InvitePresenter presenter;
    //endregion

    //region Impl InviteActivity
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
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        toolbarTitle.setText(getTitle());

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
    //endregion

    //region Impl InviteView
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }
    //endregion


}
