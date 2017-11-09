package com.brewmapp.presentation.view.impl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.ProfileInfoPresenter;
import com.brewmapp.presentation.presenter.impl.ProfileInfoPresenterImpl;
import com.brewmapp.presentation.view.contract.ProfileInfoView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

public class ProfileInfoActivity extends BaseActivity implements ProfileInfoView {
    @BindView(R.id.common_toolbar)    Toolbar toolbar;
    @BindView(R.id.profile_info_activity_container)FrameLayout frameLayout;
    @BindView(R.id.profile_info_activity_profile_avatar)    ImageView avatar;
    @BindView(R.id.profile_info_activity_profile_username)    TextView user_name;
    @BindView(R.id.profile_info_activity_profile_status)    TextView status;
    @BindView(R.id.profile_info_activity_profile_city)    TextView country_city;

    @Inject    ProfileInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);
    }

    @Override
    protected void initView() {
        enableBackButton();
    }

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
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void refreshUserProfile(User load) {
        setTitle(load.getFormattedName());
        Picasso.with(this).load(load.getThumbnail()).fit().into(avatar);
        user_name.setText(load.getFormattedName());
        country_city.setText(load.getFormattedPlace());
        status.setText(R.string.online);
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
    public void showFragment(BaseFragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.profile_info_activity_container, fragment)
                .commit();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
