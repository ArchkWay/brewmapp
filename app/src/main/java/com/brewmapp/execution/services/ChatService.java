package com.brewmapp.execution.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.ChatListMessages;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.execution.exchange.common.ChatImage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.ChatListDialogs;
import com.brewmapp.execution.services.base.BaseService;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.socket.client.Socket;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;


public class ChatService extends BaseService{
    private String LogTag="ChatService";

    //********ACTION incoming and outgoing
    public static final String ACTION_REQUEST_DELETE_DIALOG = "ACTION_IN_REQUEST_DELETE_DIALOG";
    public static final String ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE";
    public static final String ACTION_SEND_IMAGE = "ACTION_SEND_IMAGE";
    public static final String ACTION_REQUEST_DIALOG_CONTENT = "ACTION_REQUEST_DIALOG_CONTENT";
    public static final String ACTION_REQUEST_DIALOGS = "ACTION_REQUEST_DIALOGS";
    public static final String ACTION_SET_RECEIVER = "ACTION_SET_RECEIVER";
    public static final String ACTION_CLEAR_RECEIVER = "ACTION_CLEAR_RECEIVER";
    public static final String ACTION_SET_ONLINE = "ACTION_SET_ONLINE";
    public static final String ACTION_SET_OFFLINE = "ACTION_SET_OFFLINE";
    public static final String ACTION_MARK_MESSAGE_READ = "ACTION_MARK_MESSAGE_READ";
    public static final String ACTION_COUNT_MESSAGE_UNREAD = "ACTION_COUNT_MESSAGE_UNREAD";
    //********ACTION outgoing
    public static final String ACTION_RESTART_SWAP = "ACTION_RESTART_SWAP";
    public static final String ACTION_RECEIVE_MESSAGE = "ACTION_RECEIVE_MESSAGE";
    //********KEY PARAM
    public static final String EXTRA_PARAM1 = "com.brewmapp.execution.services.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "com.brewmapp.execution.services.extra.PARAM2";
    public static final String RECEIVER = "com.brewmapp.execution.services.extra.RECEIVER";
    private static final String KEY_TIME_ADD_TO_QUEUE = "com.brewmapp.execution.services.extra.KEY_TIME_ADD_TO_QUEUE";
    //********KEY RESULT
    public static final int RESULT_OK = 0;
    public static final int RESULT_ERROR = 1;

    @Inject public Socket socket;
    @Inject public UserRepo userRepo;
    @Inject public UiSettingRepo uiSettingRepo;

