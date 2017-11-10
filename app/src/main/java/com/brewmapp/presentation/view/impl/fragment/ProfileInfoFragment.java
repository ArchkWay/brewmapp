package com.brewmapp.presentation.view.impl.fragment;


import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.contract.ProfileInfoFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileInfoFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileInfoActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInfoFragment extends BaseFragment implements ProfileInfoFragmentView {


    @BindView(R.id.profile_info_activity_profile_avatar)    ImageView avatar;
    @BindView(R.id.profile_info_activity_profile_username)    TextView user_name;
    @BindView(R.id.profile_info_activity_profile_status)    TextView status;
    @BindView(R.id.profile_info_activity_profile_city)    TextView country_city;
    @BindView(R.id.profile_info_activity_profile_country)    TextView country;
    @BindView(R.id.profile_info_activity_profile_marital_status)    TextView marital_status;
    @BindView(R.id.profile_info_activity_profile_city2)    TextView city2;

    @Inject    ProfileInfoFragmentPresenter presenter;

    private OnFragmentInteractionListener mListener;

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
        return presenter.getTitle();
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void refreshProfile(User user) {

        Picasso.with(getContext()).load(user.getThumbnail()).fit().into(avatar);
        user_name.setText(user.getFormattedName());
        country_city.setText(user.getFormattedPlace());
        status.setText(R.string.online);

        try{country.setText(user.getRelations().getmCountry().getName());}catch (Exception e){};
        try{city2.setText(user.getRelations().getmCity().getName());}catch (Exception e){};
        //marital_status
        if(user.getGender()==1)
            marital_status.setText(user.getFamilyStatus()==1?R.string.marital_status_0:R.string.marital_status_1);
        else
            marital_status.setText(user.getFamilyStatus()==1?R.string.marital_status_2:R.string.marital_status_3);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_create:
                mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileInfoActivity.FRAGMENT_EDIT)));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.clear();
//        inflater.inflate(R.menu.save,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
