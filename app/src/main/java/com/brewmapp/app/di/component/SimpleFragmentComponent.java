package com.brewmapp.app.di.component;

import com.brewmapp.app.di.module.SimpleFragmentModule;
import com.brewmapp.app.di.scope.SimpleFragmentScope;
import com.brewmapp.presentation.view.impl.fragment.Simple.CreateBeerFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.CreateRestoFragment;

import dagger.Subcomponent;

@SimpleFragmentScope
@Subcomponent(modules = SimpleFragmentModule.class)
public interface SimpleFragmentComponent {
    void inject(CreateRestoFragment createRestoFragment);
    void inject(CreateBeerFragment createBeerFragment);
}
