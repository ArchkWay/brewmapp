package com.brewmapp.presentation.view.contract;

import java.util.List;

import com.brewmapp.data.entity.MenuField;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import ru.frosteye.ovsa.presentation.navigation.NavigatorView;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 02.08.2017.
 */

public interface MainView extends BasicView, NavigatorView {

    void showFragment(BaseFragment fragment);
    void showDrawer(boolean shown);
    void successCheckEnvironment(User load, List<MenuField> aDefault);
    void commonError(String... strings);

}
