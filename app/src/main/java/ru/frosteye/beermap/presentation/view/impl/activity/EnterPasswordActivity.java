package ru.frosteye.beermap.presentation.view.impl.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.pojo.RegisterPackage;
import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhone;
import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhoneAndPassword;
import ru.frosteye.beermap.presentation.presenter.contract.EnterPasswordPresenter;
import ru.frosteye.beermap.presentation.view.contract.EnterPasswordView;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TextTools;

public class EnterPasswordActivity extends BaseActivity implements EnterPasswordView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_password_password) EditText password;
    @BindView(R.id.activity_password_phone) TextView phone;

    @Inject EnterPasswordPresenter presenter;

    private RegisterPackageWithPhoneAndPassword registerPackage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_password);
    }

    @Override
    protected void initView() {
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
        menu.findItem(R.id.action_done).setEnabled(registerPackage.validate());
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_done) {
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
