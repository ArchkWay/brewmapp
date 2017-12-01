package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Interest_info;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.entity.wrapper.SubscriptionInfo;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.presentation.view.impl.activity.AssessmentsActivity;
import com.brewmapp.presentation.view.impl.activity.InterestListActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import info.hoang8f.android.segmented.SegmentedGroup;
import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.CardMenuField;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.wrapper.PostInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ProfilePresenter;
import com.brewmapp.presentation.view.contract.ProfileView;
import com.brewmapp.presentation.view.impl.activity.AlbumsActivity;
import com.brewmapp.presentation.view.impl.activity.FriendsActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.widget.InfoCounter;
import com.transitionseverywhere.TransitionManager;

import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

import static android.app.Activity.RESULT_OK;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;
import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_PROFILE;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfileFragment extends BaseFragment implements ProfileView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.fragment_profile_avatar) ImageView avatar;
    @BindView(R.id.fragment_profile_city) TextView city;
    @BindView(R.id.fragment_profile_app_bar)    AppBarLayout appBar;
    @BindView(R.id.fragment_profile_counter_friends) InfoCounter friendsCounter;
    @BindView(R.id.fragment_profile_counter_photos) InfoCounter photosCounter;
    @BindView(R.id.fragment_profile_counter_subscribers) InfoCounter subscribersCounter;
    @BindView(R.id.fragment_profile_counter_subscribes) InfoCounter subscribesCounter;
    @BindView(R.id.fragment_profile_status) TextView status;
    @BindView(R.id.fragment_profile_username) TextView username;
    @BindView(R.id.fragment_profile_menu) RecyclerView menu;
    @BindView(R.id.fragment_profile_posts) RecyclerView posts_subs;
    @BindView(R.id.fragment_profile_flow_segment) SegmentedGroup segment;
    @BindView(R.id.fragment_profile_text_no_record) TextView text_no_record;
    @BindView(R.id.fragment_profile_transitions_container) ViewGroup transitions_container;
    @BindView(R.id.fragment_profile_scrollView) ScrollView scrollView;

    @Inject    ProfilePresenter presenter;

    private FlexibleAdapter<CardMenuField> menuAdapter;
    private FlexibleModelAdapter<PostInfo> postAdapter;
    private FlexibleModelAdapter<SubscriptionInfo> subscriptionAdapter;
    private LoadPostsPackage loadPostsPackage = new LoadPostsPackage();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View view) {
        if(getArguments().getBoolean(MainActivity.KEY_FERST_FRAGMENT,false)){
            scrollView.setVisibility(View.INVISIBLE);
            transitions_container.postDelayed(new Runnable() {
                @Override
                public void run() {
                    TransitionManager.getDefaultTransition().setDuration(1000);
                    TransitionManager.beginDelayedTransition(transitions_container);
                    scrollView.setVisibility(View.VISIBLE);
                }
            },0);
        }


        menuAdapter = new FlexibleAdapter<>(CardMenuField.createProfileItems(getActivity()), this);
        menu.setLayoutManager(new LinearLayoutManager(getActivity()));
        menu.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        menu.setAdapter(menuAdapter);
        photosCounter.setOnClickListener(v -> startActivity(new Intent(getActivity(), AlbumsActivity.class)));
        friendsCounter.setOnClickListener(v -> startActivity(new Intent(getActivity(), FriendsActivity.class)));
        friendsCounter.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), FriendsActivity.class);
            intent.putExtra(Keys.SUBSCRIBERS, true);
            startActivity(intent);
        });

        segment.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId){
                case R.id.fragment_profile_posts_subscription:
                    loadPostsPackage.setPage(0);
                    loadPostsPackage.setSubs(true);
                    refreshItems();
                    break;
                case R.id.fragment_profile_posts_my:
                    loadPostsPackage.setPage(0);
                    loadPostsPackage.setSubs(false);
                    refreshItems();
                    break;

            }
        });

        postAdapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        subscriptionAdapter= new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        posts_subs.setLayoutManager(manager);
        posts_subs.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        //posts_subs.setAdapter(postAdapter);
        setHasOptionsMenu(true);
    }

    private void processAction(int action, Object payload) {
        switch (action) {
            case Actions.ACTION_LIKE_POST:
                presenter.onLikePost(((Post) payload));
                break;
            case Actions.ACTION_START_DETAILS_ACTIVITY:
                Interest interest=new Interest();
                Interest_info interest_info=new Interest_info();
                interest_info.setId((String)payload);
                interest.setInterest_info(interest_info);
                Intent intent=new Intent(getContext(), RestoDetailActivity.class);
                intent.putExtra(Keys.RESTO_ID,interest);
                startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
                break;
        }
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        segment.check(R.id.fragment_profile_posts_subscription);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return R.menu.profile;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return ResourceHelper.getString(R.string.my_profile);
    }

    @Override
    public void enableControls(boolean enabled, int code) {


    }

    @Override
    public void showUserProfile(UserProfile profile) {
        if(getActivity()==null) return;
        getActivity().setTitle(R.string.my_profile);
        Picasso.with(getActivity()).load(profile.getUser().getThumbnail()).fit().into(avatar);
        username.setText(profile.getUser().getFormattedName());
        status.setText(getString(R.string.online));
        friendsCounter.setCount(profile.getUser().getCounts().getFriends());
        photosCounter.setCount(profile.getUser().getCounts().getPhotos());
        subscribesCounter.setCount(profile.getUser().getSubscriptionsCount());
        subscribersCounter.setCount(profile.getUser().getCounts().getSubscribers());
        city.setText(profile.getUser().getFormattedPlace());
    }

    @Override
    public void appendPosts(Posts posts) {
        if(loadPostsPackage.getPage() == 0) postAdapter.clear();

        postAdapter.addItems(postAdapter.getItemCount(), posts.getModels());

        text_no_record.setVisibility(postAdapter.getItemCount()==0?View.VISIBLE:View.GONE);

        posts_subs.setAdapter(postAdapter);
    }

    @Override
    public void appendSubscriptions(Subscriptions subscriptions) {
        if(loadPostsPackage.getPage() == 0) subscriptionAdapter.clear();

        subscriptionAdapter.addItems(subscriptionAdapter.getItemCount(), subscriptions.getModels());

        text_no_record.setVisibility(subscriptionAdapter.getItemCount()==0?View.VISIBLE:View.GONE);

        posts_subs.setAdapter(subscriptionAdapter);
    }

    @Override
    public void onError() {
        status.setText(getString(R.string.offline));
        status.setTextColor(Color.RED);
    }

    @Override
    public boolean onItemClick(int position) {
        switch (position) {
            case 0:
                startActivityForResult(new Intent(getActivity(), NewPostActivity.class),REQUEST_CODE_REFRESH_ITEMS);
                break;
            case 1:
                startActivity(new Intent(getActivity(), AlbumsActivity.class));
                break;
            case 2:
                startActivity(new Intent(Keys.CAP_BEER,null,getActivity(), InterestListActivity.class));
                break;
            case 3:
                startActivityForResult(new Intent(Keys.CAP_RESTO,null,getActivity(), InterestListActivity.class),REQUEST_CODE_REFRESH_ITEMS);
                break;
            case 4:
                startActivity(new Intent(getActivity(), AssessmentsActivity.class));
                break;
        }
        return false;
    }

    @Override
    public void refreshState() {
        postAdapter.notifyDataSetChanged();
    }

    public void refreshItems() {
        if(loadPostsPackage.isSubs())
            presenter.onLoadSubscription(loadPostsPackage);
        else
            presenter.onLoadPosts(loadPostsPackage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK)
                    refreshItems();
                    return;

            case REQUEST_CODE_REFRESH_PROFILE:
                if(resultCode==RESULT_OK) {
                    presenter.refreshProfile();
                    refreshItems();
                }
                return;
                default:
                    super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBarAction(int id) {
        switch (id){
            case R.id.action_create:
                startActivityForResult(new Intent(getActivity(), ProfileEditActivity.class), REQUEST_CODE_REFRESH_PROFILE);
                return;
            default:
                super.onBarAction(id);
        }
    }

}
