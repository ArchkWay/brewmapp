package com.brewmapp.app.environment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;
import com.brewmapp.presentation.view.impl.activity.FriendsActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;

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

    public static void MultiListActivity(Activity activity, String action, String beerId) {
        activity.startActivity(new Intent(action, Uri.parse(beerId),activity,MultiListActivity.class));
    }
    public static void MultiListActivity(Activity activity, String action) {
        activity.startActivity(new Intent(action, null,activity,MultiListActivity.class));
    }

    public static void MainActivity(BeerDetailActivity activity, String action, Object o) {
        activity.startActivity(new Intent(action,null,activity,MainActivity.class));
    }

    public static void RestoDetailActivityForResult(Activity activity, Interest payload, int requestCodeRefreshItems) {
        Intent intent = new Intent(activity, RestoDetailActivity.class);
        intent.putExtra(Keys.RESTO_ID, (Interest)payload);
        activity.startActivityForResult(intent, REQUEST_CODE_REFRESH_ITEMS);
    }
    public static void RestoDetailActivity(Context context, String id_resto) {
        Intent intent = new Intent(context, RestoDetailActivity.class);
        intent.putExtra(Keys.RESTO_ID, new Interest(new Resto(id_resto,"")));
        context.startActivity(intent);
    }
}
