package com.brewmapp.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.pojo.LoadPostsPackage;
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
import ru.frosteye.ovsa.presentation.view.widget.FixedAppBarLayout;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;
import ru.frosteye.ovsa.presentation.view.widget.swipe.SwipeRefreshLayoutBottom;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfileFragment extends BaseFragment implements ProfileView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.fragment_profile_avatar) ImageView avatar;
    @BindView(R.id.fragment_profile_city) TextView city;
    @BindView(R.id.fragment_profile_app_bar) FixedAppBarLayout appBar;
    @BindView(R.id.fragment_profile_counter_friends) InfoCounter friendsCounter;
    @BindView(R.id.fragment_profile_counter_photos) InfoCounter photosCounter;
    @BindView(R.id.fragment_profile_counter_subscribers) InfoCounter subscribersCounter;
    @BindView(R.id.fragment_profile_counter_albums) InfoCounter albumsCounter;
    @BindView(R.id.fragment_profile_status) TextView status;
    @BindView(R.id.fragment_profile_post_refresh) SwipeRefreshLayoutBottom postRefresh;
    @BindView(R.id.fragment_profile_username) TextView username;
    @BindView(R.id.fragment_profile_menu) RecyclerView menu;
    @BindView(R.id.fragment_profile_posts) RecyclerView posts;
    @BindView(R.id.fragment_profile_flow_segment) SegmentedGroup segment;

    @Inject ProfilePresenter presenter;

    private FlexibleAdapter<CardMenuField> menuAdapter;
    private FlexibleModelAdapter<PostInfo> postAdapter;
    private EndlessRecyclerOnScrollListener scrollListener;
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
            presenter.onLoadPosts(loadPostsPackage);
        });
       /* swipe.setOnRefreshListener(() -> {
            segment.check(R.id.fragment_profile_posts_my);
            loadPostsPackage.setPage(0);
            presenter.onLoadEverything();
        });
*/
        postRefresh.setOnRefreshListener(() -> {
            loadPostsPackage.increasePage();
            presenter.onLoadPosts(loadPostsPackage);
        });
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
        appBar.setOnStateChangeListener(toolbarChange -> {
            /*if(toolbarChange == FixedAppBarLayout.State.COLLAPSED) {
                posts.addOnScrollListener(scrollListener);
            } else {
                posts.removeOnScrollListener(scrollListener);
            }*/
            Log.i("OV", toolbarChange.toString());
        });
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
//        swipe.setRefreshing(!enabled);


    }

    @Override
    public void showUserProfile(UserProfile profile) {
        getActivity().setTitle(profile.getUser().getFormattedName());
        Picasso.with(getActivity()).load(profile.getUser().getThumbnail()).fit().into(avatar);
        username.setText(profile.getUser().getFormattedName());
        status.setText(getString(R.string.online));
        friendsCounter.setCount(profile.getUser().getCounts().getFriends());
        photosCounter.setCount(profile.getUser().getCounts().getPhotos());
        albumsCounter.setCount(profile.getUser().getCounts().getAlbums());
        subscribersCounter.setCount(profile.getUser().getCounts().getSubscribers());
    }

    @Override
    public void appendPosts(Posts posts) {
        if(loadPostsPackage.getPage() == 0) postAdapter.clear();
        if(posts.getModels().isEmpty()) loadPostsPackage.decreasePage();
        postRefresh.setRefreshing(false);
        postAdapter.addItems(menuAdapter.getItemCount(), posts.getModels());
        if(postAdapter.getItemCount() == posts.getTotal()) postRefresh.setEnabled(false);
    }

    @Override
    public boolean onItemClick(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(getActivity(), NewPostActivity.class));
                break;
            case 1:
                startActivity(new Intent(getActivity(), AlbumsActivity.class));
                break;
        }
        return false;
    }

    @Override
    public void refreshState() {
        postAdapter.notifyDataSetChanged();
    }
}
