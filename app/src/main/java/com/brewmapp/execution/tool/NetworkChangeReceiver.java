package com.brewmapp.execution.tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.brewmapp.execution.services.ChatService;

public class NetworkChangeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(isOnline(context)) {
            context.startService(new Intent(ChatService.ACTION_INIT_CHAT_SERVICE, null, context, ChatService.class));
        }else {
            context.startService(new Intent(ChatService.ACTION_INTERNET_OFF, null, context, ChatService.class));
        }
    }

    public boolean isOnline(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }
}
