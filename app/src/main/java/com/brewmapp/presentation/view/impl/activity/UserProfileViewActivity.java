package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.UserProfilePresenter;
import com.brewmapp.presentation.view.contract.UserProfileView;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class UserProfileViewActivity extends BaseActivity implements UserProfileView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.activity_profile_view_avatar)    ImageView avatar;
    @BindView(R.id.activity_profile_view_username)    TextView username;
    @BindView(R.id.activity_profile_view_city)    TextView city;
    @BindView(R.id.activity_profile_view_status)    TextView status;

    @Inject    UserProfilePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_view);
    }

    @Override
    protected void initView() {
        enableBackButton();
        presenter.onAttach(this);
        presenter.parseIntent(getIntent());
        setTitle(R.string.title_view_profile);
    }

    @Override
    protected void attachPresenter() {

    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        finish();
    }

    @Override
    public void refreshContent(User user) {
        String tmpStr;
        tmpStr=user.getThumbnail();
        if(tmpStr!=null)
            Picasso.with(this).load(tmpStr).fit().centerCrop().into(avatar);
        else
            Picasso.with(this).load(user.getGender()==1?R.drawable.ic_user_man:R.drawable.ic_user_woman).fit().centerCrop().into(avatar);
        username.setText(user.getFormattedName());
        city.setText(user.getCountryCityFormated());
        status.setText(R.string.online);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }
}
