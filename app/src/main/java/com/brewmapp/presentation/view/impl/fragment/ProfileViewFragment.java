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
import com.brewmapp.app.environment.Actions;
import com.brewmapp.app.environment.Starter;
import com.brewmapp.data.entity.CardMenuField;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ProfileViewFragmentPresenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.contract.ProfileViewFragmentView;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.widget.InfoCounter;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
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
public class ProfileViewFragment extends BaseFragment implements ProfileViewFragmentView , FlexibleAdapter.OnItemClickListener, View.OnClickListener{

    //region BindView
    @BindView(R.id.fragment_profile_view_avatar)    ImageView avatar;
    @BindView(R.id.fragment_profile_view_name)      TextView name;
    @BindView(R.id.fragment_profile_view_place)      TextView place;
    @BindView(R.id.fragment_profile_view_time)      TextView time;
    @BindView(R.id.fragment_profile_view_swipe)    RefreshableSwipeRefreshLayout swipe;
    @BindView(R.id.fragment_profile_view_counter_friends)    InfoCounter counter_friends;
    @BindView(R.id.fragment_profile_view_counter_photos)    InfoCounter counter_photos;
    @BindView(R.id.fragment_profile_view_counter_subscribers)    InfoCounter counter_subscribers;
    @BindView(R.id.fragment_profile_view_counter_subscribes)    InfoCounter counter_subscribes;
    @BindView(R.id.fragment_profile_view_menu)    RecyclerView menu;
    @BindView(R.id.fragment_profile_view_request)    Button view_request;
    @BindView(R.id.fragment_profile_button_private_message) Button private_message;
    @BindView(R.id.fragment_profile_view_information) ImageView view_information;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private List<CardMenuField> cardMenuFields = new ArrayList<>();
    private User user;
    //endregion

