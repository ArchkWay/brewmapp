package com.brewmapp.execution.services;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.os.ResultReceiver;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;


public class ChatService extends IntentService {
    
    private InnerThis innerThis;

    public static final String ACTION_LOAD_OLD_MESSAGE = "com.brewmapp.execution.services.action.LOAD_OLD_MESSAGE";
    public static final String ACTION_AUTHORIZATION = "com.brewmapp.execution.services.action.AUTHORIZATION";
    public static final String ACTION_BAZ = "com.brewmapp.execution.services.action.BAZ";
    public static final String EXTRA_PARAM1 = "com.brewmapp.execution.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.brewmapp.execution.services.extra.PARAM2";
    public static final String RECEIVER = "com.brewmapp.execution.services.extra.RECEIVER";
    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_ERROR = 1;
    public static final int STATUS_COMPLEATE = 1;


    private Socket socket;
    private Intent intent;

    public ChatService() {
        super("ChatService");
        innerThis=new InnerThis();
        socket = BeerMap.getAppComponent().getSocket();
        innerThis.initSocket();

    }


    @Override
    protected void onHandleIntent(Intent intent) {
        this.intent=intent;
        innerThis.prepare();
        switch (innerThis.getAction(intent)){
            case ACTION_AUTHORIZATION: innerThis.requestAuthorization();break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        innerThis.disconnect();
    }

    class InnerThis{
        ResultReceiver receiver;
        boolean isConnected;
        //foreground
        void foregroundON(){
            Notification.Builder builder = new Notification.Builder(getBaseContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker("Your Ticker") // use something from something from R.string
                    .setContentTitle("Your content title") // use something from something from
                    .setContentText("Your content text") // use something from something from
                    .setProgress(0, 0, true); // display indeterminate progress

            startForeground(1,builder.build());
        }
        void foregroundOFF(){
            stopForeground(true);
        }
        //Status
        void sendStatus(int status, Object... args) {
            Bundle bundle=new Bundle();
            switch (status){
                case STATUS_ERROR:
                    bundle.putSerializable(RequestCodes.INTENT_EXTRAS,(String[])args);
                    break;
                default:
                    bundle=null;
            }
            if(bundle!=null)
                receiver.send(status, bundle);
        }
        void prepare() {
            receiver = intent.getParcelableExtra(RECEIVER);
            sendStatus(STATUS_RUNNING, Bundle.EMPTY);
        }
        //parse intent
        public String getAction(Intent intent) {
            try {
                return intent.getAction();
            }catch (Exception e) {
                return null;
            }
        }
        //swap
        public void requestAuthorization() {
            User user;
            try {
                user= (User) intent.getSerializableExtra(RequestCodes.INTENT_EXTRAS);
            }catch (Exception e){
                sendStatus(STATUS_ERROR,e.getMessage());
                return;
            }

            if(!isConnected) {
                isConnected = true;
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(Keys.ID, user.getId());
                    jsonObject.put(Keys.TOKEN, user.getToken());
                    socket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendStatus(STATUS_ERROR,e.getMessage());
                }
            }

        }
        public void successAuthorization(String[] strings) {

        }
        public void initSocket() {
            socket.on(Socket.EVENT_DISCONNECT,args -> sendStatus(STATUS_ERROR,args));
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> sendStatus(STATUS_ERROR,args));
            //Swap
            socket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, args -> successAuthorization((String[])args));
//            socket.on(Socket.EVENT_CONNECT,args ->  view.getActivity().runOnUiThread(()->authorization()));
//            socket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, args -> view.getActivity().runOnUiThread(()->loadDialogs(args)));
//            socket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, args -> view.getActivity().runOnUiThread(()->loadMessages(args)));
//            socket.on(Keys.CHAT_EVENT_SEND_SUCCESS, args -> view.getActivity().runOnUiThread(()->view.sendSuccess()));
//            socket.on(Socket.EVENT_MESSAGE, args -> view.getActivity().runOnUiThread(()->receive(args)));

        }

        public void disconnect() {
            socket.off();
            socket.disconnect();
        }
    }
}
