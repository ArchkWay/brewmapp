package com.brewmapp.execution.services;

import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.services.base.BaseService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.Socket;


public class ChatService extends BaseService{



    private InnerWorker innerWorker =new InnerWorker();

    public static final String ACTION_SEND_MESSAGE = "com.brewmapp.execution.services.action.ACTION_SEND_MESSAGE";
    public static final String ACTION_RECEIVE_MESSAGE = "com.brewmapp.execution.services.action.ACTION_RECEIVE_MESSAGE";
    public static final String ACTION_REQUEST_DIALOG_CONTENT = "com.brewmapp.execution.services.action.ACTION_REQUEST_DIALOG_CONTENT";
    public static final String ACTION_REQUEST_DIALOGS = "com.brewmapp.execution.services.action.ACTION_REQUEST_DIALOGS";
    public static final String ACTION_INIT_DIALOG = "com.brewmapp.execution.services.action.ACTION_INIT_DIALOG";
    public static final String ACTION_SET_RECEIVER = "com.brewmapp.execution.services.action.ACTION_SET_RECEIVER";
    public static final String ACTION_CLEAR_RECEIVER = "com.brewmapp.execution.services.action.ACTION_CLEAR_RECEIVER";
    public static final String ACTION_INIT_CHAT_SERVICE = "com.brewmapp.execution.services.action.ACTION_INIT_CHAT_SERVICE";
    public static final String ACTION_INTERNET_OFF = "com.brewmapp.execution.services.action.ACTION_INTERNET_OFF";
    public static final String EXTRA_PARAM1 = "com.brewmapp.execution.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.brewmapp.execution.services.extra.PARAM2";
    public static final String RECEIVER = "com.brewmapp.execution.services.extra.RECEIVER";
    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = 1;

    @Inject public Socket socket;
    @Inject public UserRepo userRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String action=intent.getAction();

        switch (action){
            case ACTION_INIT_CHAT_SERVICE:
                innerWorker.initSocket();
                break;
            case ACTION_INTERNET_OFF:
                innerWorker.disconnect();
                break;
            default: {
                innerWorker.addQueue(innerWorker.getAction(intent), intent);
                if(innerWorker.isAuthorized)
                    innerWorker.handleQueue();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        innerWorker.disconnect();
    }

    class InnerWorker {
        ResultReceiver receiver;
        boolean isAuthorized =false;
        List<Action> queue= new ArrayList<>();

        void initSocket() {
            socket.on(Socket.EVENT_DISCONNECT,args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            //Swap
            socket.on(Socket.EVENT_CONNECT, this::authorization);
            socket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, this::successAuthorisation);
            socket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, this::loadDialogs);
            socket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, this::loadMessages);
            socket.on(Keys.CHAT_EVENT_SEND_SUCCESS, this::sentSuccess);
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, this::sentError);
            socket.on(Socket.EVENT_MESSAGE, this::receiveMessage);

