package com.brewmapp.presentation.view.impl.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.pojo.RegisterPackage;
import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.presentation.presenter.contract.ConfirmPhonePresenter;
import com.brewmapp.presentation.view.contract.ConfirmPhoneView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TimeCounter;

public class ConfirmCodeActivity extends BaseActivity implements ConfirmPhoneView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_confirmCode_code) EditText code;
    @BindView(R.id.activity_confirmCode_phone) MaskedEditText phone;
    @BindView(R.id.activity_confirmCode_hintCounter) TextView hintCounter;
    @BindView(R.id.activity_confirmCode_resend) Button resend;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;

    @Inject ConfirmPhonePresenter presenter;

    private RegisterPackageWithPhone registerPackage;
    private TimeCounter counter = new TimeCounter();

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);
    }

    @Override
    protected void initView() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        toolbarTitle.setText(getTitle());
        enableBackButton();
        registerPackage = ((RegisterPackageWithPhone) getIntent().getSerializableExtra(RegisterPackage.KEY));

        phone.setText(registerPackage.getPhone());
        resend.setOnClickListener(v -> presenter.onPhoneReady(registerPackage.getPhone()));
        registerTextChangeListeners(s -> {
            if(s.length() == 4) {
                presenter.onCodeReady(s.toString());
            }
        }, code);
    }

    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.onPhoneReady(registerPackage.getPhone());
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
        showTopBarLoading(!enabled);
        this.code.setEnabled(enabled);
        resend.setEnabled(enabled);
    }

    @Override
    public void proceed() {
        stopCounter();
        Intent intent = new Intent(this, EnterPasswordActivity.class);
        intent.putExtra(RegisterPackage.KEY, registerPackage);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        counter.stop();
    }

    @Override
    public void die() {
        code.setText(null);
    }

    @Override
    public void startCounter() {
        hintCounter.setVisibility(View.VISIBLE);
        resend.setEnabled(false);
        counter.start((time, seconds) -> {
            runOnUiThread(() -> {
                if(seconds == 0) {
                    stopCounter();
                } else {
                    hintCounter.setText(getString(R.string.confirm_counter_pattern, DateUtils.formatElapsedTime(seconds)));
                }
            });
        }, 300, true);
    }

    private void stopCounter() {
        hintCounter.setVisibility(View.INVISIBLE);
        resend.setEnabled(true);
        counter.stop();
    }


}
