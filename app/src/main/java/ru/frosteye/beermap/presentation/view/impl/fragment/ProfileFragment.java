package ru.frosteye.beermap.presentation.view.impl.fragment;

import android.view.View;

import javax.inject.Inject;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.presentation.presenter.contract.ProfilePresenter;
import ru.frosteye.beermap.presentation.view.contract.MainView;
import ru.frosteye.beermap.presentation.view.contract.ProfileView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.navigation.Navigator;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by ovcst on 03.08.2017.
 */

public class ProfileFragment extends BaseFragment implements ProfileView {

    @Inject ProfilePresenter presenter;


    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return R.menu.profile;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return ResourceHelper.getString(R.string.my_profile);
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }
}
