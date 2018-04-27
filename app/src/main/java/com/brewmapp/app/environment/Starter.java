package com.brewmapp.app.environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.impl.activity.AddReviewBeerActivity;
import com.brewmapp.presentation.view.impl.activity.AddReviewRestoActivity;
import com.brewmapp.presentation.view.impl.activity.AlbumsActivity;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;
import com.brewmapp.presentation.view.impl.activity.BreweryDetailsActivity;
import com.brewmapp.presentation.view.impl.activity.FriendsActivity;
import com.brewmapp.presentation.view.impl.activity.InterestListActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.PhotoGalleryActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.activity.ResultSearchActivity;
import com.brewmapp.presentation.view.impl.activity.SelectCategoryActivity;
import com.crashlytics.android.Crashlytics;

import java.io.Serializable;
import java.util.ArrayList;

import ru.frosteye.ovsa.execution.executor.Callback;


/**
 * Created by xpusher on 1/19/2018.
 */

public class Starter {
    public static void StartFriendsActivity(FragmentActivity activity, boolean subsccriber) {
        Intent intent = new Intent(activity, FriendsActivity.class);
        intent.putExtra(Keys.SUBSCRIBERS, subsccriber);
        activity.startActivity(intent);
    }

    public static void MultiListActivity_MODE_SHOW_REVIEWS_BEER(Activity activity,String beerId) {
        activity.startActivity(new Intent(MultiListView.MODE_SHOW_REVIEWS_BEER, Uri.parse(beerId),activity,MultiListActivity.class));
    }
    public static void MultiListActivity_MODE_SHOW_REVIEWS_RESTO(Activity activity,String resto_id) {
        activity.startActivity(new Intent(MultiListView.MODE_SHOW_REVIEWS_RESTO,Uri.parse(resto_id),activity,MultiListActivity.class));
    }
    public static void MultiListActivity_MODE_SHOW_MENU(Activity activity,Resto resto) {
        Intent intent=new Intent(MultiListView.MODE_SHOW_MENU,null,activity,MultiListActivity.class);
        intent.putExtra(activity.getString(R.string.key_serializable_extra),resto);
        activity.startActivity(intent);
    }
    public static void MultiListActivity(Activity activity, String action){
        MultiListActivity(activity, action, null);
    }
    public static void MultiListActivity(Activity activity, String action, String use_id) {
        activity.startActivity(new Intent(action, Uri.parse(use_id),activity,MultiListActivity.class));
    }

    public static void MainActivity(BaseActivity activity, String action, Object... o) {
        Intent intent=new Intent(action,null,activity,MainActivity.class);
        switch (action){
            case MainActivity.MODE_MAP_FRAGMENT:
                intent.putExtra(Keys.LOCATION,(Serializable) o[0]);
                break;
            case MainActivity.MODE_EVENT_FRAGMENT_WITHOUT_TABS:
                intent.putExtra(RequestCodes.INTENT_EXTRAS,(Integer) o[0]);
                intent.putExtra(Keys.RELATED_ID,String.valueOf(o[1]));
                intent.putExtra(Keys.RELATED_MODEL,String.valueOf(o[2]));
                break;
        }
        activity.startActivity(intent);

    }

