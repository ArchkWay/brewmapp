package com.brewmapp.presentation.view.impl.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.pojo.RegisterPackage;
import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.data.pojo.RegisterPackageWithPhoneAndPassword;
import com.brewmapp.presentation.presenter.contract.EnterPasswordPresenter;
import com.brewmapp.presentation.view.contract.EnterPasswordView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TextTools;

public class EnterPasswordActivity extends BaseActivity implements EnterPasswordView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_password_password) EditText password;
    @BindView(R.id.activity_password_phone) TextView phone;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_dropdown)
    LinearLayout toolbarDropdown;

    @Inject EnterPasswordPresenter presenter;

    private RegisterPackageWithPhoneAndPassword registerPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
    }

    @Override
    protected void initView() {
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        toolbarTitle.setText(getTitle());
        enableBackButton();
        registerPackage = new RegisterPackageWithPhoneAndPassword(
                ((RegisterPackageWithPhone) getIntent().getSerializableExtra(RegisterPackage.KEY))
        );
        phone.setText(registerPackage.getPhone());
        registerTextChangeListeners(s -> {
            registerPackage.setPassword(TextTools.extractTrimmed(password));
            invalidateOptionsMenu();
        }, password);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_allDone).setEnabled(registerPackage.validate());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_allDone) {
            presenter.onRegisterPackageReady(registerPackage);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
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
        hideKeyboard();
        showTopBarLoading(!enabled);
        phone.setEnabled(enabled);
    }

    @Override
    public void proceed() {
        startActivityAndClearTask(MainActivity.class);
    }
}
