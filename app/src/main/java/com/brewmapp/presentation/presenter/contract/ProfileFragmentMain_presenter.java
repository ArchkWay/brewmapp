package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Post;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.presentation.view.contract.ProfileFragmentMain_view;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public interface ProfileFragmentMain_presenter extends LivePresenter<ProfileFragmentMain_view> {
    void onLoadPosts(LoadPostsPackage loadPostsPackage);
    void onLikePost(Post post);
    void onLoadSubscription(LoadPostsPackage loadPostsPackage);
    void refreshProfile();
}
