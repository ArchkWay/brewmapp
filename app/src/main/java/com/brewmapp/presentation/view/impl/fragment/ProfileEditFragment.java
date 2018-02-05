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
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.User;

import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogSelectCountryCity;
import com.squareup.picasso.Picasso;

import java.io.File;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import info.hoang8f.android.segmented.SegmentedGroup;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;
import ru.frosteye.ovsa.tool.TextTools;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileEditFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileEditFragment extends BaseFragment implements ProfileEditFragmentView {

    @BindView(R.id.fragment_profile_edit_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_profile_edit_avatar)    ImageView avatar;
    @BindView(R.id.fragment_profile_edit_name)      EditText name;
    @BindView(R.id.fragment_profile_edit_lastName)    EditText lastName;
    @BindView(R.id.fragment_profile_edit_segmented)    SegmentedGroup segmentedGroup;
    @BindView(R.id.fragment_profile_edit_layout_phone)    ConstraintLayout layout_phone;
    @BindView(R.id.fragment_profile_edit_edit_text_phone)    MaskedEditText edit_text_phone;
    @BindView(R.id.fragment_profile_edit_text_status)    EditText text_status;
    @BindView(R.id.fragment_profile_edit_layout_birthday)    View layout_birthday;
    @BindView(R.id.fragment_profile_edit_text_birthday)    TextView text_birthday;
    @BindView(R.id.fragment_profile_edit_layout_city)    View layout_city;
    @BindView(R.id.fragment_profile_edit_text_city)    TextView text_city;
    @BindView(R.id.fragment_profile_edit_layout_family_status)    View layout_family_status;
    @BindView(R.id.fragment_profile_edit_text_family_status)    TextView text_family_status;
    @BindView(R.id.fragment_profile_edit_edit_text_phone_dop)    MaskedEditText edit_text_phone_dop;
    @BindView(R.id.fragment_profile_edit_edit_skype)    EditText edit_skype;
    @BindView(R.id.fragment_profile_edit_edit_site)    EditText edit_site;
    @BindView(R.id.fragment_profile_edit_edit_book)    EditText edit_book;
    @BindView(R.id.fragment_profile_edit_edit_film)    EditText edit_film;
    @BindView(R.id.fragment_profile_edit_edit_interest)    EditText edit_interest;
    @BindView(R.id.fragment_profile_edit_edit_game)    EditText edit_game;
    @BindView(R.id.fragment_profile_edit_edit_music)    EditText edit_music;
    @BindView(R.id.fragment_profile_edit_edit_work)    EditText edit_work;

    @BindView(R.id.fragment_profile_edit_layout_book)    View layout_book;
    @BindView(R.id.fragment_profile_edit_layout_film)    View layout_film;
    @BindView(R.id.fragment_profile_edit_layout_game)    View layout_game;
    @BindView(R.id.fragment_profile_edit_layout_interest)    View layout_interest;
    @BindView(R.id.fragment_profile_edit_layout_music)    View layout_music;
    @BindView(R.id.fragment_profile_edit_layout_work)    View layout_work;
    @BindView(R.id.fragment_profile_edit_layout_skype)    View layout_skype;
    @BindView(R.id.fragment_profile_edit_layout_site)    View layout_site;



    private OnFragmentInteractionListener mListener;


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
        return getActivity().getString(R.string.title_edit_profile);
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
        swipe.setRefreshing(true);
        User user=presenter.getUserWithNewData();
        registerTextChangeListeners(s ->{user.setFirstname(TextTools.extractTrimmed(name));invalidateOptionsMenu();},name);
        registerTextChangeListeners(s ->{user.setStatus(TextTools.extractTrimmed(text_status));invalidateOptionsMenu();},text_status);
        registerTextChangeListeners(s ->{user.setLastname(TextTools.extractTrimmed(lastName));invalidateOptionsMenu();},lastName);
        segmentedGroup.setOnCheckedChangeListener((group, checkedId) -> {user.setGender(checkedId== R.id.fragment_profile_edit_man?1:2);text_family_status.setText("");invalidateOptionsMenu();});
        layout_birthday.setOnClickListener(presenter.getOnClickBirthday(getActivity(),user,text_birthday));
        avatar.setOnClickListener(v->mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.SELECT_PHOTO))));
        layout_family_status.setOnClickListener(v -> showSelect(getActivity(), user.getGender() == 1 ? R.array.family_status_man : R.array.family_status_women, (text, position) -> {text_family_status.setText(text);user.setFamilyStatus(position);invalidateOptionsMenu();}));
        layout_city.setOnClickListener(v -> new DialogSelectCountryCity(getActivity(),getActivity().getSupportFragmentManager(), city -> {
            String nameCountry=getString(R.string.text_view_city_not_found);
            try {
                nameCountry=city.getRelations().getmCountry().getName();
            }catch (Exception e){

            }
                    text_city.setText(String.format("%s, %s",nameCountry,city.getName()));
                    user.setCityId(city.getId());
                    user.setCountryId(Integer.valueOf(city.getCountryId()));
                    getActivity().invalidateOptionsMenu();
        }));

        registerTextChangeListeners(s ->{user.setPhone(edit_text_phone.getRawText());invalidateOptionsMenu();},edit_text_phone);
        registerTextChangeListeners(s ->{user.setAdditionalPhone(edit_text_phone_dop.getRawText());invalidateOptionsMenu();},edit_text_phone_dop);
        registerTextChangeListeners(s ->{user.setSkype(TextTools.extractTrimmed(edit_skype));invalidateOptionsMenu();},edit_skype);
        registerTextChangeListeners(s ->{user.setSite(TextTools.extractTrimmed(edit_site));invalidateOptionsMenu();},edit_site);
        registerTextChangeListeners(s ->{user.setBooks(TextTools.extractTrimmed(edit_book));invalidateOptionsMenu();},edit_book);
        registerTextChangeListeners(s ->{user.setGames(TextTools.extractTrimmed(edit_game));invalidateOptionsMenu();},edit_game);
        registerTextChangeListeners(s ->{user.setMusic(TextTools.extractTrimmed(edit_music));invalidateOptionsMenu();},edit_music);
        registerTextChangeListeners(s ->{user.setInterests(TextTools.extractTrimmed(edit_interest));invalidateOptionsMenu();},edit_interest);
        registerTextChangeListeners(s ->{user.setFilms(TextTools.extractTrimmed(edit_film));invalidateOptionsMenu();},edit_film);

        layout_work.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        edit_work.setEnabled(false);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.save,menu);
        menu.findItem(R.id.action_save).setEnabled(presenter.isNeedSaveUser());
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                swipe.setRefreshing(true);
                presenter.save(mListener);
                hideKeyboard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void enableControls(boolean enabled, int code) {
    }

    @Override
    public void setContent(User user) {
        swipe.setRefreshing(false);

        String tmpStr;

        try {Picasso.with(getContext()).load(user.getThumbnail()).fit().into(avatar);}catch (Exception e){}

        name.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        try {segmentedGroup.check(user.getGender()==1?R.id.fragment_profile_edit_man:R.id.fragment_profile_edit_woman);}catch (Exception e){}
        text_status.setText(user.getStatus());
        tmpStr=user.getFormatedBirthday();        if(tmpStr!=null) text_birthday.setText(tmpStr);
        try {text_family_status.setText(getResources().getStringArray(user.getGender()==1?R.array.family_status_man:R.array.family_status_women)[user.getFamilyStatus()]);}catch (Exception e){}

        tmpStr=user.getCountryCityFormated();     if(tmpStr!=null)  text_city.setText(tmpStr);
        edit_text_phone.setText(user.getPhone());
        edit_text_phone_dop.setText(user.getAdditionalPhone());
        edit_book.setText(user.getBooks());
        edit_site.setText(user.getSite());
        edit_skype.setText(user.getSkype());
        edit_film.setText(user.getFilms());
        edit_interest.setText(user.getInterests());
        edit_music.setText(user.getMusic());
        edit_game.setText(user.getGames());
        text_city.setText(user.getCountryCityFormated());
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
    public void selectedPhoto(File file) {
        presenter.setNewPhoto(file);
        invalidateOptionsMenu();
        Picasso.with(getContext()).load(file).fit().into(avatar);
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
}
