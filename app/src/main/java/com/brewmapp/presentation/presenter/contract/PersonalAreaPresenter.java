package com.brewmapp.presentation.presenter.contract;

import com.brewmapp.data.entity.Post;
import com.brewmapp.data.pojo.LoadPostsPackage;
import com.brewmapp.presentation.view.contract.PersonalAreaView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public interface PersonalAreaPresenter extends LivePresenter<PersonalAreaView> {


    void onLoadEverything();
    void onLoadPosts(LoadPostsPackage loadPostsPackage);
    void onLikePost(Post post);
}
