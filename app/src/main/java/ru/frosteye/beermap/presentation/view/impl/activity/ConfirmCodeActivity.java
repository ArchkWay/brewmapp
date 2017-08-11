package ru.frosteye.beermap.presentation.view.impl.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.pojo.RegisterPackage;
import ru.frosteye.beermap.data.pojo.RegisterPackageWithPhone;
import ru.frosteye.beermap.presentation.presenter.contract.ConfirmPhonePresenter;
import ru.frosteye.beermap.presentation.view.contract.ConfirmPhoneView;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TimeCounter;

public class ConfirmCodeActivity extends BaseActivity implements ConfirmPhoneView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_confirmCode_code) EditText code;
    @BindView(R.id.activity_confirmCode_phone) MaskedEditText phone;
    @BindView(R.id.activity_confirmCode_hintCounter) TextView hintCounter;
    @BindView(R.id.activity_confirmCode_resend) Button resend;

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
        registerPackage = ((RegisterPackageWithPhone) getIntent().getSerializableExtra(RegisterPackage.KEY));
        enableBackButton();
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
        resend.setVisibility(View.GONE);
        counter.start((time, seconds) -> {
            runOnUiThread(() -> {
                if(seconds == 0) {
                    stopCounter();
                } else {
                    hintCounter.setText(getString(R.string.confirm_counter_pattern, time));
                }
            });
        }, 60, true);
    }

    private void stopCounter() {
        hintCounter.setVisibility(View.GONE);
        resend.setVisibility(View.VISIBLE);
        counter.stop();
    }
}
