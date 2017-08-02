package ru.frosteye.beermap.presentation.view.contract;

import java.util.List;

import ru.frosteye.beermap.data.entity.MenuField;
import ru.frosteye.beermap.data.entity.User;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by ovcst on 02.08.2017.
 */

public interface MainView extends BasicView {
    void showUser(User user);
    void showMenuItems(List<MenuField> fields);
}
