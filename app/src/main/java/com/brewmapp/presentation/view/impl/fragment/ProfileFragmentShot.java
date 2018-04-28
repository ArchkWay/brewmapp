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
import com.brewmapp.data.entity.Stub;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.contract.InfoItem_view;
import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;
import com.brewmapp.data.entity.wrapper.InfoItem;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentShot_presenter;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.contract.ProfileActivity_view;
import com.brewmapp.presentation.view.contract.ProfileFragmentShot_view;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileActivity;
import com.brewmapp.presentation.view.impl.widget.InfoCounter;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.swipe.SwipeRefreshLayoutBottom;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragmentShot.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ProfileFragmentShot extends BaseFragment implements ProfileFragmentShot_view, FlexibleAdapter.OnItemClickListener, View.OnClickListener{

    //region BindView
    @BindView(R.id.fragment_profile_view_avatar)    ImageView avatar;
    @BindView(R.id.fragment_profile_view_name)      TextView name;
    @BindView(R.id.fragment_profile_view_place)      TextView place;
    @BindView(R.id.fragment_profile_view_time)      TextView time;
    @BindView(R.id.fragment_profile_view_counter_friends)    InfoCounter counter_friends;
    @BindView(R.id.fragment_profile_view_counter_photos)    InfoCounter counter_photos;
    @BindView(R.id.fragment_profile_view_counter_subscribers)    InfoCounter counter_subscribers;
    @BindView(R.id.fragment_profile_view_counter_subscribes)    InfoCounter counter_subscribes;
    @BindView(R.id.fragment_profile_view_menu)    RecyclerView menu;
    @BindView(R.id.fragment_profile_view_request)    Button view_request;
    @BindView(R.id.fragment_profile_button_private_message) Button private_message;
    @BindView(R.id.fragment_profile_view_information) ImageView view_information;
    @BindView(R.id.fragment_profile_view_swipe)    SwipeRefreshLayoutBottom refreshLayout;
    //endregion

    //region Private
    private OnFragmentInteractionListener mListener;
    private List<IFlexible> cardMenuFields = new ArrayList<>();
    private User user;
    //endregion

    //region Inject
    @Inject    ProfileFragmentShot_presenter presenter;
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
        menu.setAdapter(new FlexibleModelAdapter<>(cardMenuFields, this));

        private_message.setOnClickListener(v -> Starter.MultiFragmentActivity_MODE_CHAT(getActivity(),user));
        view_information.setOnClickListener(v->
                Starter.ProfileEditActivity_StartInVisible(
                (BaseActivity) getActivity(),
                String.valueOf(ProfileActivity_view.SHOW_PROFILE_FRAGMENT_VIEW_FULL),
                getActivity().getIntent().getData().toString())
        );
        view_request.setOnClickListener(this);
        counter_photos.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(() -> refreshLayout.postDelayed(() -> refreshLayout.setRefreshing(false),1000));
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
        CardMenuField cardMenuField= getCardMenuFieldById(CardMenuField.SUBSCRIBE);
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

        List<PostInfo> postInfoList=posts.getModels();
        cardMenuFields.add(new InfoItem("", InfoItem_view.SEPARATOR));
        if(postInfoList.size()==0){
            cardMenuFields.add(new InfoItem(getString(R.string.text_no_while_news), InfoItem_view.WHILE_NOT_EXIST_SUBSCRIBE));
        }else {
            cardMenuFields.addAll(postInfoList);
        }
        FlexibleAdapter flexibleAdapter = (FlexibleAdapter) menu.getAdapter();
        flexibleAdapter.notifyItemRangeInserted(flexibleAdapter.getItemCount(), postInfoList.size());
        presenter.loadStatusUser();
    }

    @Override
    public void subscriptionSuccess(String subscription_id) {
        CardMenuField cardMenuField= getCardMenuFieldById(CardMenuField.SUBSCRIBE);
        if(cardMenuField!=null) {
            cardMenuField.setTitle(getString(R.string.fav_unsubscrabe));
            cardMenuField.setExternalId(subscription_id);
            menu.getAdapter().notifyItemChanged(getPositionCardMenuFieldById(CardMenuField.SUBSCRIBE));
        }
    }

    @Override
    public void unSubscriptionSuccess() {
        CardMenuField cardMenuField= getCardMenuFieldById(CardMenuField.SUBSCRIBE);
        if(cardMenuField!=null) {
            cardMenuField.setTitle(getString(R.string.fav_subscrabe));
            menu.getAdapter().notifyItemChanged(getPositionCardMenuFieldById(CardMenuField.SUBSCRIBE));
        }
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
        IFlexible iFlexible=cardMenuFields.get(position);
        if(iFlexible instanceof CardMenuField) {
            CardMenuField cardMenuField= (CardMenuField) iFlexible;
            switch (cardMenuField.getId()){
                case CardMenuField.FAVORITE_BEER:
                    Starter.InterestListActivity(getActivity(), Keys.CAP_BEER, user.getId());
                    break;
                case CardMenuField.FAVORITE_RESTO:
                    Starter.InterestListActivity(getActivity(), Keys.CAP_RESTO, user.getId());
                    break;
                case CardMenuField.SUBSCRIBE:
                    String title = cardMenuField.getTitle();
                    String text_subsON = getString(R.string.fav_unsubscrabe);
                    String text_subsOFF = getString(R.string.fav_subscrabe);
                    if (title.equals(text_subsOFF)) {
                        presenter.SubscriptionOnTask();
                    } else if (title.equals(text_subsON)) {
                        presenter.SubscriptionOffTask(cardMenuField.getExternalId());
                    } else {
                        commonError();
                    }
                    break;
                case CardMenuField.MY_RATINGS:
                    Starter.MultiListActivity(getActivity(), MultiListView.MODE_SHOW_ALL_MY_EVALUATION,presenter.getUser_id());
                    break;
                case CardMenuField.MY_RESUME:
                    Starter.MultiListActivity(getActivity(), MultiListView.MODE_SHOW_MY_RESUME,presenter.getUser_id());
                    break;
                case CardMenuField.MY_WORK:
                    Starter.MultiListActivity(getActivity(), MultiListView.MODE_SHOW_MY_WORK,presenter.getUser_id());
                    break;

            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_profile_view_request:
                //region Friends control
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
                //endregion
                break;
            case R.id.fragment_profile_view_counter_photos:
                Starter.AlbumsActivity(((BaseActivity)getActivity()),Integer.valueOf(presenter.getUser_id()));
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
        mListener.onFragmentInteraction(Uri.parse(Integer.toString(ProfileActivity.ERROR)));
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


    //region Funtions
    private CardMenuField getCardMenuFieldById(int menu_id) {
        int position= getPositionCardMenuFieldById(menu_id);
        if(position>=0){
            return (CardMenuField) cardMenuFields.get(position);
        }else
            return null;
    }

    private int getPositionCardMenuFieldById(int menu_id){
        int position=0;
        Iterator<IFlexible> iterator=cardMenuFields.iterator();
        while (iterator.hasNext()){
            IFlexible iFlexible=iterator.next();
            if(iFlexible instanceof CardMenuField){
                CardMenuField cardMenuField= (CardMenuField) iFlexible;
                if(cardMenuField.getId()==menu_id)
                    return position;
            }
            position++;
        }
        return -1;
    }
    //endregion


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
