package ru.frosteye.beermap.presentation.view.impl.fragment;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import info.hoang8f.android.segmented.SegmentedGroup;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.data.entity.ProfileMenuField;
import ru.frosteye.beermap.data.entity.UserProfile;
import ru.frosteye.beermap.data.entity.container.Posts;
import ru.frosteye.beermap.data.entity.wrapper.PostInfo;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.presentation.presenter.contract.ProfilePresenter;
import ru.frosteye.beermap.presentation.view.contract.MainView;
import ru.frosteye.beermap.presentation.view.contract.ProfileView;
import ru.frosteye.beermap.presentation.view.impl.activity.AlbumsActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.FriendsActivity;
import ru.frosteye.beermap.presentation.view.impl.activity.NewPostActivity;
import ru.frosteye.beermap.presentation.view.impl.widget.ProfileCounter;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.adapter.FlexibleModelAdapter;
import ru.frosteye.ovsa.presentation.navigation.Navigator;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.ListDivider;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfileFragment extends BaseFragment implements ProfileView, FlexibleAdapter.OnItemClickListener {

    @BindView(R.id.fragment_profile_avatar) ImageView avatar;
    @BindView(R.id.fragment_profile_city) TextView city;
    @BindView(R.id.fragment_profile_counter_friends) ProfileCounter friendsCounter;
    @BindView(R.id.fragment_profile_counter_photos) ProfileCounter photosCounter;
    @BindView(R.id.fragment_profile_counter_subscribers) ProfileCounter subscribersCounter;
    @BindView(R.id.fragment_profile_counter_albums) ProfileCounter albumsCounter;
    @BindView(R.id.fragment_profile_status) TextView status;
    @BindView(R.id.fragment_profile_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.fragment_profile_username) TextView username;
    @BindView(R.id.fragment_profile_menu) RecyclerView menu;
    @BindView(R.id.fragment_profile_posts) RecyclerView posts;
    @BindView(R.id.fragment_profile_flow_segment) SegmentedGroup segment;

    @Inject ProfilePresenter presenter;

    private FlexibleAdapter<ProfileMenuField> menuAdapter;
    private FlexibleModelAdapter<PostInfo> postAdapter;
    private int page = 0;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View view) {
        menuAdapter = new FlexibleAdapter<>(ProfileMenuField.createDefault(getActivity()), this);
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
            page = 0;
            presenter.onLoadPosts(checkedId == R.id.fragment_profile_posts_my ? 0 : 1, page);
        });
        swipe.setOnRefreshListener(() -> {
            segment.check(R.id.fragment_profile_posts_my);
            page = 0;
            presenter.onLoadEverything();
        });
        postAdapter = new FlexibleModelAdapter<>(new ArrayList<>(), (code, payload) -> {

        });
        posts.addItemDecoration(new ListDivider(getActivity(), ListDivider.VERTICAL_LIST));
        posts.setLayoutManager(new LinearLayoutManager(getActivity()));
        posts.setAdapter(postAdapter);
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
        swipe.setRefreshing(!enabled);


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
    public void appendPosts(Posts posts, boolean clear) {
        if(clear) postAdapter.clear();
        postAdapter.updateDataSet(posts.getModels());
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
}
