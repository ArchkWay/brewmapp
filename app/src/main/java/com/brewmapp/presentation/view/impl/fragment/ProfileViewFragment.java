package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.CardMenuField;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.widget.InfoCounter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileViewFragment extends BaseFragment implements ProfileViewFragmentView , FlexibleAdapter.OnItemClickListener{

    @BindView(R.id.fragment_profile_view_avatar)    ImageView avatar;
    @BindView(R.id.fragment_profile_view_name)      TextView name;
    @BindView(R.id.fragment_profile_view_place)      TextView place;
    @BindView(R.id.fragment_profile_view_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_profile_view_counter_friends)    InfoCounter counter_friends;
    @BindView(R.id.fragment_profile_view_counter_photos)    InfoCounter counter_photos;
    @BindView(R.id.fragment_profile_view_counter_subscribers)    InfoCounter counter_subscribers;
    @BindView(R.id.fragment_profile_view_counter_subscribes)    InfoCounter counter_subscribes;
    @BindView(R.id.fragment_profile_view_menu)    RecyclerView menu;
    @BindView(R.id.fragment_profile_button_private_message) Button private_message;

    private OnFragmentInteractionListener mListener;
    private List<CardMenuField> cardMenuFields = new ArrayList<>();
    private User user;

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
        cardMenuFields=CardMenuField.createProfileViewItems(getActivity());
        menu.setLayoutManager(new LinearLayoutManager(getActivity()));
        menu.setAdapter(new FlexibleAdapter<>(cardMenuFields, this));
        private_message.setOnClickListener(v -> Starter.MultiFragmentActivity_MODE_CHAT(getActivity(),user));

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
    public void setContent(User user) {
        this.user=user;
        if(!"http://www.placehold.it/100x100/EFEFEF/AAAAAA?".equals(user.getThumbnail())) {
            Picasso.with(getContext()).load(user.getThumbnail()).fit().centerCrop().into(avatar);
        }else {
            Picasso.with(getContext()).load(user.getGender()==1?R.drawable.ic_user_man:R.drawable.ic_user_woman).fit().into(avatar);
        }
        name.setText(user.getFormattedName());
        place.setText(user.getCountryCityFormated());
        counter_friends.setCount(user.getCounts().getFriends());
        counter_photos.setCount(user.getCounts().getPhotos());
        counter_subscribers.setCount(user.getCounts().getSubscriptions());
        counter_subscribes.setCount(user.getCounts().getSubscribers());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_friends:
                presenter.finish(getActivity());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemClick(int position) {
        switch (cardMenuFields.get(position).getId()){
            case CardMenuField.FAVORITE_BEER:
                Starter.InterestListActivity(getActivity(), Keys.CAP_BEER,user.getId());
                break;
            case CardMenuField.FAVORITE_RESTO:
                Starter.InterestListActivity(getActivity(), Keys.CAP_RESTO,user.getId());
                break;
            case CardMenuField.MY_RATINGS:
                Starter.MultiListActivity(getActivity(), MultiListView.MODE_SHOW_ALL_MY_EVALUATION);
                break;
        }
        return false;
    }

    //***********************************
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
