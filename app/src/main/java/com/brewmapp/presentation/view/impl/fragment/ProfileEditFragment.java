package com.brewmapp.presentation.view.impl.fragment;

import android.app.DatePickerDialog;
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

import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
    @BindView(R.id.fragment_profile_edit_name)      EditText name;
    @BindView(R.id.fragment_profile_edit_lastName)    EditText lastName;
    @BindView(R.id.fragment_profile_edit_segmented)    SegmentedGroup segmentedGroup;
    @BindView(R.id.fragment_profile_edit_layout_phone)    ConstraintLayout layout_phone;
    @BindView(R.id.fragment_profile_edit_edit_text_phone)    EditText edit_text_phone;
    @BindView(R.id.fragment_profile_edit_text_status)    EditText text_status;
    @BindView(R.id.fragment_profile_edit_layout_birthday)    View layout_birthday;
    @BindView(R.id.fragment_profile_edit_text_birthday)    TextView text_birthday;
    @BindView(R.id.fragment_profile_edit_layout_city)    View layout_city;
    @BindView(R.id.fragment_profile_edit_text_city)    TextView text_city;
    @BindView(R.id.fragment_profile_edit_layout_family_status)    View layout_family_status;
    @BindView(R.id.fragment_profile_edit_text_family_status)    TextView text_family_status;
    @BindView(R.id.fragment_profile_edit_edit_text_phone_dop)    EditText edit_text_phone_dop;
    @BindView(R.id.fragment_profile_edit_edit_skype)    EditText edit_skype;
    @BindView(R.id.fragment_profile_edit_edit_site)    EditText edit_site;

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
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);

        User user=presenter.getUserWithNewData();
        registerTextChangeListeners(s ->{user.setFirstname(TextTools.extractTrimmed(name));invalidateOptionsMenu();},name);
        registerTextChangeListeners(s ->{user.setStatus(TextTools.extractTrimmed(text_status));invalidateOptionsMenu();},text_status);
        registerTextChangeListeners(s ->{user.setLastname(TextTools.extractTrimmed(lastName));invalidateOptionsMenu();},lastName);
        segmentedGroup.setOnCheckedChangeListener((group, checkedId) -> {user.setGender(checkedId== R.id.fragment_profile_edit_man?1:0);text_family_status.setText("");invalidateOptionsMenu();});
        layout_birthday.setOnClickListener(v -> {Calendar calendar = Calendar.getInstance();new DatePickerDialog(getActivity(), (view, year, month, dayOfMonth) -> {text_birthday.setText(String.format("%s-%s-%s",String.valueOf(year),String.valueOf(month),String.valueOf(dayOfMonth)));user.setBirthday(new GregorianCalendar(year, month, dayOfMonth).getTime());invalidateOptionsMenu();}, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();});
        avatar.setOnClickListener(v->mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.FRAGMENT_SELECT_PHOTO))));
        layout_family_status.setOnClickListener(v -> showSelect(getActivity(), user.getGender() == 1 ? R.array.family_status_man : R.array.family_status_women, (text, position) -> {text_family_status.setText(text);user.setFamilyStatus(position);}));

        //edit_text_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        edit_text_phone.setEnabled(false);
        edit_text_phone_dop.setEnabled(false);
        text_city.setEnabled(false);
        edit_skype.setEnabled(false);
        edit_site.setEnabled(false);

        layout_book.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_city.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_game.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_phone.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_music.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_work.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_skype.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
        layout_site.setOnClickListener(v -> showMessage(getString(R.string.message_develop)));
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
    public void refreshProfile(User user) {
        if(!"http://www.placehold.it/100x100/EFEFEF/AAAAAA?".equals(user.getThumbnail()))
            Picasso.with(getContext()).load(user.getThumbnail()).fit().into(avatar);
        name.setText(user.getFirstname());
        lastName.setText(user.getLastname());
        segmentedGroup.check(user.getGender()==1?R.id.fragment_profile_edit_man:R.id.fragment_profile_edit_woman);
        text_status.setText(user.getStatus());
        if(user.getBirthday().getYear()>0)
            text_birthday.setText(String.format("%s-%s-%s",user.getBirthday().getYear(),user.getBirthday().getMonth(),user.getBirthday().getDay()));
        text_family_status.setText(getResources().getStringArray(user.getGender()==1?R.array.family_status_man:R.array.family_status_women)[user.getFamilyStatus()]);

    }

    @Override
    public void commonError(String... strings) {
        if(strings.length==0)
            showMessage(getString(R.string.error));
        else
            showMessage(strings[0]);
        mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.FRAGMENT_ERROR)));
    }

    @Override
    public void selectedPhoto(File file) {
        presenter.getUserWithNewData().setThumbnail(file.getAbsolutePath());
        invalidateOptionsMenu();
    }

    private void invalidateOptionsMenu(){
        mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileEditActivity.FRAGMENT_INVALIDATE_MENU)));
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
