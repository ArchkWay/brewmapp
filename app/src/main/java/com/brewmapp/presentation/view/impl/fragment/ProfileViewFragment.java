package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.squareup.picasso.Picasso;
import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileViewFragment extends BaseFragment implements ProfileViewFragmentView {

    @BindView(R.id.fragment_profile_edit_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_profile_edit_avatar)    ImageView avatar;
    @BindView(R.id.fragment_profile_edit_name)      TextView name;
    @BindView(R.id.fragment_profile_edit_lastName)    TextView lastName;
    @BindView(R.id.fragment_profile_edit_segmented)    SegmentedGroup segmentedGroup;
    @BindView(R.id.fragment_profile_edit_layout_phone)    ConstraintLayout layout_phone;
    @BindView(R.id.fragment_profile_edit_edit_text_phone)    MaskedEditText edit_text_phone;
    @BindView(R.id.fragment_profile_edit_text_status)    TextView text_status;
    @BindView(R.id.fragment_profile_edit_layout_birthday)    View layout_birthday;
    @BindView(R.id.fragment_profile_edit_text_birthday)    TextView text_birthday;
    @BindView(R.id.fragment_profile_edit_layout_city)    View layout_city;
    @BindView(R.id.fragment_profile_edit_text_city)    TextView text_city;
    @BindView(R.id.fragment_profile_edit_layout_family_status)    View layout_family_status;
    @BindView(R.id.fragment_profile_edit_text_family_status)    TextView text_family_status;
    @BindView(R.id.fragment_profile_edit_edit_text_phone_dop)    MaskedEditText edit_text_phone_dop;
    @BindView(R.id.fragment_profile_edit_edit_skype)    TextView edit_skype;
    @BindView(R.id.fragment_profile_edit_edit_site)    TextView edit_site;
    @BindView(R.id.fragment_profile_edit_edit_book)    TextView edit_book;
    @BindView(R.id.fragment_profile_edit_edit_film)    TextView edit_film;
    @BindView(R.id.fragment_profile_edit_edit_interest)    TextView edit_interest;
    @BindView(R.id.fragment_profile_edit_edit_game)    TextView edit_game;
    @BindView(R.id.fragment_profile_edit_edit_music)    TextView edit_music;
    @BindView(R.id.fragment_profile_edit_edit_work)    TextView edit_work;

    @BindView(R.id.fragment_profile_edit_layout_book)    View layout_book;
    @BindView(R.id.fragment_profile_edit_layout_film)    View layout_film;
    @BindView(R.id.fragment_profile_edit_layout_game)    View layout_game;
    @BindView(R.id.fragment_profile_edit_layout_interest)    View layout_interest;
    @BindView(R.id.fragment_profile_edit_layout_music)    View layout_music;
    @BindView(R.id.fragment_profile_edit_layout_work)    View layout_work;
    @BindView(R.id.fragment_profile_edit_layout_skype)    View layout_skype;
    @BindView(R.id.fragment_profile_edit_layout_site)    View layout_site;



    private OnFragmentInteractionListener mListener;


    @Inject    ProfileViewFragmentPresenter presenter;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_view;
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
        return getString(R.string.title_view_profile);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadContent(getActivity().getIntent());
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }


    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    public void refreshProfile(User user) {
        String tmpStr;

        if(!"http://www.placehold.it/100x100/EFEFEF/AAAAAA?".equals(user.getThumbnail())) {
            Picasso.with(getContext()).load(user.getThumbnail()).fit().centerCrop().into(avatar);
        }else {
            Picasso.with(getContext()).load(user.getGender()==1?R.drawable.ic_user_man:R.drawable.ic_user_woman).fit().into(avatar);
        }
        name.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        try {segmentedGroup.check(user.getGender()==1?R.id.fragment_profile_edit_man:R.id.fragment_profile_edit_woman);}catch (Exception e){}
        text_status.setText(user.getStatus());
        text_birthday.setText(user.getFormatedBirthday());
        try {
            text_family_status.setText(getResources().getStringArray(user.getGender()==1?R.array.family_status_man:R.array.family_status_women)[user.getFamilyStatus()]);
        }catch (Exception e){}

        text_city.setText(user.getCountryCityFormated());
        edit_text_phone.setText(user.getPhone());
        edit_text_phone_dop.setText(user.getAdditionalPhone());
        edit_book.setText(user.getBooks());
        edit_site.setText(user.getSite());
        edit_skype.setText(user.getSkype());
        edit_film.setText(user.getFilms());
        edit_interest.setText(user.getInterests());
        edit_music.setText(user.getMusic());
        edit_game.setText(user.getGames());

    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.ERROR)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.friens,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void invalidateOptionsMenu(){
        mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.INVALIDATE_MENU)));
    };

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_friends:
                presenter.finish(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