    //region Inject
    @Inject    ProfileViewFragmentPresenter presenter;
    //endregion

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_view;
    }

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        cardMenuFields=CardMenuField.createProfileViewItems(getActivity());
        menu.setLayoutManager(new LinearLayoutManager(getActivity()));
        menu.setAdapter(new FlexibleAdapter<>(cardMenuFields, this));
        private_message.setOnClickListener(v -> Starter.MultiFragmentActivity_MODE_CHAT(getActivity(),user));
        view_information.setOnClickListener(v->
//                Starter.ProfileEditActivity_StartInVisible(
//                (BaseActivity) getActivity(),
//                String.valueOf(ProfileEditView.SHOW_PROFILE_FRAGMENT_VIEW_FULL),
//                getActivity().getIntent().getData().toString())
                        showMessage(getString(R.string.message_develop))
        );
        view_request.setOnClickListener(this);
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.loadContent(getActivity().getIntent());
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
        counter_subscribers.setCount(user.getCounts().getSubscribers());

        time.setText(MapUtils.FormatDate(user.getLastLogin()));

        presenter.loadSubscription();

    }

    @Override
    public void setSubscriptions(Subscriptions subscriptions) {

        //region set Title menu Subscription
        List<SubscriptionInfo> subscriptionInfoList = subscriptions.getModels();
        CardMenuField cardMenuField=CardMenuField.getItemProfileViewItemsById(CardMenuField.SUBSCRIBE);
        if(cardMenuField!=null) {
            Iterator<SubscriptionInfo> subscriptionInfoIterator = subscriptionInfoList.iterator();
            while (subscriptionInfoIterator.hasNext()) {
                SubscriptionInfo subscriptionInfo = subscriptionInfoIterator.next();
                Subscription subscription = subscriptionInfo.getModel();
                if (Keys.CAP_USER.equals(subscription.getRelated_model())) {
                    if (subscription.getRelated_id().equals(presenter.getUser_id())) {
                        cardMenuField.setTitle(getString(R.string.fav_unsubscrabe));
                        cardMenuField.setExternalId(subscription.getId());
                    }else
                        cardMenuField.setTitle(getString(R.string.fav_subscrabe));
                    menu.getAdapter().notifyDataSetChanged();
                    break;
                }
            }
        }
        //endregion
        counter_subscribes.setCount(subscriptionInfoList.size());
        presenter.loadNews();

    }

    @Override
    public void setNews(Posts posts) {

        presenter.loadStatusUser();
    }

    @Override
    public void subscriptionSuccess() {
        CardMenuField.getItemProfileViewItemsById(CardMenuField.SUBSCRIBE).setTitle(getString(R.string.fav_subscrabe));
        menu.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void unSubscriptionSuccess() {
        CardMenuField.getItemProfileViewItemsById(CardMenuField.SUBSCRIBE).setTitle(getString(R.string.fav_unsubscrabe));
        menu.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void setStatusFriend(int status) {
        view_request.setTag(status);
        switch (status){
            case FriendsView.FRIENDS_NOW:
                view_request.setBackground(getResources().getDrawable(R.drawable.selector_button_red));
                view_request.setText(R.string.text_button_friend_delete_full);
                break;
            case FriendsView.FRIENDS_REQUEST_IN:
                view_request.setText(R.string.text_button_friend_accept_full);
                break;
            case FriendsView.FRIENDS_REQUEST_OUT:
                view_request.setBackground(getResources().getDrawable(R.drawable.selector_button_gray));
                view_request.setText(R.string.text_button_friend_request_send);
                break;
            case FriendsView.FRIENDS_NOBODY:
                view_request.setBackground(getResources().getDrawable(R.drawable.selector_button_green_solid));
                view_request.setText(R.string.button_text_request);
                break;
        }
        mListener.setVisibleChildActivity();
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
            case CardMenuField.SUBSCRIBE:
                CardMenuField cardMenuField=CardMenuField.getItemProfileViewItemsById(CardMenuField.SUBSCRIBE);
                String title=cardMenuField.getTitle();
                String text_subsOFF=getString(R.string.fav_unsubscrabe);
                String text_subsON=getString(R.string.fav_subscrabe);
                if(title.equals(text_subsOFF)) {
                    presenter.SubscriptionOnTask();
                }else if(title.equals(text_subsON)){
                    presenter.SubscriptionOffTask(cardMenuField.getExternalId());
                }else {
                    commonError();
                }
                break;
            case CardMenuField.MY_RATINGS:
            case CardMenuField.MY_RESUME:
            case CardMenuField.MY_WORK:
                //Starter.MultiListActivity(getActivity(), MultiListView.MODE_SHOW_ALL_MY_EVALUATION);
                showMessage(getString(R.string.message_develop));
                break;

        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_profile_view_request:
                int tag;
                try {
                    tag= (int) v.getTag();
                }catch (Exception e){return;}

                switch (tag){
                    case FriendsView.FRIENDS_REQUEST_OUT:
                        presenter.deleteFriend(getFragmentManager(),getString(R.string.text_request_cancel));
                        break;
                    case FriendsView.FRIENDS_NOW:
                        presenter.deleteFriend(getFragmentManager(),getString(R.string.text_request_friend_delete));
                        break;
                    case FriendsView.FRIENDS_REQUEST_IN:
                        presenter.allowFriens(getFragmentManager(),getString(R.string.text_request_reject));
                        break;
                    case FriendsView.FRIENDS_NOBODY:
                        presenter.sendRequestFriends(getFragmentManager(),getString(R.string.request_friends));
                        break;

                }

                break;
        }
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
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public void enableControls(boolean enabled, int code) {
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
    public void friendDeletedSuccess() {
        mListener.sendActionParentActivity(Actions.ACTION_REFRESH);
        mListener.showSnackbar(getString(R.string.text_friend_deleted_success));
        setStatusFriend(FriendsView.FRIENDS_NOBODY);
    }

    @Override
    public void friendAllowSuccess() {
        mListener.sendActionParentActivity(Actions.ACTION_REFRESH);
        mListener.showSnackbar(getString(R.string.text_friend_added_success));
        setStatusFriend(FriendsView.FRIENDS_NOW);
    }

    @Override
    public void requestSendSuccess() {
        mListener.sendActionParentActivity(Actions.ACTION_REFRESH);
        mListener.showSnackbar(getString(R.string.text_friend_sent_success));
        setStatusFriend(FriendsView.FRIENDS_REQUEST_OUT);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.stub,menu);
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


    //***********************************
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
        void setVisibleChildActivity();

        void setResult(int resultOk);

        void finish();

        void sendActionParentActivity(int actionRefresh);

        void showSnackbar(String string);
    }

}
