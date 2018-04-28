package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentFull_presenter;
import com.brewmapp.presentation.view.contract.ProfileFragmentFull_view;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import br.com.sapereaude.maskedEditText.MaskedEditText;
import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;


public class ProfileFragmentFull extends BaseFragment implements ProfileFragmentFull_view {
    @BindView(R.id.fragment_profile_view_full_name)    TextView view_name;
    @BindView(R.id.fragment_profile_view_full_lastName)    TextView full_lastName;
    @BindView(R.id.fragment_profile_view_full_avatar)    RoundedImageView avatar;
    @BindView(R.id.fragment_profile_view_full_man)    RadioButton man;
    @BindView(R.id.fragment_profile_view_full_woman)    RadioButton woman;
    @BindView(R.id.fragment_profile_view_full_layout_status)    LinearLayout layout_status;
    @BindView(R.id.fragment_profile_view_full_layout_city)    ConstraintLayout layout_city;
    @BindView(R.id.fragment_profile_view_full_layout_family_status)    ConstraintLayout layout_family_status;
    @BindView(R.id.fragment_profile_view_full_layout_birthday)    ConstraintLayout layout_birthday;
    @BindView(R.id.fragment_profile_view_full_layout_phone)    ConstraintLayout layout_phone;
    @BindView(R.id.fragment_profile_view_full_layout_phone_dop)    ConstraintLayout layout_phone_dop;
    @BindView(R.id.fragment_profile_view_full_layout_skype)    ConstraintLayout layout_skype;
    @BindView(R.id.fragment_profile_view_full_layout_site)    ConstraintLayout layout_site;
    @BindView(R.id.fragment_profile_view_full_layout_work)    ConstraintLayout layout_work;
    @BindView(R.id.fragment_profile_view_full_layout_interest)    ConstraintLayout layout_interest;
    @BindView(R.id.fragment_profile_view_full_layout_music)    ConstraintLayout layout_music;
    @BindView(R.id.fragment_profile_view_full_layout_film)    ConstraintLayout layout_film;
    @BindView(R.id.fragment_profile_view_full_layout_book)    ConstraintLayout layout_book;
    @BindView(R.id.fragment_profile_view_full_layout_game)    ConstraintLayout layout_game;
    @BindView(R.id.fragment_profile_view_full_text_status)    TextView text_status;
    @BindView(R.id.fragment_profile_view_full_text_game)    TextView text_game;
    @BindView(R.id.fragment_profile_view_full_text_book)    TextView text_book;
    @BindView(R.id.fragment_profile_view_full_text_film)    TextView text_film;
    @BindView(R.id.fragment_profile_view_full_text_music)    TextView text_music;
    @BindView(R.id.fragment_profile_view_full_text_interest)    TextView text_interest;
    @BindView(R.id.fragment_profile_view_full_text_birthday)    TextView text_birthday;
    @BindView(R.id.fragment_profile_view_full_text_city)    TextView text_city;
    @BindView(R.id.fragment_profile_view_full_text_family_status)    TextView text_family_status;
    @BindView(R.id.fragment_profile_view_full_text_skype)    TextView text_skype;
    @BindView(R.id.fragment_profile_view_full_text_site)    TextView text_site;
    @BindView(R.id.fragment_profile_view_full_text_work)    TextView text_work;
    @BindView(R.id.fragment_profile_view_full_edit_text_phone)    MaskedEditText edit_text_phone;
    @BindView(R.id.fragment_profile_view_full_edit_text_phone_dop)    MaskedEditText edit_text_phone_dop;

    private OnFragmentInteractionListener mListener;

    @Inject    ProfileFragmentFull_presenter presenter;


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_view_full;
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        if(presenter.parseIntent(mListener.getIntent()))
            presenter.loadProfile();
        else
            mListener.commonError();
    }

    @Override
    protected void initView(View view) {


    }

    @Override
    public void showProfile(ArrayList<User> users) {
        //commonError();
        if(users.size()!=1){
            commonError();
            return;
        }else {
            User user=users.get(0);
            view_name.setText(user.getFirstname());
            full_lastName.setText(user.getLastname());
            if(user.getGender()==0) {
                woman.setChecked(true);
                avatar.setBackgroundResource(R.drawable.ic_user_woman);
            }else {
                man.setChecked(true);
                avatar.setBackgroundResource(R.drawable.ic_user_man);
            }

            Picasso.with(getContext()).load(user.getThumbnail()).fit().into(avatar);

            text_status.setText(user.getStatus());
            text_city.setText(user.getCountryCityFormated());
            text_game.setText(user.getGames());
            text_book.setText(user.getBooks());
            text_film.setText(user.getFilms());
            text_music.setText(user.getMusic());
            text_interest.setText(user.getInterests());
            text_birthday.setText(MapUtils.FormatDate(user.getBirthday()));
            //text_family_status.setText(user.getFamilyStatus());
            text_skype.setText(user.getSkype());
            text_site.setText(user.getSite());
            text_work.setText(user.getRestoWorkId());
            edit_text_phone.setText(user.getPhone());
            edit_text_phone_dop.setText(user.getAdditionalPhone());
        }



        mListener.setVisibleChildActivity();


    }


    //region Futctions
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
        return getString(R.string.text_podrobno);
    }

    @Override
    public void enableControls(boolean b, int i) {

    }


    @Override
    protected LivePresenter<?> getPresenter() {
        return null;
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
    public void commonError(String... message) {
        mListener.commonError(message);
    }
    //endregion



    //***********************************
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void setVisibleChildActivity();

        void finish();

        void sendActionParentActivity(int actionRefresh);

        void showSnackbar(String string);

        Intent getIntent();

        void commonError(String... strings);
    }

}
