package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;

import com.brewmapp.app.di.qualifier.ChatUrl;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.ChatDialog;
import com.brewmapp.data.entity.ChatListMessages;
import com.brewmapp.data.entity.ChatMessage;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.presentation.presenter.contract.ChatFragmentPresenter;
import com.brewmapp.presentation.view.contract.ChatFragmentView;
import com.brewmapp.execution.exchange.response.ChatListDialogs;
import com.brewmapp.presentation.view.contract.ProfileEditFragmentView;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatFragment;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatResultReceiver;
import com.brewmapp.presentation.view.impl.fragment.Chat.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miguelbcr.ui.rx_paparazzo2.RxPaparazzo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.ovsa.stub.listener.SelectListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatFragmentPresenterImpl extends BasePresenter<ChatFragmentView> implements ChatFragmentPresenter {


    private Boolean isConnected = false;
    private User friend;
    private UserRepo userRepo;
    private InnerWorker innerWorker;
    private ResultReceiver resultReceiver;
    private String chatUrl;

    @Inject
    public ChatFragmentPresenterImpl(UserRepo userRepo,@ChatUrl String chatUrl){
        this.userRepo = userRepo;
        this.chatUrl = chatUrl;
        innerWorker =new InnerWorker();
    }

    @Override
    public void onAttach(ChatFragmentView chatFragmentView) {
        super.onAttach(chatFragmentView);
    }

    @Override
    public void connectToChat(Intent intent) {
        innerWorker.init(intent);
    }

    @Override
    public void sendMessage(TextView textView) {
        innerWorker.sendMessage(textView.getText().toString());
    }

    @Override
    public void nextPage(Message message) {
        if(message.getmId()>0)
            innerWorker.requestDialogContents(message.getmId());
    }

    @Override
    public SelectListener getSelectListenerPhoto(ChatFragment chatFragment) {
        return (text, position) -> innerWorker.selectPhoto(chatFragment,position);
    }

    @Override
    public void deleteDialog() {
        innerWorker.commandToChatService(ChatService.ACTION_REQUEST_DELETE_DIALOG,friend);
    }

    @Override
    public void onDestroy() {
        innerWorker.commandToChatService(ChatService.ACTION_CLEAR_RECEIVER);
    }



    class InnerWorker {
        //main
        void init(Intent intent) {
            try {
                friend = (User) intent.getSerializableExtra(RequestCodes.INTENT_EXTRAS);
                if(friend ==null)
                    view.commonError();
                else {
                    innerWorker.commandToChatService(ChatService.ACTION_SET_RECEIVER);
                    view.setFriend(friend);
                }
            }catch (Exception e){
                view.commonError(e.getMessage());
            }
        }
        void commandToChatService(String command, Object... args) {

            if(view.getActivity()==null) return;

            Intent intent=new Intent(command,null,view.getActivity(), ChatService.class);
            //prepare arguments
            try {
                switch (command){
                    case ChatService.ACTION_SET_RECEIVER:
                        resultReceiver=new ChatResultReceiver(new Handler(view.getActivity().getMainLooper()),new SimpleSubscriber<Bundle>(){
                            @Override
                            public void onNext(Bundle bundle) {
                                super.onNext(bundle);
                                innerWorker.handleResult(bundle);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError();
                            }
                        });
                        intent.putExtra(ChatService.RECEIVER,resultReceiver);
                        break;
                    case ChatService.ACTION_REQUEST_DIALOGS:
                    case ChatService.ACTION_REQUEST_DELETE_DIALOG:
                        intent.putExtra(ChatService.EXTRA_PARAM1,((User)args[0]).getId());
                        break;
                    case ChatService.ACTION_REQUEST_DIALOG_CONTENT:
                        intent.putExtra(ChatService.EXTRA_PARAM1,((User)args[0]).getId());
                        intent.putExtra(ChatService.EXTRA_PARAM2,((int)args[1]));
                        break;
                    case ChatService.ACTION_SEND_MESSAGE:
                        intent.putExtra(ChatService.EXTRA_PARAM1,((User)args[0]).getId());
                        intent.putExtra(ChatService.EXTRA_PARAM2,(String)args[1]);
                        break;
                    case ChatService.ACTION_SEND_IMAGE:
                        intent.putExtra(ChatService.EXTRA_PARAM1,(File)args[0]);
                        intent.putExtra(ChatService.EXTRA_PARAM2,((User)args[1]).getId());
                        break;
                    case ChatService.ACTION_CLEAR_RECEIVER:
                        resultReceiver=null;
                        break;
                }
            }catch (Exception e){
                view.commonError(e.getMessage());
                return;
            }

            view.getActivity().startService(intent);

        }
        void handleResult(Bundle resultData) {
            String action=resultData.getString(ChatService.EXTRA_PARAM1);
            if(action!=null) {
                switch (action) {
                    case ChatService.ACTION_SET_RECEIVER: {
                        commandToChatService(ChatService.ACTION_REQUEST_DIALOGS, friend);
                    }break;
                    case ChatService.ACTION_RELOAD_DIALOG:
                    case ChatService.ACTION_REQUEST_DELETE_DIALOG:{
                        view.clearMessages();
                        commandToChatService(ChatService.ACTION_REQUEST_DIALOGS, friend);
                    }break;
                    case ChatService.ACTION_REQUEST_DIALOGS: {
                        ChatListDialogs chatListDialogs = (ChatListDialogs) resultData.getSerializable(ChatService.EXTRA_PARAM2);
                        for (ChatDialog chatDialog:chatListDialogs)
                            if(chatDialog.getUser().getId()==friend.getId())
                                requestDialogContents(0);
                    }break;
                    case ChatService.ACTION_REQUEST_DIALOG_CONTENT: {
                        String string = resultData.getString(ChatService.EXTRA_PARAM2);
                        showDialogContent(string);
                    }break;
                    case ChatService.ACTION_RECEIVE_MESSAGE: {
                        String string = resultData.getString(ChatService.EXTRA_PARAM2);
                        ChatReceiveMessage chatReceiveMessage = new Gson().fromJson(string.replace("\\\\", "\\"), ChatReceiveMessage.class);
                        if (Keys.CHAT_DIR_INPUT.equals(chatReceiveMessage.getDir())) {
                            String url=null;
                            int imageHeight=0;
                            int imageWidth=0;
                            try {
                                JSONArray jsonArray=new JSONArray(chatReceiveMessage.getMsg_file());
                                if(jsonArray.length()>0) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                                    url =chatUrl + "/files/" + jsonObject.getString("url");
                                    imageHeight=jsonObject.getJSONObject("info").getInt("height");
                                    imageWidth=jsonObject.getJSONObject("info").getInt("width");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Message message = new Message
                                    .Builder(Message.TYPE_MESSAGE_INPUT)
                                    .message(chatReceiveMessage.getText())
                                    .username(chatReceiveMessage.getFrom().getFormattedName())
                                    .setImage(url)
                                    .setImageHeight(imageHeight)
                                    .setImageWidth(imageWidth)
                                    .build();
                            List<Message> list = new ArrayList<>();
                            list.add(message);
                            view.addMessages(list, false);
                        }

                    }break;
                    case ChatService.ACTION_SEND_MESSAGE:{
                        String string = resultData.getString(ChatService.EXTRA_PARAM2);
                        ChatReceiveMessage chatReceiveMessage = new Gson().fromJson(string.replace("\\\\", "\\"), ChatReceiveMessage.class);
                        view.setStatusMessage(chatReceiveMessage);

                    }break;
                }
            }
        }

        void requestDialogContents(int idLastMessage) {
            commandToChatService(ChatService.ACTION_REQUEST_DIALOG_CONTENT, friend,idLastMessage);
        }
        void showDialogContent(String string) {

            ChatListMessages listMessages=new GsonBuilder().create().fromJson(string.replace("\\\\","\\"), ChatListMessages.class);
            List<Message> list=new ArrayList<>();

            for (ChatMessage chatMessage:listMessages.getData()) {
                String image=null;
                int imageHeight=0;
                int imageWidth=0;
                try {
                    JSONArray images=new JSONArray(chatMessage.getMsg_file());
                    if(images.length()>0) {
                        image = chatUrl + "/files/" + images.getJSONObject(0).getString("url");
                        imageHeight=images.getJSONObject(0).getJSONObject("info").getInt("height");
                        imageWidth=images.getJSONObject(0).getJSONObject("info").getInt("width");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                switch (chatMessage.getDir()){
                    case Keys.CHAT_DIR_INPUT:
                        list.add(
                                new Message.Builder(Message.TYPE_MESSAGE_INPUT)
                                        .username(getUnicodeString(chatMessage.getUser().getFormattedName()))
                                        .message(chatMessage.getText())
                                        .setId(chatMessage.getId())
                                        .setImage(image)
                                        .setImageHeight(imageHeight)
                                        .setImageWidth(imageWidth)
                                        .build()
                        );
                        break;
                    case Keys.CHAT_DIR_OUTPUT:
                        list.add(
                                new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                                        .username(getUnicodeString(userRepo.load().getFormattedName()))
                                        .message(chatMessage.getText())
                                        .setId(chatMessage.getId())
                                        .setImage(image)
                                        .setImageHeight(imageHeight)
                                        .setImageWidth(imageWidth)
                                        .build()
                        );
                        break;
                }
            }
            view.addMessages(list,true);
        }
        String escapeUnicodeText(String input) {

            StringBuilder b = new StringBuilder(input.length());

            java.util.Formatter f = new java.util.Formatter(b);

            for (char c : input.toCharArray()) {
                if (c < 128) {
                    b.append(c);
                } else {
                    f.format("\\u%04x", (int) c);
                }
            }

            return b.toString();
        }
        String getUnicodeString(String myString) {
            String text = "";
            try {

                byte[] utf8Bytes = myString.getBytes("UTF8");
                text = new String(utf8Bytes, "UTF8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return text;
        }
        void selectPhoto(ChatFragment baseFragment, int position) {
            switch (position) {
                case 0:
                    RxPaparazzo.single(baseFragment.getActivity())
                            .usingGallery()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if (response.resultCode() != RESULT_OK) return;
                                sendImage(response.data().getFile());
                            });

                    break;
                case 1:
                    RxPaparazzo.single(baseFragment.getActivity())
                            .usingCamera()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(response -> {
                                if (response.resultCode() != RESULT_OK) return;
                                sendImage(response.data().getFile());
                            });

                    break;
                case 2:
                    ((ProfileEditFragmentView)baseFragment).selectedPhoto(null);
                    break;
            }

        }
        void sendImage(File file) {
            if(file!=null&&file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds=true;
                BitmapFactory.decodeFile(file.getAbsolutePath(),options);

                List<Message> list=new ArrayList<>();
                list.add(
                        new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                                .username(userRepo.load().getFormattedName())
                                .message(file.getAbsolutePath())
                                .stateSending(true)
                                .setImage(file.getAbsolutePath())
                                .setImageHeight(options.outHeight)
                                .setImageWidth(options.outWidth)
                                .build()
                );
                view.addMessages(list,false);
                commandToChatService(ChatService.ACTION_SEND_IMAGE, file,friend);
            }
        }
        void sendMessage(String text_send) {
            if(text_send.length()>0) {
                List<Message> list=new ArrayList<>();
                list.add(
                        new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                                .username(userRepo.load().getFormattedName())
                                .message(text_send)
                                .stateSending(true)
                                .build()
                );
                view.addMessages(list,false);
                innerWorker.commandToChatService(ChatService.ACTION_SEND_MESSAGE,friend,innerWorker.escapeUnicodeText(text_send));
            }

        }
    }

}
