package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.UserProfile;
import com.brewmapp.data.entity.container.Posts;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.data.pojo.ProfileUpdatePackage;
import com.brewmapp.execution.task.LoadPostsTask;
import com.brewmapp.execution.task.LoadProfileAndPostsTask;
import com.brewmapp.execution.task.LoadProfileTask;
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
    private LoadProfileTask loadProfileTask;
    private LoadProfileAndPostsTask loadProfilePostsTask;

    @Inject
    public ProfilePresenterImpl(UserRepo userRepo, LoadPostsTask loadPostsTask,
                                LoadProfileTask loadProfileTask,
                                LoadProfileAndPostsTask loadProfilePostsTask) {
        this.userRepo = userRepo;
        this.loadPostsTask = loadPostsTask;
        this.loadProfileTask = loadProfileTask;
        this.loadProfilePostsTask = loadProfilePostsTask;
    }

    @Override
    public void onAttach(ProfileView profileView) {
        super.onAttach(profileView);
        if(userRepo.load().getCounts() != null) {
            view.showUserProfile(new UserProfile(userRepo.load()));
        }
        onLoadEverything();
    }

    @Override
    public void onDestroy() {
        loadPostsTask.cancel();
        loadProfileTask.cancel();
        loadProfilePostsTask.cancel();
    }

    @Override
    public void onLoadEverything() {
        enableControls(false);
        loadProfilePostsTask.execute(null, new SimpleSubscriber<ProfileUpdatePackage>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(ProfileUpdatePackage pack) {
                enableControls(true);
                view.showUserProfile(pack.getUserProfile());
            }
        });
    }

    @Override
    public void onLoadPosts(LoadPostsPackage loadPostsPackage) {

        loadPostsTask.execute(loadPostsPackage, new SimpleSubscriber<Posts>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(Posts posts) {
                enableControls(true);
                view.appendPosts(posts);
            }
        });
    }
}