    public static void RestoDetailActivityForResult(BaseActivity activity, Interest interest, int requestCode) {
        Intent intent = new Intent(activity, RestoDetailActivity.class);
        intent.putExtra(Keys.RESTO_ID, interest);
        intent.putExtra(activity.getString(R.string.key_start_activity_invisible),activity.StartProgressBarInParentActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivityForResult(intent, requestCode);
    }
    public static void RestoDetailActivity(BaseActivity baseActivity, String id_resto) {
        if(id_resto!=null) {
            Intent intent = new Intent(baseActivity, RestoDetailActivity.class);
            intent.putExtra(Keys.RESTO_ID, new Interest(new Resto(id_resto, "")));
            intent.putExtra(baseActivity.getString(R.string.key_start_activity_invisible),baseActivity.StartProgressBarInParentActivity());
            intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            baseActivity.startActivity(intent);
        }
    }

    public static void RestoDetailActivity_With_SCROLL(Context context, String id_resto, int type_scroll) {
        if(id_resto!=null) {
            Intent intent = new Intent(context, RestoDetailActivity.class);
            intent.putExtra(Keys.RESTO_ID, new Interest(new Resto(id_resto, "")));
            intent.setAction(String.valueOf(type_scroll));
            context.startActivity(intent);
        }
    }

    public static void AddReviewRestoActivityForResult(Activity activity, RestoDetail restoDetails, int requestCodeReview) {
        Intent intent=new Intent(activity, AddReviewRestoActivity.class);
        intent.putExtra(Keys.RESTO_ID,restoDetails);
        activity.startActivityForResult(intent, requestCodeReview);

    }

    public static void AddReviewBeerActivityForResult(Activity activity, String id_beer, int requestCode) {
        Intent intent=new Intent(activity, AddReviewBeerActivity.class);
        intent.putExtra(Keys.CAP_BEER,id_beer);
        activity.startActivityForResult(intent, requestCode);

    }

    public static void BeerDetailActivity(Context context, String id_beer) {
        Intent intent = new Intent(context, BeerDetailActivity.class);
        intent.putExtra(context.getString(R.string.key_serializable_extra), new Interest(new Beer(id_beer)));
        context.startActivity(intent);

    }

    public static void InterestListActivity(Activity activity, String model, int user_id) {
        activity.startActivity(
                new Intent(model,Uri.parse(String.valueOf(user_id)),activity, InterestListActivity.class)
        );
    }

    public static void InterestListActivityForResult(Activity activity, String model, int user_id, int requestCode) {
        activity.startActivityForResult(
                new Intent(model,Uri.parse(String.valueOf(user_id)),activity, InterestListActivity.class),requestCode
        );
    }

    public static void ProfileEditActivityForResult(Activity activity, String action, String user_id, int requestCode) {
        activity.startActivityForResult(
                new Intent(
                        action,
                        Uri.parse(user_id),
                        activity,
                        ProfileEditActivity.class
                ),
                requestCode
        );
    }

    public static void ProfileEditActivity(Activity activity, String action, String user_id) {
        activity.startActivity(
                new Intent(
                        action,
                        Uri.parse(user_id),
                        activity,
                        ProfileEditActivity.class
                )
        );

    }

    public static void ProfileEditActivity_StartInVisible(BaseActivity baseActivity, String action, String user_id) {
        Intent intent=new Intent(
                action,
                Uri.parse(user_id),
                baseActivity, ProfileEditActivity.class);
        intent.putExtra(baseActivity.getString(R.string.key_start_activity_invisible),baseActivity.StartProgressBarInParentActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        baseActivity.startActivity(intent);
    }

    public static void ProfileEditActivity_StartInVisible_For_Result(BaseActivity baseActivity, String action, String user_id, int requestCode) {
        Intent intent=new Intent(
                action,
                Uri.parse(user_id),
                baseActivity, ProfileEditActivity.class);
        intent.putExtra(baseActivity.getString(R.string.key_start_activity_invisible),baseActivity.StartProgressBarInParentActivity());
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        baseActivity.startActivityForResult(intent,requestCode);

    }

    public static void MultiFragmentActivity_MODE_CHAT(Activity activity, User friend) {
        Intent intent=new Intent(MultiFragmentActivityView.MODE_CHAT, null, activity, MultiFragmentActivity.class);
        User user=new User();
        user.setId(friend.getId());
        user.setFirstname(friend.getFirstname());
        user.setLastname(friend.getLastname());

        intent.putExtra(RequestCodes.INTENT_EXTRAS,user);
        activity.startActivity(intent);

    }
    public static void MultiFragmentActivity_MODE_FORM_I_OWNER(Activity activity) {
        Intent intent=new Intent(MultiFragmentActivityView.MODE_FORM_I_OWNER, null, activity, MultiFragmentActivity.class);
        activity.startActivity(intent);
    }

    public static void BreweryDetailsActivity(Activity activity, String breweryId) {
        Intent intent=new Intent(activity,BreweryDetailsActivity.class);
        intent.putExtra(Actions.PARAM1,breweryId);
        activity.startActivity(intent);
    }

    public static void InfoAboutCrashSendToServer(String info_error,String info_class_name) {

        Crashlytics.logException(new Exception(
                new StringBuilder()
                        .append(info_error)
                        .append(";")
                        .append(info_class_name)
                        .toString()
        ));
    }

    public static void PhotoGalleryActivity(BaseActivity baseActivity, ArrayList<Photo> photoArrayList, String related_model, String id_model) {
                Intent intent=new Intent(baseActivity, PhotoGalleryActivity.class);
                intent.putExtra(baseActivity.getString(R.string.key_photo_array_list),  photoArrayList);
                intent.putExtra(baseActivity.getString(R.string.key_related_model),  related_model);
                intent.putExtra(baseActivity.getString(R.string.key_id_model),  id_model);
                baseActivity.startActivity(intent);

    }

    public static void ResultSearchActivity(BaseActivity baseActivity, String selectedTab) {
        ResultSearchActivity(baseActivity, selectedTab, null);
    }
    public static void ResultSearchActivity(BaseActivity baseActivity, String selectedTab, String beer_id) {
        Intent intent = new Intent(baseActivity, ResultSearchActivity.class);
        intent.putExtra(Actions.PARAM1,selectedTab);
        intent.putExtra(baseActivity.getString(R.string.key_beer),beer_id);
        if(beer_id==null){
            baseActivity.startActivity(intent);
        }else {
            baseActivity.requestCity(new Callback<City>() {
                @Override
                public void onResult(City city) {
                    if(city!=null){
                        intent.putExtra(baseActivity.getString(R.string.key_city_id),String.valueOf(city.getId()));
                        intent.putExtra(baseActivity.getString(R.string.key_city_name),city.getName());
                        baseActivity.startActivity(intent);
                    }
                }
            });
        }

    }

    public static void SelectCategoryActivity(
            BaseActivity activity,
            String category,
            int filter,
            String split_id,
            String split_name,
            int requestSearchCode) {

        Intent intent = new Intent(activity, SelectCategoryActivity.class);
        intent.putExtra(Actions.PARAM1,category);
        intent.putExtra(Actions.PARAM2,filter);
        intent.putExtra(Actions.PARAM3,split_id);
        intent.putExtra(Actions.PARAM4,split_name);

        activity.startActivityForResult(intent,requestSearchCode);

    }

    public static void SelectCategoryActivity(
            Fragment fragment,
            String category,
            int filter,
            String split_id,
            String split_name,
            int requestSearchCode) {

        Intent intent = new Intent(fragment.getActivity(), SelectCategoryActivity.class);
        intent.putExtra(Actions.PARAM1,category);
        intent.putExtra(Actions.PARAM2,filter);
        intent.putExtra(Actions.PARAM3,split_id);
        intent.putExtra(Actions.PARAM4,split_name);

        fragment.startActivityForResult(intent,requestSearchCode);

    }

    public static void AlbumsActivity(BaseActivity baseActivity, int user_id) {
        baseActivity.startActivity(new Intent("",Uri.parse(String.valueOf(user_id)),baseActivity,AlbumsActivity.class));
    }
}
