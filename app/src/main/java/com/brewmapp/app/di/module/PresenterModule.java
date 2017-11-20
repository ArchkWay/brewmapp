package com.brewmapp.app.di.module;

import android.view.View;

import com.brewmapp.app.di.scope.PresenterScope;
import com.brewmapp.presentation.presenter.contract.AddAlbumPresenter;
import com.brewmapp.presentation.presenter.contract.AddInterestPresenter;
import com.brewmapp.presentation.presenter.contract.AddReviewBeerPresenter;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.presenter.contract.AlbumPresenter;
import com.brewmapp.presentation.presenter.contract.AlbumsPresenter;
import com.brewmapp.presentation.presenter.contract.AssessmentsPresenter;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.presenter.contract.BeerMapPresenter;
import com.brewmapp.presentation.presenter.contract.ConfirmPhonePresenter;
import com.brewmapp.presentation.presenter.contract.EnterPasswordPresenter;
import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.presenter.contract.ExtendedSearchPresenter;
import com.brewmapp.presentation.presenter.contract.FilterByCategoryPresenter;
import com.brewmapp.presentation.presenter.contract.InterestListPresenter;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.presenter.contract.InviteListPresenter;
import com.brewmapp.presentation.presenter.contract.InvitePresenter;
import com.brewmapp.presentation.presenter.contract.LoginPresenter;
import com.brewmapp.presentation.presenter.contract.MainPresenter;
import com.brewmapp.presentation.presenter.contract.MapPresenter;
import com.brewmapp.presentation.presenter.contract.NewPostPresenter;
import com.brewmapp.presentation.presenter.contract.NewPostSettingsPresenter;
import com.brewmapp.presentation.presenter.contract.PhotoSliderPresenter;
import com.brewmapp.presentation.presenter.contract.PickLocationPresenter;
import com.brewmapp.presentation.presenter.contract.PostDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.ProfileEditPresenter;
import com.brewmapp.presentation.presenter.contract.ProfilePresenter;
import com.brewmapp.presentation.presenter.contract.RegisterPresenter;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.SearchAllPresenter;
import com.brewmapp.presentation.presenter.contract.SearchPresenter;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.presenter.contract.ShareLikeViewPresenter;
import com.brewmapp.presentation.presenter.contract.StartPresenter;
import com.brewmapp.presentation.presenter.contract.UserProfilePresenter;
import com.brewmapp.presentation.presenter.impl.AddAlbumPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AddInterestPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AddReviewBeerPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AddReviewRestoPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AlbumPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AlbumsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AssessmentsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.BeerDetailPresenterImpl;
import com.brewmapp.presentation.presenter.impl.BeerMapPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ConfirmPhonePresenterImpl;
import com.brewmapp.presentation.presenter.impl.EnterPasswordPresenterImpl;
import com.brewmapp.presentation.presenter.impl.EventDetailsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.EventsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ExtendedSearchPresenterImpl;
import com.brewmapp.presentation.presenter.impl.FilterByCategoryPresenterImpl;
import com.brewmapp.presentation.presenter.impl.InterestListPresenterImpl;
import com.brewmapp.presentation.presenter.impl.FilerMapPresenterImpl;
import com.brewmapp.presentation.presenter.impl.FriendsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.InviteListPresenterImpl;
import com.brewmapp.presentation.presenter.impl.InvitePresenterImpl;
import com.brewmapp.presentation.presenter.impl.LoginPresenterImpl;
import com.brewmapp.presentation.presenter.impl.MainPresenterImpl;
import com.brewmapp.presentation.presenter.impl.MapPresenterImpl;
import com.brewmapp.presentation.presenter.impl.NewPostPresenterImpl;
import com.brewmapp.presentation.presenter.impl.NewPostSettingsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.PhotoSliderPresenterImpl;
import com.brewmapp.presentation.presenter.impl.PickLocationPresenterImpl;
import com.brewmapp.presentation.presenter.impl.PostDetailsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ProfileEditFragmentPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ProfileEditPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ProfilePresenterImpl;
import com.brewmapp.presentation.presenter.impl.RegisterPresenterImpl;
import com.brewmapp.presentation.presenter.impl.RestoDetailPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SaleDetailsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SearchAllPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SearchPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SettingsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ShareLikeViewPresenterImpl;
import com.brewmapp.presentation.presenter.impl.StartPresenterImpl;
import com.brewmapp.presentation.presenter.contract.ProfileEditFragmentPresenter;
import com.brewmapp.presentation.presenter.impl.UserProfilePresenterImpl;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import dagger.Module;
import dagger.Provides;
import ru.frosteye.ovsa.di.module.BasePresenterModule;