    @Override
    public void onCreate() {
        super.onCreate();
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if(intent!=null) {

            String action =intent.getAction();
            Log.i(LogTag,action+"(receive)");
            if (action != null)
                switch (action) {
                    case ACTION_SET_ONLINE:
                        openSocket();
                        uiSettingRepo.setIsOnLine(true);
                        break;
                    case ACTION_SET_OFFLINE:
                        closeSocket();
                        uiSettingRepo.setIsOnLine(false);
                        break;
                    case ACTION_SET_RECEIVER:
                        setReceiver(intent);
                        break;
                    case ACTION_CLEAR_RECEIVER:
                        clearReceiver();
                        break;
                    default: {
                        if (addToQueue(intent))
                            if (isAuthorized)
                                handleQueue();
                            else
                                openSocket();

                    }
                }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeSocket();
    }

    //************************************************************************
    private ResultReceiver receiver;
    private boolean isAuthorized =false;
    private List<ChatCommand> queue= new ArrayList<>();

    private void openSocket() {
        socket.off();

            socket.on(Socket.EVENT_DISCONNECT,args -> onError(args));
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> onError(args));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> onError(args));
            socket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> onError(args));
            socket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> onError(args));
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> onError(args));

            socket.on(Socket.EVENT_CONNECT, this::requestAuthorization);
            socket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, this::receiveAuthorisationSuccess);
            socket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, this::receiveListDialogs);
            socket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, this::receiveMessages);
            socket.on(Keys.CHAT_EVENT_SEND_SUCCESS, this::receiveSendMessageSuccess);
            socket.on(Keys.CHAT_DELETE_DIALOG_SUCCESS, this::receiveDeleteDialogSuccess);
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, this::onError);
            socket.on(Keys.CHAT_DELETE_DIALOG_ERROR, this::onError);
            socket.on(Keys.CHAT_MARK_ESTIMATED_SUCCESS, this::receiveMarkEstimatedSuccess);
            socket.on(Socket.EVENT_MESSAGE, this::onIncomingMessage);

            socket.connect();
        }
    private void closeSocket() {
            isAuthorized = false;
            socket.off();
            socket.disconnect();
            socket.close();
            queue.clear();
        }
    private void handleQueue() {

        if(queue.size()==0) return;

        Intent intent=queue.get(0).getIntent();
        String action=intent.getAction();
        Log.i(LogTag,action+"(go...)");
        switch (action) {
            case ACTION_SEND_IMAGE:
                requestSendImage(intent);
                break;
            case ACTION_SEND_MESSAGE:
                requestSendMessage(intent);
                break;
            case ACTION_REQUEST_DIALOG_CONTENT:
                requestMessages(intent);
                break;
            case ACTION_REQUEST_DIALOGS:
                requestListDialogs();
                break;
            case ACTION_RECEIVE_MESSAGE:
                returnResult(RESULT_OK,intent.getExtras());
                break;
            case ACTION_REQUEST_DELETE_DIALOG:
                requestDeleteDialog(intent);
                break;
            case ChatService.ACTION_MARK_MESSAGE_READ:
                requestMarkEstimated(intent);
                break;
            default:
                onError();

        }
    }
    private void returnResult(int status, Bundle bundle) {

        if(status==RESULT_OK) {
            String action=bundle.getString(EXTRA_PARAM1);
            switch (action){
                case ACTION_CLEAR_RECEIVER:
                case ACTION_SET_RECEIVER:
                case ACTION_RESTART_SWAP:
                    if (receiver != null)
                        receiver.send(status, bundle);
                    break;
                case ACTION_RECEIVE_MESSAGE:
                    if(receiver==null)
                        showNotification(bundle);
                default: {
                    if(queue.size()>0 && action.equals(queue.get(0).getIntent().getAction())) {
                        ResultReceiver receiver=queue.get(0).getReceiver();
                        if(receiver!=null)
                            receiver.send(status, bundle);
                        else if (this.receiver != null)
                            this.receiver.send(status, bundle);
                        queue.remove(0);
                        Log.i(LogTag, action + "(remove queue)");
                        handleQueue();
                    }
                }
            }

        }else if(status==RESULT_ERROR) {
            if (receiver != null)
                receiver.send(status, bundle);
            closeSocket();
        }

    }

    private void onError(Object... args) {
        closeSocket();
        Bundle bundle=new Bundle();
        bundle.putString(EXTRA_PARAM1,"SocketError");
        returnResult(RESULT_ERROR,bundle);
        }
    private void onIncomingMessage(Object[] args) {
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putString(EXTRA_PARAM1, ACTION_RECEIVE_MESSAGE);
        try {
            String string = String.valueOf(args[0]);
            ChatReceiveMessage chatReceiveMessage = new Gson().fromJson(string.replace("\\\\", "\\"), ChatReceiveMessage.class);
            bundle.putSerializable(EXTRA_PARAM2, chatReceiveMessage);
            intent.putExtras(bundle);
            intent.setAction(ACTION_RECEIVE_MESSAGE);
            queue.add(new ChatCommand(intent));
            handleQueue();
        }catch (Exception e){
            onError(e.getMessage());
        }

    }

    private void requestListDialogs() {

        socket.emit(Keys.CHAT_EVENT_ROOMS);
        }
    private void requestMessages(Intent intent) {

            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            int page=intent.getIntExtra(EXTRA_PARAM2,0);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put(Keys.USER_ID, friend_id);
                    jsonObject.put(Keys.CHAT_KEY_LAST_MSG, page==0?"*":page);
                    socket.emit(Keys.CHAT_EVENT_LOAD, jsonObject);
                } catch (JSONException e) {
                    Bundle bundle=new Bundle();
                    bundle.putString(EXTRA_PARAM1,"requestMessagesError");
                    returnResult(RESULT_ERROR,bundle);
                }
        }
    private void requestSendMessage(Intent intent) {
            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            String text_message=intent.getStringExtra(EXTRA_PARAM2);

            try {
                JSONObject jsonObject=new JSONObject();
                jsonObject.put(Keys.USER_ID,friend_id);
                jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,text_message);
                socket.emit(Keys.CHAT_EVENT_SEND,jsonObject);
            } catch (JSONException e) {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1,"requestSendMessageError");
                returnResult(RESULT_ERROR,bundle);
            }
        }
    private void requestAuthorization(Object[] args) {
            if(!isAuthorized) {
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(Keys.ID, userRepo.load().getId());
                    jsonObject.put(Keys.TOKEN, userRepo.load().getToken());
                    socket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    Bundle bundle=new Bundle();
                    bundle.putString(EXTRA_PARAM1,"requestAuthorizationError");
                    returnResult(RESULT_ERROR,bundle);
                }
            }
        }
    private void requestSendImage(Intent intent) {

        int friend_id=intent.getIntExtra(EXTRA_PARAM2,0);
        File file= (File) intent.getSerializableExtra(EXTRA_PARAM1);
        new UploadChatImageImpl(file,new SimpleSubscriber<JSONObject>(){
            @Override
            public void onNext(JSONObject jsonObject) {
                super.onNext(jsonObject);
                try {
                    jsonObject.put(Keys.USER_ID,friend_id);
                    jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,file.getAbsolutePath());
                    socket.emit(Keys.CHAT_EVENT_SEND,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
            }
        });
        }
    private void requestDeleteDialog(Intent intent) {
            int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put(Keys.USER_ID, friend_id);
                socket.emit(Keys.CHAT_DELETE_DIALOG,jsonObject);
            }catch (Exception e){
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1,"requestDeleteDialog");
                returnResult(RESULT_ERROR,bundle);
            }
        }
    private void requestMarkEstimated(Intent intent) {
        int friend_id=intent.getIntExtra(EXTRA_PARAM1,0);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Keys.USER_ID, friend_id);
            socket.emit(Keys.CHAT_MARK_ESTIMATED,jsonObject);
        }catch (Exception e){
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1,"requestMarkEstimatedError");
            returnResult(RESULT_ERROR,bundle);
        }
    }

    private void receiveListDialogs(Object[] args) {

            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DIALOGS);
                ChatListDialogs chatListDialogs = new GsonBuilder().create().fromJson(String.valueOf(args[0]).replace("\\\\", "\\"), ChatListDialogs.class);
                bundle.putSerializable(EXTRA_PARAM2,chatListDialogs );
                returnResult(RESULT_OK,bundle);
            }catch (Exception e){
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1,"receiveListDialogsError");
                returnResult(RESULT_ERROR,bundle);
            }
        }
    private void receiveMessages(Object[] args) {
            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DIALOG_CONTENT);
                ChatListMessages chatListMessages=new GsonBuilder().create().fromJson(String.valueOf(args[0]).replace("\\\\","\\"), ChatListMessages.class);String.valueOf(args[0]);
                bundle.putSerializable(EXTRA_PARAM2,chatListMessages) ;
                returnResult(RESULT_OK,bundle);
            }catch (Exception e){
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1,"receiveMessagesError");
                returnResult(RESULT_ERROR,bundle);
            }
        }
    private void receiveSendMessageSuccess(Object[] args) {

        ChatReceiveMessage chatReceiveMessage = new Gson().fromJson(args[0].toString().replace("\\\\", "\\"), ChatReceiveMessage.class);
        Bundle bundle=new Bundle();
        if(chatReceiveMessage.getMsg_file().equals("[]"))
            bundle.putString(EXTRA_PARAM1, ACTION_SEND_MESSAGE);
        else
            bundle.putString(EXTRA_PARAM1, ACTION_SEND_IMAGE);
        bundle.putSerializable(EXTRA_PARAM2, chatReceiveMessage);
        returnResult(RESULT_OK,bundle);
        }
    private void receiveAuthorisationSuccess(Object[] args) {
            isAuthorized = true;
            handleQueue();
        }
    private void receiveDeleteDialogSuccess(Object[] args) {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DELETE_DIALOG);
            returnResult(RESULT_OK,bundle);
        }
    private void receiveMarkEstimatedSuccess(Object[] args) {
        Bundle bundle=new Bundle();
        bundle.putString(EXTRA_PARAM1, ACTION_MARK_MESSAGE_READ);
        returnResult(RESULT_OK,bundle);
    }

    private void setReceiver(Intent intent) {
        receiver = intent.getParcelableExtra(RECEIVER);
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM1, ACTION_SET_RECEIVER);
        returnResult(RESULT_OK, bundle);
    }
    private void clearReceiver() {
        receiver=null;
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_PARAM1, ACTION_CLEAR_RECEIVER);
        returnResult(RESULT_OK, bundle);
    }
    private boolean addToQueue(Intent intent) {

        long currTime=System.currentTimeMillis();
        intent.putExtra(KEY_TIME_ADD_TO_QUEUE,currTime);
        if(queue.size()>0){
            Intent prevIntent=queue.get(0).getIntent();
            long lifetime=currTime-prevIntent.getLongExtra(KEY_TIME_ADD_TO_QUEUE,0);
            if(lifetime>1000){
                reloadDialog(intent);
            }else {
                Log.i(LogTag,intent.getAction()+"(add to queue)");
                queue.add(new ChatCommand(intent,(ResultReceiver)intent.getSerializableExtra(RECEIVER)));
            }
            return false;
        }else {
            Log.i(LogTag,intent.getAction()+"(add to queue)");
            queue.add(new ChatCommand(intent,(ResultReceiver)intent.getParcelableExtra(RECEIVER)));
            return true;
        }
    }
    private void reloadDialog(Intent intent) {
        closeSocket();
        Log.i(LogTag, intent.getAction() +"(restart dialog)");
        Bundle bundle=new Bundle();
        bundle.putString(EXTRA_PARAM1, ACTION_RESTART_SWAP);
        returnResult(RESULT_OK,bundle);

    }
    private void showNotification(Bundle bundle) {
        ChatReceiveMessage chatReceiveMessage= (ChatReceiveMessage) bundle.getSerializable(EXTRA_PARAM2);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(chatReceiveMessage.getFrom().getFormattedName())
                        .setContentText(chatReceiveMessage.getText());
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        Intent resultIntent = new Intent(this, MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(000012345, mBuilder.build());

    }

    class UploadChatImageImpl {
        boolean sent=false;

        int MAX_RESOLUTION =1300;
        int imageHeight ;
        int imageWidth ;

        public UploadChatImageImpl(File file, SimpleSubscriber<JSONObject> stringSimpleSubscriber) {
            try {
                sent=false;

                BitmapFactory.Options options = new BitmapFactory.Options();

                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
                imageHeight = options.outHeight;
                imageWidth = options.outWidth;
                boolean needResize=false;
                if(imageHeight> MAX_RESOLUTION){
                    float ratio=(float) MAX_RESOLUTION / (float)imageHeight;
                    imageWidth= (int) (imageWidth*ratio);
                    needResize=true;
                }
                if(imageWidth> MAX_RESOLUTION){
                    float ratio=(float) MAX_RESOLUTION / (float)imageWidth;
                    imageHeight= (int) (imageHeight*ratio);
                    needResize=true;
                }
                if(needResize)
                    bmp=Bitmap.createScaledBitmap(bmp, imageWidth, imageHeight, false);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                UploadChatImage service =ChatImage.createService(UploadChatImage.class);
                RequestBody requestFile =RequestBody.create(MediaType.parse("image/png"),byteArray);
                MultipartBody.Part body =MultipartBody.Part.createFormData("sampleFile", "image.png", requestFile);

                Call<ResponseBody> call = service.upload(body);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call,
                                           Response<ResponseBody> response) {

                        if(response.isSuccessful()) {

                            String body=((Buffer)response.body().source()).toString();
                            String file_name=body
                                    .replace("[text=","")
                                    .replace("]","")
                                    ;


                            JSONObject size=new JSONObject();
                            try {
                                size.put(Keys.WIDTH,imageWidth);
                                size.put(Keys.HEIGHT,imageHeight);
                            } catch (JSONException e) {
                                e.printStackTrace();
                                stringSimpleSubscriber.onError(e);
                            }


                            JSONArray jsonArray=new JSONArray();
                            try {
                                JSONObject image=new JSONObject();
                                image.put(Keys.TYPE,"image");
                                image.put(Keys.URL,file_name);
                                image.put(Keys.INFO,size);
                                jsonArray.put(image);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            JSONObject jsonObject=new JSONObject();
                            try {
                                jsonObject.put(Keys.USER_ID,"");
                                jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,"");
                                jsonObject.put(Keys.CHAT_KEY_MSG_FILE,jsonArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                stringSimpleSubscriber.onError(e);
                            }
                            if(!sent) {
                                sent=true;
                                stringSimpleSubscriber.onNext(jsonObject);
                                stringSimpleSubscriber.onComplete();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        stringSimpleSubscriber.onError(t);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                stringSimpleSubscriber.onError(e);
            }

        }
    }
    interface UploadChatImage {

        @Multipart
        @POST("upload")
        Call<ResponseBody> upload(
                @Part MultipartBody.Part file
        );
    }
    class ChatCommand{
        Intent intent;
        ResultReceiver receiver;

        public ChatCommand(Intent intent) {
            this.intent=intent;
        }
        public ChatCommand(Intent intent,ResultReceiver receiver) {
            this.intent=intent;
            this.receiver=receiver;
        }

        public Intent getIntent() {
            return intent;
        }

        public void setIntent(Intent intent) {
            this.intent = intent;
        }

        public ResultReceiver getReceiver() {
            return receiver;
        }

        public void setReceiver(ResultReceiver receiver) {
            this.receiver = receiver;
        }
    }


}
