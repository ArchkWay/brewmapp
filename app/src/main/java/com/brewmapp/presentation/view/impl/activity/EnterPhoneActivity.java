package com.brewmapp.presentation.view.impl.activity;

import javax.inject.Inject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.pojo.RegisterPackage;
import com.brewmapp.data.pojo.RegisterPackageWithPhone;
import com.brewmapp.presentation.presenter.contract.RegisterPresenter;
import com.brewmapp.presentation.view.contract.RegisterView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import com.brewmapp.R;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

public class EnterPhoneActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.activity_enterPhone_bottom_phone) MaskedEditText phone;
    @BindView(R.id.activity_enterPhone_bottom_country) LinearLayout country;
    @BindView(R.id.common_toolbar) Toolbar toolbar;
    @BindView(R.id.common_toolbar_title)    TextView toolbarTitle;
    @BindView(R.id.common_toolbar_subtitle)    TextView toolbarSubTitle;
    @BindView(R.id.common_toolbar_dropdown)    LinearLayout toolbarDropdown;


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
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarDropdown.setVisibility(View.VISIBLE);
        toolbarSubTitle.setVisibility(View.GONE);
        toolbarTitle.setText(getTitle());
        enableBackButton();

        registerPackage = new RegisterPackageWithPhone(
                ((RegisterPackage) getIntent().getSerializableExtra(RegisterPackage.KEY))
        );
        registerTextChangeListeners(s -> {
            registerPackage.setPhone("7" + phone.getRawText());
            invalidateOptionsMenu();
        }, phone);

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterPhoneActivity.this, SelectCategoryActivity.class);
                intent.putExtra(Actions.PARAM1, SearchFragment.CATEGORY_LIST_BEER );
                intent.putExtra(Actions.PARAM2,1);
                intent.putExtra(Actions.PARAM3,"null");
                startActivityForResult(intent, RequestCodes.REQUEST_SEARCH_CODE);


            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