@Module
public class PresenterModule extends BasePresenterModule<BaseActivity, BaseFragment> {
    public PresenterModule(View view) {
        super(view);
    }

    public PresenterModule(BaseActivity activity) {
        super(activity);
    }

    public PresenterModule(BaseFragment fragment) {
        super(fragment);
    }

    @Provides @PresenterScope
    StartPresenter provideStartPresenter(StartPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    LoginPresenter provideLoginPresenter(LoginPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    RegisterPresenter provideRegisterPresenter(RegisterPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    ConfirmPhonePresenter provideConfirmPhonePresenter(ConfirmPhonePresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    MainPresenter provideMainPresenter(MainPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    ProfilePresenter provideProfilePresenter(ProfilePresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    FriendsPresenter provideFriendsPresenter(FriendsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    EnterPasswordPresenter provideEnterPasswordPresenter(EnterPasswordPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    InvitePresenter provideInvitePresenter(InvitePresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    AlbumsPresenter provideAlbumsPresenter(AlbumsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    AddAlbumPresenter provideAddAlbumPresenter(AddAlbumPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    AlbumPresenter provideAlbumPresenter(AlbumPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    InviteListPresenter provideInviteListPresenter(InviteListPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    NewPostPresenter provideNewPostPresenter(NewPostPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    PickLocationPresenter providePickLocationPresenter(PickLocationPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    NewPostSettingsPresenter provideNewPostSettingsPresenter(NewPostSettingsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    EventsPresenter provideEventsPresenter(EventsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    BeerMapPresenter provideBeerMapPresenter(BeerMapPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    SearchAllPresenter provideSearchAllPresenter(SearchAllPresenterImpl presenter) {
        return presenter;
    }
    @Provides @PresenterScope
    SettingsPresenter provideSettingsPresenter(SettingsPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    SearchPresenter provideSearchPresenter(SearchPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    ExtendedSearchPresenter provideExtendedSearchActivityPresenter(ExtendedSearchPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    EventDetailsPresenter provideEventDetailsPresenter(EventDetailsPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    PhotoSliderPresenter providePhotoSliderPresenter(PhotoSliderPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    MapPresenter provideMapPresenter(MapPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    SaleDetailsPresenter provideSaleDetailsPresenter(SaleDetailsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    PostDetailsPresenter providePostDetailsPresenter(PostDetailsPresenterImpl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    ShareLikeViewPresenter provideShareLikeViewPresenter(ShareLikeViewPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    AssessmentsPresenter provideAssessmentsPresenter(AssessmentsPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    InterestListPresenter provideFavoriteBeerPresenter(InterestListPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    AddInterestPresenter provideAddFavoriteBeerPresenter(AddInterestPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    RestoDetailPresenter provideRestoCardPresenter(RestoDetailPresenterImpl presenter){
        return presenter;
    }
    @Provides @PresenterScope
    FilterMapPresenter provideFilterMapViewPresenter(FilerMapPresenterImpl presenter){
        return presenter;
    }
    @Provides @PresenterScope
    BeerDetailPresenter provideBeerDetailPresenter(BeerDetailPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    AddReviewRestoPresenter provideAddReviewRestoPresenter(AddReviewRestoPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    ProfileEditPresenter provideProfileInfoPresenter(ProfileEditPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    FilterByCategoryPresenter provideFilterByCategory (FilterByCategoryPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    ProfileEditFragmentPresenter provideProfileEditFragmentPresenter(ProfileEditFragmentPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    AddReviewBeerPresenter provideAddReviewBeerPresenter(AddReviewBeerPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    UserProfilePresenter provideUserProfilePresenter(UserProfilePresenterImpl presenter){
        return presenter;
    }

}
