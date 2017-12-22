package com.brewmapp.execution.services;

import android.app.Notification;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.ResultReceiver;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.execution.exchange.common.ChatImage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.services.base.BaseService;

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



    private InnerWorker innerWorker =new InnerWorker();

    public static final String ACTION_SEND_MESSAGE = "com.brewmapp.execution.services.action.ACTION_SEND_MESSAGE";
    public static final String ACTION_SEND_IMAGE = "com.brewmapp.execution.services.action.ACTION_SEND_IMAGE";
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
                innerWorker.onDisconnect();
                break;
            default: {
                boolean HandleQueueNow=innerWorker.queue.size()==0;
                innerWorker.addQueue(innerWorker.getAction(intent), intent);
                if(innerWorker.isAuthorized)
                    if(HandleQueueNow)
                        innerWorker.handleQueue();
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        innerWorker.onDisconnect();
    }

    class InnerWorker {
        ResultReceiver receiver;
        boolean isAuthorized =false;
        List<ActionQueue> queue= new ArrayList<>();
        void initSocket() {
            socket.on(Socket.EVENT_DISCONNECT,args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Socket.EVENT_CONNECT_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> returnResult(RESULT_ERROR,Bundle.EMPTY));
            //Swap
            socket.on(Socket.EVENT_CONNECT, this::requestAuthorization);
            socket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, this::receiveAuthorisationSuccess);
            socket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, this::receiveListDialogs);
            socket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, this::receiveMessages);
            socket.on(Keys.CHAT_EVENT_SEND_SUCCESS, this::receiveSendMessageSuccess);
            socket.on(Keys.CHAT_EVENT_SEND_ERROR, this::onError);
            socket.on(Socket.EVENT_MESSAGE, this::receiveMessage);

            socket.connect();
        }
        void handleQueue() {

            if(queue.size()>0) {
                String action=queue.get(0).getAction();
                Intent intent=queue.get(0).getIntent();
                switch (action) {
                    case ACTION_SEND_IMAGE:
                        innerWorker.requestSendImage(intent);
                        break;
                    case ACTION_SEND_MESSAGE:
                        innerWorker.requestSendMessage(intent);
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

        void requestListDialogs() {
            socket.emit(Keys.CHAT_EVENT_ROOMS);
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
        void requestSendMessage(Intent intent) {
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
        void requestAuthorization(Object[] args) {
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
        void requestSendImage(Intent intent) {
            int friend_id=intent.getIntExtra(EXTRA_PARAM2,0);
            File file= (File) intent.getSerializableExtra(EXTRA_PARAM1);
            new UploadChatImageImpl(file,new SimpleSubscriber<JSONObject>(){
                @Override
                public void onNext(JSONObject jsonObject) {
                    super.onNext(jsonObject);
                    try {
                        jsonObject.put(Keys.USER_ID,friend_id);
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

            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1,ACTION_SEND_IMAGE);
            returnResult(RESULT_OK,bundle);
        }

        void receiveListDialogs(Object[] args) {
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
        void receiveMessages(Object[] args) {
            try {
                Bundle bundle=new Bundle();
                bundle.putString(EXTRA_PARAM1, ACTION_REQUEST_DIALOG_CONTENT);
                bundle.putString(EXTRA_PARAM2,String.valueOf(args[0]));
                returnResult(RESULT_OK,bundle);
            }catch (Exception e){
                returnResult(RESULT_ERROR,Bundle.EMPTY);
            }
        }
        void receiveSendMessageSuccess(Object[] args) {
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1, ACTION_SEND_MESSAGE);
            bundle.putString(EXTRA_PARAM2, ((JSONObject) args[0]).toString());
            returnResult(RESULT_OK,bundle);
        }
        void receiveAuthorisationSuccess(Object[] args) {
            isAuthorized = true;
            Intent intent=new Intent();
            Bundle bundle=new Bundle();
            bundle.putString(EXTRA_PARAM1,ACTION_INIT_DIALOG);
            intent.putExtras(bundle);
            addQueue(ACTION_INIT_DIALOG,intent);
            handleQueue();
        }

        void onError(Object[] args) {
            returnResult(RESULT_ERROR,Bundle.EMPTY);
        }
        void onDisconnect() {
            isAuthorized = false;
            socket.off();
            socket.disconnect();
            socket.close();
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
        void removeQueue() {
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
        void addQueue(String action, Intent intent) {
            queue.add(new ActionQueue(action,intent));
        }

    }
    class ActionQueue {
        private String action;
        private Intent intent;

        public ActionQueue(String action, Intent intent) {
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
    class UploadChatImageImpl {

        int MAX_PHOTO=1300;

        int imageHeight ;
        int imageWidth ;

        public UploadChatImageImpl(File file, SimpleSubscriber<JSONObject> stringSimpleSubscriber) {
            try {


                BitmapFactory.Options options = new BitmapFactory.Options();

                Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath(),options);
                imageHeight = options.outHeight;
                imageWidth = options.outWidth;
                boolean needResize=false;
                if(imageHeight>MAX_PHOTO){
                    float ratio=(float) MAX_PHOTO / (float)imageHeight;
                    imageWidth= (int) (imageWidth*ratio);
                    needResize=true;
                }
                if(imageWidth>MAX_PHOTO){
                    float ratio=(float) MAX_PHOTO / (float)imageWidth;
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
                                jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,"IMAGE");
                                jsonObject.put(Keys.CHAT_KEY_MSG_FILE,jsonArray);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                stringSimpleSubscriber.onError(e);
                            }
                            stringSimpleSubscriber.onNext(jsonObject);
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


}
