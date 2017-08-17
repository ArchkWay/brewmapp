package ru.frosteye.beermap.presentation.view.contract;

import ru.frosteye.beermap.data.entity.UserProfile;
import ru.frosteye.beermap.data.entity.container.Posts;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 03.08.2017.
 */

public interface ProfileView extends BasicView {
    void showUserProfile(UserProfile profile);
    void appendPosts(Posts posts, boolean clear);
}
