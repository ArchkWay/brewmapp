package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.facebook.login.widget.LoginButton;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.LoginPresenter;
import com.brewmapp.presentation.view.contract.LoginView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import ru.frosteye.ovsa.tool.TextTools;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_login_enter) Button enter;
    @BindView(R.id.activity_login_login)    MaskedEditText login;
    @BindView(R.id.activity_login_login_email)    AppCompatEditText login_email;
    @BindView(R.id.activity_login_password) EditText password;
    @BindView(R.id.activity_login_logo) View logoImage;
    @BindView(R.id.activity_login_fb) View fb;
    @BindView(R.id.activity_login_fbLogin) LoginButton fbLogin;
    @BindView(R.id.activity_login_rbGroup)    RadioGroup radioGroup;
    @BindView(R.id.activity_login_container)    LinearLayout container;

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
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean b=login.hasFocus()||login_email.hasFocus();
                if(b)
                    hideKeyboard();

                login.setVisibility(View.INVISIBLE);
                login_email.setVisibility(View.INVISIBLE);
                switch (checkedId){
                    case R.id.activity_login_rbGroup_phone:
                        login.setVisibility(View.VISIBLE);
                        if(b) {
                            login.requestFocus();
                            login.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    keyboard.showSoftInput(login, 0);
                                }
                            }, 500);
                        }
                        break;
                    case R.id.activity_login_rbGroup_email:
                        login_email.setVisibility(View.VISIBLE);
                        if(b) {
                            login_email.requestFocus();
                            login_email.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    keyboard.showSoftInput(login_email, 0);
                                }
                            }, 500);
                        }
                        break;
                }
                container.requestLayout();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.requestCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    private void login() {

        if(login.getVisibility()==View.VISIBLE) {
            if (TextTools.isTrimmedEmpty(login) || TextTools.isTrimmedEmpty(password)) {
                showSnackbarRed(getString(R.string.not_valid_login));
                return;
            }
        }else if(login_email.getVisibility()==View.VISIBLE) {
            if (!TextTools.validateEmail(login_email) || TextTools.isTrimmedEmpty(password)) {
                showSnackbarRed(getString(R.string.not_valid_login));
                return;
            }
        }

        String loginString = login.getVisibility()==View.VISIBLE?login.getRawText().trim():login_email.getText().toString().trim();

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

    @Override
    public void proceed() {
        startActivityAndClearTask(MainActivity.class);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public LoginButton getLoginButton() {
        return fbLogin;
    }
}
