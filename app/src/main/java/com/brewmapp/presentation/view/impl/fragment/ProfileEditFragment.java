package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.pojo.ProfileChangePackage;
import com.brewmapp.presentation.presenter.impl.ProfileEditFragmentPresenterImpl;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileInfoActivity;
import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.tool.TextTools;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileEditFragment extends BaseFragment implements ProfileEditFragmentView {

    @BindView(R.id.fragment_profile_edit_avatar)    ImageView avatar;
    @BindView(R.id.fragment_profile_edit_name)    EditText name;
    @BindView(R.id.fragment_profile_edit_lastName)    EditText lastName;
    @BindView(R.id.fragment_profile_edit_segmented)    SegmentedGroup segmentedGroup;

    private OnFragmentInteractionListener mListener;
    private ProfileChangePackage profileChangePackage=new ProfileChangePackage();

    @Inject    ProfileEditFragmentPresenter presenter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_edit;
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

    @Override
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return presenter.getTitle();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        segmentedGroup.setOnCheckedChangeListener(presenter.getOnCheckedChangeListener());
        registerTextChangeListeners(s ->{
            profileChangePackage.setFirstName(TextTools.extractTrimmed(name));
            mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileInfoActivity.FRAGMENT_INVALIDATE_MENU)));
            },name);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                presenter.save();
                mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileInfoActivity.FRAGMENT_INFO)));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void refreshProfile(User user) {
        Picasso.with(getContext()).load(user.getThumbnail()).fit().into(avatar);
        name.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        segmentedGroup.check(user.getGender()==1?R.id.fragment_profile_edit_man:R.id.fragment_profile_edit_woman);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
