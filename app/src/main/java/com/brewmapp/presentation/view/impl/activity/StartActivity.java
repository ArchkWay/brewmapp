package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.StartPresenter;
import com.brewmapp.presentation.view.contract.StartView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import com.facebook.login.widget.LoginButton;

public class StartActivity extends BaseActivity implements StartView {


    @BindView(R.id.activity_start_enter) View enter;
    @BindView(R.id.activity_start_register) View register;
    @BindView(R.id.activity_start_container) View container;
    @BindView(R.id.activity_start_check_connection) View check_connection;
    @BindView(R.id.activity_login_fbLogin)    LoginButton fbLogin;
    @BindView(R.id.activity_login_fb) View fb;
    @BindView(R.id.activity_start_terms_of_use)    TextView terms_of_use;

    @Inject StartPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }


    @Override
    public void enableControls(boolean enabled, int code) {
        container.setVisibility(enabled?View.VISIBLE:View.GONE);
        check_connection.setVisibility(enabled?View.GONE:View.VISIBLE);
        fb.setEnabled(enabled);
        fb.setAlpha(enabled ? 1.0f : 0.3f);

    }

    @Override
    protected void initView() {
        enter.setOnClickListener(v -> startActivity(LoginActivity.class));
        register.setOnClickListener(v -> startActivity(RegisterActivity.class));
        fb.setOnClickListener(v -> presenter.onFacebookLogin());
        String s="Нажимая \"Зарегистрироваться\", \"Войти с помощью Facebook\" или \"Войти\", я принимаю условия <a href=\"https://brewmapp.com/terms/\"  >Пользовательского соглашения</a>.";
        terms_of_use.setText(Html.fromHtml(s));
        terms_of_use.setMovementMethod(LinkMovementMethod.getInstance());

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
        startActivity(MainActivity.class);
        finish();
    }

    @Override
    public LoginButton getLoginButton() {
        return fbLogin;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.requestCallbackManager().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Activity getActivity() {
        return this;
    }

}
