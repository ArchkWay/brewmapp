package com.brewmapp.presentation.view.impl.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatResultReceiver;

import butterknife.ButterKnife;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.activity.PresenterActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by ovcst on 01.05.2017.
 */

public abstract class BaseActivity extends PresenterActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PresenterComponent component = BeerMap.getAppComponent().plus(new PresenterModule(this));
        component.inject(this);
        inject(component);
        chatResultReceiver=new ChatResultReceiver(new Handler(getMainLooper()),new SimpleSubscriber<Bundle>(){
            @Override
            public void onNext(Bundle bundle) {
                super.onNext(bundle);
                String action=bundle.getString(ChatService.EXTRA_PARAM1);
                switch (action){
                    case ChatService.ACTION_RECEIVE_MESSAGE:
                        ChatReceiveMessage chatReceiveMessage = (ChatReceiveMessage) bundle.getSerializable(ChatService.EXTRA_PARAM2);
                        String text=chatReceiveMessage.getText()+getString(R.string.chat_new_message_text,chatReceiveMessage.getFrom().getFormattedName());
                        showSnackbar(text);
                        break;
                }
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
    }

    public void showSnackbar(String text) {
        View view=getWindow().getDecorView().findViewById(android.R.id.content);
        if(view!=null) {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(BaseActivity.this, R.color.mdtp_accent_color));
//                            snackbar.setAction(android.R.string.ok, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    snackbar.dismiss();
//                                }
//                            });
            snackbar.show();
        }
    }

    protected abstract void inject(PresenterComponent component);

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        Toolbar toolbar = findActionBar();
        if(toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    protected Toolbar findActionBar() {
        return null;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSnackbarReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unRegisterSnackbarReceiver();
    }

//******************************************
    private ChatResultReceiver chatResultReceiver;

    private void unRegisterSnackbarReceiver() {
        Intent intent=new Intent(ChatService.ACTION_CLEAR_RECEIVER,null,this, ChatService.class);
        startService(intent);
    }
    private void registerSnackbarReceiver() {
        Intent intent=new Intent(ChatService.ACTION_SET_RECEIVER,null,this, ChatService.class);
        intent.putExtra(ChatService.RECEIVER,chatResultReceiver);
        startService(intent);
    }

}
