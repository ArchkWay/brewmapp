package com.brewmapp.app.environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.contract.MultiListView;
import com.brewmapp.presentation.view.impl.activity.AddReviewBeerActivity;
import com.brewmapp.presentation.view.impl.activity.AddReviewRestoActivity;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;
import com.brewmapp.presentation.view.impl.activity.FriendsActivity;
import com.brewmapp.presentation.view.impl.activity.InterestListActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.ProfileEditActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.brewmapp.presentation.view.impl.fragment.ProfileFragment;

import static com.brewmapp.app.environment.RequestCodes.REQUEST_CODE_REFRESH_ITEMS;

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
    public static void MultiListActivity(Activity activity, String action) {
        activity.startActivity(new Intent(action, null,activity,MultiListActivity.class));
    }

    public static void MainActivity(BeerDetailActivity activity, String action, Object o) {
        activity.startActivity(new Intent(action,null,activity,MainActivity.class));
    }

    public static void RestoDetailActivityForResult(Activity activity, Interest interest, int requestCode) {
        Intent intent = new Intent(activity, RestoDetailActivity.class);
        intent.putExtra(Keys.RESTO_ID, interest);
        activity.startActivityForResult(intent, requestCode);
    }
    public static void RestoDetailActivity(Context context, String id_resto) {
        Intent intent = new Intent(context, RestoDetailActivity.class);
        intent.putExtra(Keys.RESTO_ID, new Interest(new Resto(id_resto,"")));
        context.startActivity(intent);
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

    public static void MultiFragmentActivity_MODE_CHAT(Activity activity, User friend) {
        Intent intent=new Intent(MultiFragmentActivityView.MODE_CHAT, null, activity, MultiFragmentActivity.class);
        User user=new User();
        user.setId(friend.getId());
        user.setFirstname(friend.getFirstname());
        user.setLastname(friend.getLastname());

        intent.putExtra(RequestCodes.INTENT_EXTRAS,user);
        activity.startActivity(intent);

    }
}
