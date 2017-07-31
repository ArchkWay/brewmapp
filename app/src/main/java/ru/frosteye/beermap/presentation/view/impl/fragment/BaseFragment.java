package ru.frosteye.beermap.presentation.view.impl.fragment;

import android.content.Context;
import android.view.View;

import ru.frosteye.beermap.app.di.component.PresenterComponent;
import ru.frosteye.beermap.app.di.module.PresenterModule;
import ru.frosteye.beermap.app.environment.BeerMap;

import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.fragment.PresenterFragment;

public abstract class BaseFragment extends PresenterFragment {

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
    }

    public String createTag() {
        return getClass().getSimpleName();
    }

    ;

    protected abstract void inject(PresenterComponent component);

    public abstract CharSequence getTitle();


}
