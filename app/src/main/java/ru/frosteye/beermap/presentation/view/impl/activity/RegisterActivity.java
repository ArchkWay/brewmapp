package ru.frosteye.beermap.presentation.view.impl.activity;

import javax.inject.Inject;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RadioGroup;

import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.pojo.RegisterPackage;
import ru.frosteye.beermap.presentation.presenter.contract.RegisterPresenter;
import ru.frosteye.beermap.presentation.view.contract.RegisterView;
import ru.frosteye.ovsa.presentation.callback.SimpleTextChangeCallback;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import ru.frosteye.beermap.R;
import ru.frosteye.ovsa.tool.TextTools;

public class RegisterActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.activity_register_lastName) EditText lastName;
    @BindView(R.id.activity_register_name) EditText firstName;
    @BindView(R.id.activity_register_segmented) SegmentedGroup segmented;

    @Inject RegisterPresenter presenter;
    @Inject RegisterPackage registerPackage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected Toolbar findActionBar() {
        return toolbar;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void initView() {
        enableBackButton();
        registerTextChangeListeners(s -> {
            registerPackage.setFirstName(TextTools.extractTrimmed(firstName));
            registerPackage.setLastName(TextTools.extractTrimmed(lastName));
            invalidateOptionsMenu();
        }, firstName, lastName);
        segmented.setOnCheckedChangeListener((group, checkedId) -> {
            registerPackage.setGender(checkedId == R.id.activity_register_man ? 1 : 2);
            invalidateOptionsMenu();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.next, menu);
        return registerPackage.validate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(ConfirmPhoneActivity.class);
        return true;
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
