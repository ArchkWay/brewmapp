package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.entity.container.Subscriptions;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.data.pojo.ProfileInfoPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.execution.task.LoadPostsTask;
import com.brewmapp.execution.task.LoadProfileAndPostsTask;
import com.brewmapp.execution.task.LoadProfileTask;
import com.brewmapp.execution.task.LoadSubscriptionsItemsTask;
import com.brewmapp.presentation.presenter.contract.ProfilePresenter;
import com.brewmapp.presentation.view.contract.ProfileView;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfilePresenterImpl extends BasePresenter<ProfileView> implements ProfilePresenter {


    private UserRepo userRepo;
    private LoadPostsTask loadPostsTask;
    private LoadSubscriptionsItemsTask loadSubscriptionsItemsTask;
    private LoadProfileTask loadProfileTask;
    private LoadProfileAndPostsTask loadProfilePostsTask;
    private LikeTask likeTask;

    @Inject
    public ProfilePresenterImpl(UserRepo userRepo, LoadPostsTask loadPostsTask,
                                LoadProfileTask loadProfileTask,
                                LoadProfileAndPostsTask loadProfilePostsTask,
                                LikeTask likeTask,
                                LoadSubscriptionsItemsTask loadSubscriptionsItemsTask) {
        this.userRepo = userRepo;
        this.loadPostsTask = loadPostsTask;
        this.loadProfileTask = loadProfileTask;
        this.loadProfilePostsTask = loadProfilePostsTask;
        this.likeTask = likeTask;
        this.loadSubscriptionsItemsTask = loadSubscriptionsItemsTask;
    }

    @Override
    public void onAttach(ProfileView profileView) {
        super.onAttach(profileView);
        showUserProfile();
        onLoadEverything();
    }

    private void showUserProfile() {
        if(userRepo.load().getCounts() != null) {
            view.showUserProfile(new UserProfile(userRepo.load()));
        }
    }

    @Override
    public void onDestroy() {
        loadPostsTask.cancel();
        loadProfileTask.cancel();
        loadProfilePostsTask.cancel();
        likeTask.cancel();
    }

    @Override
    public void onLoadEverything() {
        enableControls(false);
        loadProfilePostsTask.execute(null, new SimpleSubscriber<ProfileInfoPackage>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
                view.onError();
            }

            @Override
            public void onNext(ProfileInfoPackage pack) {
                enableControls(true);
                userRepo.save(pack.getUserProfile().getUser());
                showUserProfile();
            }
        });
    }

    @Override
    public void onLoadPosts(LoadPostsPackage loadPostsPackage) {
        loadPostsTask.execute(loadPostsPackage, new SimpleSubscriber<Posts>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);showMessage(e.getMessage());view.onError();
            }
            @Override
            public void onNext(Posts posts) {
                enableControls(true);view.appendPosts(posts);
            }
        });
    }
    @Override
    public void onLoadSubscription(LoadPostsPackage loadPostsPackage) {
        loadSubscriptionsItemsTask.execute(0,new SimpleSubscriber<Subscriptions>(){
            @Override
            public void onNext(Subscriptions subscriptions) {
                super.onNext(subscriptions);
                enableControls(true);view.appendSubscriptions(subscriptions);
                User user=userRepo.load();
                user.setSubscriptionsCount(subscriptions.getModels().size());
                userRepo.save(user);
                showUserProfile();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
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
