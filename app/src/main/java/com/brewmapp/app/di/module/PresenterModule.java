package com.brewmapp.app.di.module;

import android.app.Service;
import android.view.View;

import com.brewmapp.app.di.scope.PresenterScope;
import com.brewmapp.presentation.presenter.contract.AddAlbumPresenter;
import com.brewmapp.presentation.presenter.contract.BeerEditFragmentPresenter;
import com.brewmapp.presentation.presenter.contract.BreweryDetailsActivityPresenter;
import com.brewmapp.presentation.presenter.contract.ChatFragmentPresenter;
import com.brewmapp.presentation.presenter.contract.MessageFragmentPresenter;
import com.brewmapp.presentation.presenter.contract.MultiFragmentActivityPresenter;
import com.brewmapp.presentation.presenter.contract.MultiListPresenter;
import com.brewmapp.presentation.presenter.contract.AddReviewBeerPresenter;
import com.brewmapp.presentation.presenter.contract.AddReviewRestoPresenter;
import com.brewmapp.presentation.presenter.contract.AlbumPresenter;
import com.brewmapp.presentation.presenter.contract.AlbumsPresenter;
import com.brewmapp.presentation.presenter.contract.AssessmentsPresenter;
import com.brewmapp.presentation.presenter.contract.BeerDetailPresenter;
import com.brewmapp.presentation.presenter.contract.MapFragment_presenter;
import com.brewmapp.presentation.presenter.contract.ConfirmPhonePresenter;
import com.brewmapp.presentation.presenter.contract.EnterPasswordPresenter;
import com.brewmapp.presentation.presenter.contract.EventDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.presenter.contract.ExtendedSearchPresenter;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentFull_presenter;
import com.brewmapp.presentation.presenter.contract.SelectCategoryActivityPresenter;
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
import com.brewmapp.presentation.presenter.contract.ProfileActivity_presenter;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentMain_presenter;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentShot_presenter;
import com.brewmapp.presentation.presenter.contract.RegisterPresenter;
import com.brewmapp.presentation.presenter.contract.RestoDetailPresenter;
import com.brewmapp.presentation.presenter.contract.RestoEditFragmentPresenter;
import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.presenter.contract.ResultSearchActivityPresenter;
import com.brewmapp.presentation.presenter.contract.SettingsPresenter;
import com.brewmapp.presentation.presenter.contract.ShareLikeViewPresenter;
import com.brewmapp.presentation.presenter.contract.StartPresenter;
import com.brewmapp.presentation.presenter.impl.AddAlbumPresenterImpl;
import com.brewmapp.presentation.presenter.impl.BeerEditFragmentPresenterImpl;
import com.brewmapp.presentation.presenter.impl.BreweryDetailsActivityPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ChatFragmentPresenterImpl;
import com.brewmapp.presentation.presenter.impl.MessageFragmentPresenterImpl;
import com.brewmapp.presentation.presenter.impl.MultiFragmentActivityPresenterImpl;
import com.brewmapp.presentation.presenter.impl.MultiListPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AddReviewBeerPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AddReviewRestoPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AlbumPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AlbumsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.AssessmentsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.BeerDetailPresenterImpl;
import com.brewmapp.presentation.presenter.impl.MapFragment_presenter_Impl;
import com.brewmapp.presentation.presenter.impl.ConfirmPhonePresenterImpl;
import com.brewmapp.presentation.presenter.impl.EnterPasswordPresenterImpl;
import com.brewmapp.presentation.presenter.impl.EventDetailsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.EventsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ExtendedSearchPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ProfileFragmentFull_presenter_Impl;
import com.brewmapp.presentation.presenter.impl.SelectCategoryActivityPresenterImpl;
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
import com.brewmapp.presentation.presenter.impl.ProfileFragmentEdit_presenter_Impl;
import com.brewmapp.presentation.presenter.impl.ProfileActivity_presenter_Impl;
import com.brewmapp.presentation.presenter.impl.ProfileFragmentMain_presenter_Impl;
import com.brewmapp.presentation.presenter.impl.ProfileFragmentShot_presenter_Impl;
import com.brewmapp.presentation.presenter.impl.RegisterPresenterImpl;
import com.brewmapp.presentation.presenter.impl.RestoDetailPresenterImpl;
import com.brewmapp.presentation.presenter.impl.RestoEditFragmentPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SaleDetailsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SearchFragmentPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ResultSearchActivityPresenterImpl;
import com.brewmapp.presentation.presenter.impl.SettingsPresenterImpl;
import com.brewmapp.presentation.presenter.impl.ShareLikeViewPresenterImpl;
import com.brewmapp.presentation.presenter.impl.StartPresenterImpl;
import com.brewmapp.presentation.presenter.contract.ProfileFragmentEdit_presenter;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import dagger.Module;
import dagger.Provides;
import ru.frosteye.ovsa.di.module.BasePresenterModule;

@Module
public class PresenterModule extends BasePresenterModule<BaseActivity, BaseFragment, Service> {
    public PresenterModule(View view) {
        super(view);
    }

    public PresenterModule(BaseActivity activity) {
        super(activity);
    }

    public PresenterModule(Service service) {
        super(service);
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
    ProfileFragmentMain_presenter provideProfilePresenter(ProfileFragmentMain_presenter_Impl presenter) {
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
    MapFragment_presenter provideBeerMapPresenter(MapFragment_presenter_Impl presenter) {
        return presenter;
    }

    @Provides @PresenterScope
    SearchFragmentPresenter provideSearchAllPresenter(SearchFragmentPresenterImpl presenter) {
        return presenter;
    }
    @Provides @PresenterScope
    SettingsPresenter provideSettingsPresenter(SettingsPresenterImpl presenter) {
        return presenter;
    }
    
    @Provides @PresenterScope
    ResultSearchActivityPresenter provideSearchPresenter(ResultSearchActivityPresenterImpl presenter) {
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
    MultiListPresenter provideAddFavoriteBeerPresenter(MultiListPresenterImpl presenter){
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
    ProfileActivity_presenter provideProfileInfoPresenter(ProfileActivity_presenter_Impl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    SelectCategoryActivityPresenter provideFilterByCategory (SelectCategoryActivityPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    ProfileFragmentEdit_presenter provideProfileEditFragmentPresenter(ProfileFragmentEdit_presenter_Impl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    AddReviewBeerPresenter provideAddReviewBeerPresenter(AddReviewBeerPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    MultiFragmentActivityPresenter provideMultiFragmentActivityPresenter(MultiFragmentActivityPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    ProfileFragmentShot_presenter provideProfileViewFragmentPresenter(ProfileFragmentShot_presenter_Impl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    MessageFragmentPresenter provideMessageFragmentPresenter(MessageFragmentPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    BeerEditFragmentPresenter provideBeerEditFragmentPresenter(BeerEditFragmentPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    RestoEditFragmentPresenter provideRestoEditFragmentPresenter(RestoEditFragmentPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    ChatFragmentPresenter provideChatFragmentPresenter(ChatFragmentPresenterImpl presenter){
        return presenter;
    }
    @Provides @PresenterScope
    BreweryDetailsActivityPresenter provideBreweryDetailsActivityPresenter(BreweryDetailsActivityPresenterImpl presenter){
        return presenter;
    }

    @Provides @PresenterScope
    ProfileFragmentFull_presenter provideProfileFragmentFull_presenter(ProfileFragmentFull_presenter_Impl presenter){
        return presenter;
    }
}
