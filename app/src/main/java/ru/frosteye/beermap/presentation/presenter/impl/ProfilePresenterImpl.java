package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.data.entity.UserProfile;
import ru.frosteye.beermap.data.entity.container.Posts;
import ru.frosteye.beermap.execution.exchange.request.base.Keys;
import ru.frosteye.beermap.execution.exchange.request.base.WrapperParams;
import ru.frosteye.beermap.execution.exchange.request.base.Wrappers;
import ru.frosteye.beermap.execution.task.LoadPostsTask;
import ru.frosteye.beermap.execution.task.LoadProfileTask;
import ru.frosteye.beermap.presentation.presenter.contract.ProfilePresenter;
import ru.frosteye.beermap.presentation.view.contract.ProfileView;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfilePresenterImpl extends BasePresenter<ProfileView> implements ProfilePresenter {


    private UserRepo userRepo;
    private LoadPostsTask loadPostsTask;
    private LoadProfileTask loadProfileTask;

    @Inject
    public ProfilePresenterImpl(UserRepo userRepo, LoadPostsTask loadPostsTask,
                                LoadProfileTask loadProfileTask) {
        this.userRepo = userRepo;
        this.loadPostsTask = loadPostsTask;
        this.loadProfileTask = loadProfileTask;
    }

    @Override
    public void onAttach(ProfileView profileView) {
        super.onAttach(profileView);
        if(userRepo.load().getCounts() == null) {
            onLoadEverything();
        } else {
            view.showUserProfile(new UserProfile(userRepo.load()));
            onLoadPosts(0, 0);
        }
    }

    @Override
    public void onDestroy() {
        loadPostsTask.cancel();
        loadProfileTask.cancel();
    }

    @Override
    public void onLoadEverything() {
        enableControls(false);
        loadProfileTask.execute(null, new SimpleSubscriber<UserProfile>() {
            @Override
            public void onError(Throwable e) {
                view.enableControls(true, CODE_LOAD_PROFILE);
            }

            @Override
            public void onNext(UserProfile profile) {

                view.enableControls(true, CODE_LOAD_PROFILE);
                view.showUserProfile(profile);
            }
        });
        WrapperParams params = new WrapperParams(Wrappers.NEWS);
        params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
        params.addParam(Keys.RELATED_ID, userRepo.load().getId());
        params.addParam(Keys.USER_SUBSCRIPTION, userRepo.load().getId());
        loadPostsTask.execute(params, new SimpleSubscriber<Posts>() {
            @Override
            public void onError(Throwable e) {
                view.enableControls(true, CODE_LOAD_POSTS);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(Posts posts) {
                view.enableControls(true, CODE_LOAD_POSTS);
                view.appendPosts(posts, true);
            }
        });
    }

    @Override
    public void onLoadPosts(int position, int page) {
        view.enableControls(false, CODE_LOAD_POSTS);
        WrapperParams params = new WrapperParams(Wrappers.NEWS);
        params.addParam(Keys.RELATED_MODEL, Keys.CAP_USER);
        params.addParam(Keys.RELATED_ID, userRepo.load().getId());
        if(position == 0) {
            params.addParam(Keys.USER_SUBSCRIPTION, userRepo.load().getId());
        }
        loadPostsTask.execute(params, new SimpleSubscriber<Posts>() {
            @Override
            public void onError(Throwable e) {
                view.enableControls(true, CODE_LOAD_POSTS);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(Posts posts) {
                view.enableControls(true, CODE_LOAD_POSTS);
                view.appendPosts(posts, page == 0);
            }
        });
    }
}
