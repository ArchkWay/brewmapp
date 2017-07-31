package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import butterknife.BindView;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.presentation.presenter.contract.LoginPresenter;
import ru.frosteye.beermap.presentation.view.contract.LoginView;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import ru.frosteye.beermap.R;
import ru.frosteye.ovsa.stub.listener.OnDoneListener;
import ru.frosteye.ovsa.tool.TextTools;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_login_enter) Button enter;
    @BindView(R.id.activity_login_login) EditText login;
    @BindView(R.id.activity_login_password) EditText password;
    @BindView(R.id.activity_login_logo) View logoImage;
    @BindView(R.id.activity_login_fb) View fb;

    @Inject LoginPresenter presenter;

    private boolean logoIsHidden;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
        showTopBarLoading(!enabled);
        enter.setEnabled(enabled);
        login.setEnabled(enabled);
        password.setEnabled(enabled);
        fb.setEnabled(enabled);
        fb.setAlpha(enabled ? 1.0f : 0.3f);
    }

    @Override
    protected void initView() {
        enableBackButton();
        registerTextChangeListeners(enter, login, password);
        enter.setOnClickListener(v -> login());
        fb.setOnClickListener(v -> presenter.onFacebookLogin());
        setOnDoneListener(password, this::login);
        login.setOnFocusChangeListener(loginListener);
        password.setOnFocusChangeListener(passwordListener);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    private void login() {
        if(TextTools.isTrimmedEmpty(login) || TextTools.isTrimmedEmpty(password)) return;
        String loginString = TextTools.extractTrimmed(login);
        String passString = TextTools.extractTrimmed(password);
        presenter.onLoginPassReady(loginString, passString);
    }

    private View.OnFocusChangeListener loginListener = (v, hasFocus) -> {
        if(hasFocus && !logoIsHidden) {
            logoIsHidden = true;
            logoImage.setVisibility(View.GONE);
        }
    };

    private View.OnFocusChangeListener passwordListener = (v, hasFocus) -> {
        if(hasFocus && !logoIsHidden) {
            logoIsHidden = true;
            logoImage.setVisibility(View.GONE);
        }
    };

    @Override
    public void onBackPressed() {
        if(clearFocus()) return;
        super.onBackPressed();
    }

    private boolean clearFocus() {
        if(login.hasFocus() || password.hasFocus()) {
            logoImage.setVisibility(View.VISIBLE);
            login.clearFocus();
            password.clearFocus();
            hideKeyboard();
            logoIsHidden = false;
            return true;
        }
        return false;
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
}
