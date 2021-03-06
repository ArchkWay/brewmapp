package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.data.pojo.ReviewPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.execution.task.LoadPostsTask;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.execution.task.LoadReviewsTask;
import com.brewmapp.execution.task.LoadSubscriptionsItemsTask;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentMain_presenter;
import com.brewmapp.presentation.view.contract.ProfileFragmentMain_view;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfileFragmentMain_presenter_Impl extends BasePresenter<ProfileFragmentMain_view> implements ProfileFragmentMain_presenter {

    private UserRepo userRepo;
    private LoadPostsTask loadPostsTask;
    private LoadSubscriptionsItemsTask loadSubscriptionsItemsTask;
    private LoadProfileTask loadProfileTask;
    private LikeTask likeTask;
    private UiSettingRepo uiSettingRepo;
    private Context context;
    private LoadReviewsTask loadReviewsTask;

    @Inject
    public ProfileFragmentMain_presenter_Impl(UserRepo userRepo, LoadPostsTask loadPostsTask,
                                              LoadProfileTask loadProfileTask,
                                              LikeTask likeTask,
                                              LoadSubscriptionsItemsTask loadSubscriptionsItemsTask,
                                              UiSettingRepo uiSettingRepo,
                                              Context context,
                                              LoadReviewsTask loadReviewsTask) {
        this.userRepo = userRepo;
        this.loadPostsTask = loadPostsTask;
        this.loadProfileTask = loadProfileTask;
        this.likeTask = likeTask;
        this.loadSubscriptionsItemsTask = loadSubscriptionsItemsTask;
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
        this.loadReviewsTask = loadReviewsTask;
    }

    @Override
    public void onAttach(ProfileFragmentMain_view profileView) {
        super.onAttach(profileView);
        refreshProfile();
    }

    @Override
    public void refreshProfile() {
        if(userRepo.load().getCounts() != null) {
            view.setContent(new UserProfile(userRepo.load()));
        }
    }

    @Override
    public void onDestroy() {
        loadPostsTask.cancel();
        loadProfileTask.cancel();
        likeTask.cancel();
        loadSubscriptionsItemsTask.cancel();
    }

    @Override
    public void onLoadPosts(LoadPostsPackage loadPostsPackage) {
        if(uiSettingRepo.isOnLine()) {
            ReviewPackage reviewPackage = new ReviewPackage();
            reviewPackage.setUser_id(String.valueOf(userRepo.load().getId()));
            loadReviewsTask.execute(reviewPackage, new SimpleSubscriber<List<IFlexible>>() {
                @Override
                public void onNext(List<IFlexible> iFlexibles) {
                    super.onNext(iFlexibles);
                    view.appendPosts(iFlexibles);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                }
            });
//            loadPostsTask.execute(loadPostsPackage, new SimpleSubscriber<Posts>() {
//                @Override
//                public void onNext(Posts posts) {
//                    view.appendPosts(posts);
//                }
//                @Override
//                public void onError(Throwable e) {
//                    showMessage(e.getMessage());
//                    view.onError();
//                }
//            });
        }else {
            view.onError();
        }
    }

    @Override
    public void onLoadSubscription(LoadPostsPackage loadPostsPackage) {
        if(uiSettingRepo.isOnLine()) {
            loadSubscriptionsItemsTask.execute(0, new SimpleSubscriber<Subscriptions>() {
                @Override
                public void onNext(Subscriptions subscriptions) {
                    super.onNext(subscriptions);
                    view.appendSubscriptions(subscriptions);
                    User user = userRepo.load();
                    user.setSubscriptionsCount(subscriptions.getModels().size());
                    userRepo.save(user);
                    refreshProfile();
                }

                @Override
                public void onError(Throwable e) {
                    view.onError();
                    super.onError(e);
                }
            });
        }else {
            view.onError();
        }
    }

    @Override
    public void onLikePost(Post post) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_NEWS, post.getId());
        likeTask.execute(likeDislikePackage, new SimpleSubscriber<MessageResponse>() {
            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }

            @Override
            public void onNext(MessageResponse messageResponse) {
                post.increaseLikes();
                view.refreshState();
            }
        });
    }
}
