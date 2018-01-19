package com.brewmapp.app.environment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.impl.activity.BeerDetailActivity;
import com.brewmapp.presentation.view.impl.activity.FriendsActivity;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.activity.MultiListActivity;

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
}
