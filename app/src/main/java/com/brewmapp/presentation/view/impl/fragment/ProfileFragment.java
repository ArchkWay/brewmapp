package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.presentation.view.impl.activity.AssessmentsActivity;
import com.brewmapp.presentation.view.impl.activity.FavoriteBeerActivity;
import com.brewmapp.presentation.view.impl.activity.FavoriteRestoActivity;
import com.brewmapp.utils.Cons;
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
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

import static android.app.Activity.RESULT_OK;

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
    @BindView(R.id.fragment_profile_posts) RecyclerView posts;
    @BindView(R.id.fragment_profile_flow_segment) SegmentedGroup segment;
    @BindView(R.id.fragment_profile_text_no_record) TextView text_no_record;


    @Inject ProfilePresenter presenter;

    private FlexibleAdapter<CardMenuField> menuAdapter;
    private FlexibleModelAdapter<PostInfo> postAdapter;
    private LoadPostsPackage loadPostsPackage = new LoadPostsPackage();

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View view) {
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
            loadPostsPackage.setPage(0);
            loadPostsPackage.setSubs(checkedId != R.id.fragment_profile_posts_my);
            refreshItems();
        });
//        postRefresh.setOnRefreshListener(() -> {
//            loadPostsPackage.increasePage();
//            presenter.onLoadPosts(loadPostsPackage);
//        });
        postAdapter = new FlexibleModelAdapter<>(new ArrayList<>(), this::processAction);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        /*scrollListener = new EndlessRecyclerOnScrollListener(manager) {
            @Override
            public void onLoadMore(int currentPage) {
                loadPostsPackage.setPage(currentPage - 1);
                presenter.onLoadPosts(loadPostsPackage);
            }
        };*/
        posts.setLayoutManager(manager);
        posts.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        posts.setAdapter(postAdapter);
    }

    private void processAction(int action, Object payload) {
        switch (action) {
            case Actions.ACTION_LIKE_POST:
                presenter.onLikePost(((Post) payload));
                break;
        }
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
        getActivity().setTitle(profile.getUser().getFormattedName());
        Picasso.with(getActivity()).load(profile.getUser().getThumbnail()).fit().into(avatar);
        username.setText(profile.getUser().getFormattedName());
        status.setText(getString(R.string.online));
        friendsCounter.setCount(profile.getUser().getCounts().getFriends());
        photosCounter.setCount(profile.getUser().getCounts().getPhotos());
        subscribesCounter.setCount(profile.getUser().getSubscriptionsCount());
        subscribersCounter.setCount(profile.getUser().getCounts().getSubscribers());
    }

    @Override
    public void appendPosts(Posts posts) {
        if(loadPostsPackage.getPage() == 0) postAdapter.clear();

        //if(posts.getModels().isEmpty()) loadPostsPackage.decreasePage();

        postAdapter.addItems(menuAdapter.getItemCount(), posts.getModels());

        text_no_record.setVisibility(postAdapter.getItemCount()==0?View.VISIBLE:View.GONE);
    }

    @Override
    public boolean onItemClick(int position) {
        switch (position) {
            case 0:
                startActivityForResult(new Intent(getActivity(), NewPostActivity.class),Cons.REQUEST_CODE_REFRESH_ITEMS);
                break;
            case 1:
                startActivity(new Intent(getActivity(), AlbumsActivity.class));
                break;
            case 2:
                startActivity(new Intent(getActivity(), FavoriteBeerActivity.class));
                break;
            case 3:
                startActivity(new Intent(getActivity(), FavoriteRestoActivity.class));
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
        presenter.onLoadPosts(loadPostsPackage);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case Cons.REQUEST_CODE_REFRESH_ITEMS:
                if(resultCode==RESULT_OK) {
                    refreshItems();
                    return;
                }
                default:
                    super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