            socket.connect();
        }
        void requestListDialogs() {
            socket.emit(Keys.CHAT_EVENT_ROOMS);
        }
        void loadDialogs(Object[] args) {
            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DIALOGS);
                bundle.putString(EXTRA_PARAM2,String.valueOf(args[0]));
                returnResult(RESULT_OK,bundle);
            }catch (Exception e){
                returnResult(RESULT_ERROR,Bundle.EMPTY);
            }
        }
        void receiveMessage(Object[] args) {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1, ACTION_RECEIVE_MESSAGE);
            bundle.putString(EXTRA_PARAM2, String.valueOf(args[0]));
            returnResult(RESULT_OK,bundle);
        }
        void sentError(Object[] args) {
            returnResult(RESULT_ERROR,Bundle.EMPTY);
        }
        void sentSuccess(Object[] args) {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1, ACTION_SEND_MESSAGE);
            bundle.putString(EXTRA_PARAM2, ((JSONObject) args[0]).toString());
            returnResult(RESULT_OK,bundle);
        }
        void loadMessages(Object[] args) {
            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DIALOG_CONTENT);
                bundle.putString(EXTRA_PARAM2,String.valueOf(args[0]));
                returnResult(RESULT_OK,bundle);
            }catch (Exception e){
                returnResult(RESULT_ERROR,Bundle.EMPTY);
            }
        }
        void successAuthorisation(Object[] args) {
            isAuthorized = true;
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1,ACTION_INIT_DIALOG);
            intent.putExtras(bundle);
            addQueue(ACTION_INIT_DIALOG,intent);
            handleQueue();
        }
        void handleQueue() {

            if(queue.size()>0) {
                String action=queue.get(0).getAction();
                Intent intent=queue.get(0).getIntent();
                switch (action) {
                    case ACTION_SEND_MESSAGE:
                        innerWorker.sendMessages(intent);
                        break;
                    case ACTION_REQUEST_DIALOG_CONTENT:
                        innerWorker.requestMessages(intent);
                        break;
                    case ACTION_REQUEST_DIALOGS:
                        innerWorker.requestListDialogs();
                        break;
                    case ACTION_INIT_DIALOG:
                        returnResult(RESULT_OK,intent.getExtras());
                        break;
                    case ACTION_SET_RECEIVER:
                        innerWorker.setReceiver(intent);
                        break;
                    case ACTION_CLEAR_RECEIVER:
                        innerWorker.setReceiver(null);
                        break;
                }
            }
        }
        void disconnect() {
            isAuthorized = false;
            socket.off();
            socket.disconnect();
            socket.close();
        }
        void authorization(Object[] args) {
            if(!isAuthorized) {
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(Keys.ID, userRepo.load().getId());
                    jsonObject.put(Keys.TOKEN, userRepo.load().getToken());
                    socket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    returnResult(RESULT_ERROR,Bundle.EMPTY);
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
        void returnResult(int status, Bundle bundle) {

            if(status==RESULT_ERROR) {

            }else if(status==RESULT_OK) {
                if(queue.size()>0){
                    removeQueue();
                    if (receiver != null)
                        receiver.send(status, bundle);
                    handleQueue();
                }else {
                    if (receiver != null)
                        receiver.send(status, bundle);
                }
            }

        }
        private void removeQueue() {
                queue.remove(0);
        }
        String getAction(Intent intent) {
            try {
                return intent.getAction();
            }catch (Exception e) {
                return null;
            }
        }
        void setReceiver(Intent intent) {
            if(intent==null){
                receiver=null;
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_CLEAR_RECEIVER);
                returnResult(RESULT_OK, bundle);
            }else {
                receiver = intent.getParcelableExtra(RECEIVER);
                Bundle bundle = new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_SET_RECEIVER);
                returnResult(RESULT_OK, bundle);
            }
        }
        void requestMessages(Intent intent) {
            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            int page=intent.getIntExtra(EXTRA_PARAM2,0);

            if(friend_id==0){
                returnResult(RESULT_ERROR,Bundle.EMPTY);
            }else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Keys.USER_ID, friend_id);
                    jsonObject.put(Keys.CHAT_KEY_LAST_MSG, page==0?"*":page);
                    socket.emit(Keys.CHAT_EVENT_LOAD, jsonObject);
                } catch (JSONException e) {
                    returnResult(RESULT_ERROR,Bundle.EMPTY);
                }
            }
        }
        void sendMessages(Intent intent) {
            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            String text_message=intent.getStringExtra(EXTRA_PARAM2);

            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put(Keys.USER_ID,friend_id);
                jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,text_message);
                socket.emit(Keys.CHAT_EVENT_SEND,jsonObject);
            } catch (JSONException e) {
                returnResult(RESULT_ERROR,Bundle.EMPTY);
            }
        }
        void addQueue(String action, Intent intent) {
            queue.add(new Action(action,intent));
        }
    }
    class Action{
        private String action;
        private Intent intent;

        public Action(String action, Intent intent) {
            setAction(action);
            setIntent(intent);
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Intent getIntent() {
            return intent;
        }

        public void setIntent(Intent intent) {
            this.intent = intent;
        }
    }
}
