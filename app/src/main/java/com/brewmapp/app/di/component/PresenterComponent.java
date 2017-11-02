package com.brewmapp.app.di.component;

import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.di.scope.PresenterScope;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.presentation.view.impl.activity.AddAlbumActivity;
import com.brewmapp.presentation.view.impl.activity.AddInterestActivity;
import com.brewmapp.presentation.view.impl.activity.AlbumActivity;
import com.brewmapp.presentation.view.impl.activity.AlbumsActivity;
import com.brewmapp.presentation.view.impl.activity.AssessmentsActivity;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;
import com.brewmapp.presentation.view.impl.activity.ConfirmCodeActivity;
import com.brewmapp.presentation.view.impl.activity.EnterPasswordActivity;
import com.brewmapp.presentation.view.impl.activity.EnterPhoneActivity;
import com.brewmapp.presentation.view.impl.activity.EventDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.ExtendedSearchActivity;
import com.brewmapp.presentation.view.impl.activity.FilterByCategory;
import com.brewmapp.presentation.view.impl.activity.FilterMapActivity;
import com.brewmapp.presentation.view.impl.activity.InterestListActivity;
import com.brewmapp.presentation.view.impl.activity.FriendsActivity;
import com.brewmapp.presentation.view.impl.activity.InviteActivity;
import com.brewmapp.presentation.view.impl.activity.InviteListActivity;
import com.brewmapp.presentation.view.impl.activity.LoginActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.PostDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.activity.SaleDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.UniversalMapActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.activity.NewPostSettingsActivity;
import com.brewmapp.presentation.view.impl.activity.PhotoSliderActivity;
import com.brewmapp.presentation.view.impl.activity.PickLocationActivity;
import com.brewmapp.presentation.view.impl.activity.RegisterActivity;
import com.brewmapp.presentation.view.impl.activity.SearchActivity;
import com.brewmapp.presentation.view.impl.activity.StartActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogShare;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import dagger.Subcomponent;

import com.brewmapp.presentation.view.impl.fragment.BeerMapFragment;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;
import com.brewmapp.presentation.view.impl.fragment.FriendsFragment;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragment;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;
import com.brewmapp.presentation.view.impl.fragment.SettingsFragment;
import com.brewmapp.presentation.view.impl.widget.ShareLikeView;

@PresenterScope
@Subcomponent(modules = PresenterModule.class)
public interface PresenterComponent {
    void inject(BaseFragment baseFragment);
    void inject(ProfileFragment fragment);
    void inject(FriendsFragment fragment);
    void inject(EventsFragment fragment);
    void inject(SearchFragment fragment);
    void inject(BeerMapFragment fragment);
    void inject(SettingsFragment fragment);

    void inject(BaseActivity activity);
    void inject(StartActivity activity);
    void inject(EnterPasswordActivity activity);
    void inject(LoginActivity activity);
    void inject(RegisterActivity activity);
    void inject(EnterPhoneActivity activity);
    void inject(MainActivity activity);
    void inject(ConfirmCodeActivity activity);
    void inject(InviteActivity activity);
    void inject(AlbumsActivity activity);
    void inject(AddAlbumActivity activity);
    void inject(AlbumActivity activity);
    void inject(InviteListActivity activity);
    void inject(NewPostActivity activity);
    void inject(PickLocationActivity activity);
    void inject(FriendsActivity activity);
    void inject(NewPostSettingsActivity activity);
    void inject(SearchActivity activity);
    void inject(ExtendedSearchActivity activity);
    void inject(EventDetailsActivity activity);
    void inject(PhotoSliderActivity activity);
    void inject(UniversalMapActivity activity);
    void inject(SaleDetailsActivity activity);
    void inject(PostDetailsActivity activity);
    void inject(DialogShare dialogShare);
    void inject(ShareLikeView shareLikeView);
    void inject(AssessmentsActivity assessmentsActivity);
    void inject(InterestListActivity interestListActivity);
    void inject(AddInterestActivity addInterestActivity);
    void inject(RestoDetailActivity restoDetailActivity);
    void inject(BeerDetailActivity beerDetailActivity);
    void inject(FilterByCategory filterByCategory);
    void inject(FilterMapActivity filterMapActivity);
}
