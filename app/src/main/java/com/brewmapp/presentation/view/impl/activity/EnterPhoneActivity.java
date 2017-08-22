package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.pojo.RegisterPackage;
import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.presentation.presenter.contract.RegisterPresenter;
import com.brewmapp.presentation.view.contract.RegisterView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;

public class EnterPhoneActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.activity_enterPhone_bottom_phone) MaskedEditText phone;
    @BindView(R.id.common_toolbar) Toolbar toolbar;


    @Inject RegisterPresenter presenter;

    private RegisterPackageWithPhone registerPackage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }


    @Override
    protected void initView() {
        registerPackage = new RegisterPackageWithPhone(
                ((RegisterPackage) getIntent().getSerializableExtra(RegisterPackage.KEY))
        );
        registerTextChangeListeners(s -> {
            registerPackage.setPhone("7" + phone.getRawText());
            invalidateOptionsMenu();
        }, phone);
        enableBackButton();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_next).setEnabled(registerPackage.validate());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_next) {
            presenter.onRegisterPackageReady(registerPackage);
        }
        return super.onOptionsItemSelected(item);
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
        showTopBarLoading(!enabled);
        phone.setEnabled(enabled);
    }

    @Override
    public void proceed() {
        Intent intent = new Intent(this, ConfirmCodeActivity.class);
        intent.putExtra(RegisterPackage.KEY, registerPackage);
        startActivity(intent);
    }
}
