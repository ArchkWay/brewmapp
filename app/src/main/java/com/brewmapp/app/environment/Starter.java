package com.brewmapp.app.environment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.contract.MultiListView;
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

    public static void MultiListActivity(BeerDetailActivity activity, String action, String beerId) {
        activity.startActivity(new Intent(MultiListView.MODE_SHOW_REVIEWS_BEER, Uri.parse(beerId),activity,MultiListActivity.class));
    }

    public static void MainActivity(BeerDetailActivity activity, String action, Object o) {
        activity.startActivity(new Intent(MainActivity.MODE_MAP_FRAGMENT,null,activity,MainActivity.class));
    }
}
