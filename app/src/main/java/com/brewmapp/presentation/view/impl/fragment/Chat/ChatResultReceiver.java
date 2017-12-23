package com.brewmapp.presentation.view.impl.fragment.Chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import com.brewmapp.execution.services.ChatService;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;

/**
 * Created by Kras on 23.12.2017.
 */

public class ChatResultReceiver extends ResultReceiver {

    private SimpleSubscriber<Bundle> simpleSubscriber;

    public ChatResultReceiver(Handler handler, SimpleSubscriber<Bundle> simpleSubscriber) {
        super(handler);
        this.simpleSubscriber=simpleSubscriber;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode){
            case ChatService.RESULT_OK:{
                simpleSubscriber.onNext(resultData);
            }break;
            case ChatService.RESULT_ERROR:{
                simpleSubscriber.onError(null);
            }break;
        }
    }
}