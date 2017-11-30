package com.brewmapp.presentation.view.impl.fragment;

import android.content.Context;
import android.support.annotation.MenuRes;
import android.view.View;

import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;

import butterknife.ButterKnife;

import com.brewmapp.presentation.support.navigation.FragmentInterractor;
import com.brewmapp.presentation.view.contract.MainView;

import java.util.List;

import ru.frosteye.ovsa.presentation.navigation.ActionBarItemDelegate;
import ru.frosteye.ovsa.presentation.view.fragment.NavigatorFragment;

public abstract class BaseFragment extends NavigatorFragment<MainView> implements ActionBarItemDelegate {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
        inject(component);
    }

    @Override
    protected void prepareView(View view) {
        ButterKnife.bind(this, view);
        getActivity().setTitle(getTitle());
        if(interractor()!=null)   view.post(() -> interractor().processSmoothShow(true));

    }

    public String createTag() {
        return getClass().getSimpleName();
    }

    public abstract  @MenuRes int getMenuToInflate();

    protected abstract void inject(PresenterComponent component);

    public abstract CharSequence getTitle();

    public List<String> getTitleDropDown() {
        return null;
    }

    protected FragmentInterractor interractor() {
        if(getActivity() != null && (getActivity() instanceof FragmentInterractor)) {
            return ((FragmentInterractor) getActivity());
        }
        return null;
    }

    @Override
    public void onBarAction(int id) {
        super.onBarAction(id);
    }



}
