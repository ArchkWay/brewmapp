package com.brewmapp.app.di.component;

import com.brewmapp.app.di.module.SimpleFragmentModule;
import com.brewmapp.app.di.scope.SimpleFragmentScope;
import com.brewmapp.presentation.view.impl.fragment.Simple.AddBeerFragment;
import com.brewmapp.presentation.view.impl.fragment.Simple.AddRestoFragment;

import dagger.Subcomponent;

@SimpleFragmentScope
@Subcomponent(modules = SimpleFragmentModule.class)
public interface SimpleFragmentComponent {
    void inject(AddRestoFragment addRestoFragment);
    void inject(AddBeerFragment addBeerFragment);
}
