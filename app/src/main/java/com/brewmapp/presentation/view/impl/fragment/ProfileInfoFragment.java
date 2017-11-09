package com.brewmapp.presentation.view.impl.fragment;


import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.contract.ProfileInfoFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoFragmentView;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInfoFragment extends BaseFragment implements ProfileInfoFragmentView {

    @BindView(R.id.profile_info_activity_profile_country)    TextView country;
    @BindView(R.id.profile_info_activity_profile_marital_status)    TextView marital_status;
    @BindView(R.id.profile_info_activity_profile_city2)    TextView city2;

    @Inject    ProfileInfoFragmentPresenter presenter;

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
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
    public int getFragmentLayout() {
        return R.layout.fragment_profil_info;
    }

    @Override
    public int getMenuToInflate() {
        return R.menu.profile;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(ProfileInfoFragment.this);
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void refreshProfile(User user) {
        try{country.setText(user.getRelations().getmCountry().getName());}catch (Exception e){};
        try{city2.setText(user.getRelations().getmCity().getName());}catch (Exception e){};
        //marital_status
        if(user.getGender()==1)
            marital_status.setText(user.getFamilyStatus()==1?R.string.marital_status_0:R.string.marital_status_1);
        else
            marital_status.setText(user.getFamilyStatus()==1?R.string.marital_status_2:R.string.marital_status_3);


    }

    @Override
    public void onBarAction(int id) {
        super.onBarAction(id);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
