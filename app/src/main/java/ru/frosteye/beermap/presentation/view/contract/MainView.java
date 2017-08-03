package ru.frosteye.beermap.presentation.view.contract;

import java.util.List;

import ru.frosteye.beermap.data.entity.MenuField;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.beermap.presentation.view.impl.fragment.BaseFragment;
import ru.frosteye.ovsa.presentation.navigation.NavigatorView;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 02.08.2017.
 */

public interface MainView extends BasicView, NavigatorView {
    void showUser(User user);
    void showMenuItems(List<MenuField> fields);
    void showFragment(BaseFragment fragment);
    void showDrawer(boolean shown);
}
