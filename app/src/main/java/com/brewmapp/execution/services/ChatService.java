package com.brewmapp.execution.services;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.execution.exchange.request.base.Keys;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.socket.client.Socket;


public class ChatService extends Service {



    private InnerThis innerThis;

    public static final String ACTION_SEND_MESSAGE = "com.brewmapp.execution.services.action.ACTION_SEND__MESSAGE";
    public static final String ACTION_RECIEVE_MESSAGE = "com.brewmapp.execution.services.action.ACTION__RECIEVE_MESSAGE";
    public static final String ACTION_REQUEST_MESSAGES = "com.brewmapp.execution.services.action.ACTION__MESSAGES";
    public static final String ACTION_REQUEST_DIALOGS = "com.brewmapp.execution.services.action.REQUEST_DIALOGS";
    public static final String ACTION_AUTHORIZATION = "com.brewmapp.execution.services.action.AUTHORIZATION";
    public static final String EXTRA_PARAM1 = "com.brewmapp.execution.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.brewmapp.execution.services.extra.PARAM2";
    public static final String RECEIVER = "com.brewmapp.execution.services.extra.RECEIVER";
    public static final int STATUS_COMPLEATE = 0;
    public static final int STATUS_ERROR = 1;



    @Inject public Socket socket;
    @Inject public UserRepo userRepo;


    public ChatService() {
        innerThis=new InnerThis();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        innerThis.setIntent(intent);

        switch (innerThis.getAction(intent)) {
            case ACTION_SEND_MESSAGE:
                innerThis.sendMessages();
                break;
            case ACTION_REQUEST_MESSAGES:
                innerThis.requestMessages();
                break;
            case ACTION_REQUEST_DIALOGS:
                innerThis.requestListDialogs();
                break;
            case ACTION_AUTHORIZATION:
                innerThis.setReciever(intent);
                innerThis.initSocket();
                break;
        }

        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        innerThis.disconnect();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class InnerThis{
        ResultReceiver receiver;
        boolean isConnected=false;
        Intent intent;
        //dialogs
        private void requestListDialogs() {
            socket.emit(Keys.CHAT_EVENT_ROOMS);
        }
        public void loadDialogs(Object[] args) {
            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DIALOGS);
                bundle.putString(EXTRA_PARAM2,String.valueOf(args[0]));
                sendStatus(STATUS_COMPLEATE,bundle);
            }catch (Exception e){
                sendStatus(STATUS_ERROR);
            }
        }
        //connection
        void initSocket() {
            socket.on(Socket.EVENT_DISCONNECT,args -> sendStatus(STATUS_ERROR,args));
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> sendStatus(STATUS_ERROR,args));
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> sendStatus(STATUS_ERROR,args));
            //Swap
            socket.on(Socket.EVENT_CONNECT,args ->  authorization());
            socket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, args -> successAuthorisation());
            socket.connect();
            socket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, args -> loadDialogs(args));
            socket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, args -> loadMessages(args));
            socket.on(Keys.CHAT_EVENT_SEND_SUCCESS, args -> sentSuccess(args));
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> sentError(args));
            socket.on(Socket.EVENT_MESSAGE, args -> receiveMessage(args));
        }

        private void receiveMessage(Object[] args) {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1, ACTION_RECIEVE_MESSAGE);
            bundle.putString(EXTRA_PARAM2, String.valueOf(args[0]));
            sendStatus(STATUS_COMPLEATE,bundle);
        }

        private void sentError(Object[] args) {
            sendStatus(STATUS_ERROR);
        }

        private void sentSuccess(Object[] args) {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1, ACTION_SEND_MESSAGE);
            sendStatus(STATUS_COMPLEATE,bundle);
        }

        void loadMessages(Object[] args) {
            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_MESSAGES);
                bundle.putString(EXTRA_PARAM2,String.valueOf(args[0]));
                sendStatus(STATUS_COMPLEATE,bundle);
            }catch (Exception e){
                sendStatus(STATUS_ERROR);
            }
        }

        void successAuthorisation() {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1,ACTION_AUTHORIZATION);
            sendStatus(STATUS_COMPLEATE,bundle);
        }
        void disconnect() {
            socket.off();
            socket.disconnect();
            socket.close();
        }
        void authorization() {
            if(!isConnected) {
                isConnected = true;
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(Keys.ID, userRepo.load().getId());
                    jsonObject.put(Keys.TOKEN, userRepo.load().getToken());
                    socket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    sendStatus(STATUS_ERROR,e.getMessage());
                }
            }
        }
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
        void sendStatus(int status, Object... object) {
            if(receiver!=null) {
                if (object.length > 0 && object[0] instanceof Bundle)
                    receiver.send(status, (Bundle) object[0]);
                else {
                    receiver.send(status, Bundle.EMPTY);
                    stopSelf();
                }
            }
        }
        void setIntent(Intent intent) {
            this.intent=intent;
        }
        String getAction(Intent intent) {
            try {
                return intent.getAction();
            }catch (Exception e) {
                return null;
            }
        }
        void setReciever(Intent intent) {
            receiver = intent.getParcelableExtra(RECEIVER);
        }
        void requestMessages() {
            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            if(friend_id==0){
                sendStatus(STATUS_ERROR);
            }else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Keys.USER_ID, friend_id);
                    jsonObject.put(Keys.CHAT_KEY_LAST_MSG, "*");
                    socket.emit(Keys.CHAT_EVENT_LOAD, jsonObject);
                } catch (JSONException e) {
                    sendStatus(STATUS_ERROR);
                }
            }
        }
        void sendMessages() {
            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            String text_message=intent.getStringExtra(EXTRA_PARAM2);

            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put(Keys.USER_ID,friend_id);
                jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,text_message);
                socket.emit(Keys.CHAT_EVENT_SEND,jsonObject);
            } catch (JSONException e) {
                sendStatus(STATUS_ERROR);
            }
        }
    }
}
