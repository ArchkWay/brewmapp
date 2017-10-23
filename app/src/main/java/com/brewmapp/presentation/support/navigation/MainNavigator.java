package com.brewmapp.presentation.support.navigation;

import javax.inject.Inject;

import com.brewmapp.app.di.scope.PresenterScope;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.MenuField;
import com.brewmapp.presentation.view.contract.MainView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.brewmapp.presentation.view.impl.fragment.BeerMapFragment;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.brewmapp.presentation.view.impl.fragment.FriendsFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragment;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.presentation.view.impl.fragment.SettingsFragment;

import ru.frosteye.ovsa.presentation.navigation.Navigator;
import ru.frosteye.ovsa.presentation.navigation.impl.BaseNavigatorImpl;
import ru.frosteye.ovsa.presentation.navigation.impl.SimpleNavAction;

/**
 * Created by ovcst on 03.08.2017.
 */

@PresenterScope
public class MainNavigator extends BaseNavigatorImpl<MainView> implements Navigator<MainView> {

    private BaseFragment fragmentToShow;
    private UiSettingRepo uiSettingRepo;

    @Inject
    public MainNavigator(UiSettingRepo uiSettingRepo) {
        this.uiSettingRepo = uiSettingRepo;
    }

    @Override
    public void onNavigatorAction(Action action) {
        uiSettingRepo.setActiveFragment(action.code());
        switch (action.code()) {
            case MenuField.PROFILE:
                fragmentToShow = new ProfileFragment();
                break;
            case MenuField.EVENTS:
                fragmentToShow = new EventsFragment();
                break;
            case MenuField.SEARCH:
                fragmentToShow = new SearchFragment();
                break;
            case MenuField.MAP:
                fragmentToShow = new BeerMapFragment();
                break;
            case MenuField.SETTINGS:
                fragmentToShow = new SettingsFragment();
                break;
            default:
                fragmentToShow = new FriendsFragment();
        }

        fragmentToShow.setNavigator(this);
        getView().showDrawer(false);

    }

    public void onMenuItemSelected(MenuField menuField) {
        onNavigatorAction(new SimpleNavAction(menuField.getId()));
    }

    public void onDrawerClosed() {
        if(fragmentToShow != null) {
            getView().showFragment(fragmentToShow);
            fragmentToShow = null;
        }
    }

}
