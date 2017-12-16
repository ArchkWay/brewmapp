package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.widget.TextView;

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
import com.brewmapp.presentation.view.impl.fragment.Chat.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import io.socket.client.Socket;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatFragmentPresenterImpl extends BasePresenter<ChatFragmentView> implements ChatFragmentPresenter {

    //private Socket mSocket;
    private Boolean isConnected = false;
    private User friend;
    private UserRepo userRepo;
    private InnerThis innerThis;
    private ResultReceiver resultReceiver;

    @Inject
    public ChatFragmentPresenterImpl(UserRepo userRepo, Socket socket){
        this.userRepo = userRepo;
        //this.mSocket=socket;
        innerThis=new InnerThis();
    }

    @Override
    public void onAttach(ChatFragmentView chatFragmentView) {
        super.onAttach(chatFragmentView);
    }

    @Override
    public void connectToChat(Intent intent) {
        innerThis.init(intent);
    }

    @Override
    public void send(TextView textView) {
        innerThis.commandToChatService(ChatService.ACTION_REQUEST_DIALOGS,friend);
        String text_send=textView.getText().toString();
        if(text_send.length()>0) {
            view.addMessage(
                    new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                            .username(userRepo.load().getFormattedName())
                            .message(text_send)
                            .build()
            );
            innerThis.send(text_send);
        }
    }

    @Override
    public void onDestroy() {view.getActivity().stopService(new Intent(view.getActivity(),ChatService.class));}

    class InnerThis{

        public void init(Intent intent) {
            try {
                friend = (User) intent.getSerializableExtra(RequestCodes.INTENT_EXTRAS);
                innerThis.commandToChatService(ChatService.ACTION_AUTHORIZATION);
            }catch (Exception e){
                view.commonError(e.getMessage());
            }
        }

        void commandToChatService(String command, Object... args) {
            Intent intent=new Intent(command,null,view.getActivity(), ChatService.class);
            //prepare arguments
            try {
                switch (command){
                    case ChatService.ACTION_REQUEST_DIALOGS:
                    case ChatService.ACTION_REQUEST_MESSAGES:
                        intent.putExtra(ChatService.EXTRA_PARAM1,((User)args[0]).getId());
                        break;
                    case ChatService.ACTION_AUTHORIZATION:
                        resultReceiver=new ChatResultReceiver(new Handler(view.getActivity().getMainLooper()));
                        intent.putExtra(ChatService.RECEIVER,resultReceiver);
                        break;
                    case ChatService.ACTION_SEND_MESSAGE:
                        intent.putExtra(ChatService.EXTRA_PARAM1,((User)args[0]).getId());
                        intent.putExtra(ChatService.EXTRA_PARAM2,(String)args[1]);
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
            switch (action){
                case ChatService.ACTION_AUTHORIZATION: {
                    commandToChatService(ChatService.ACTION_REQUEST_DIALOGS, friend);
                }break;
                case ChatService.ACTION_REQUEST_DIALOGS: {
                    String string = resultData.getString(ChatService.EXTRA_PARAM2);
                    ChatListDialogs chatListDialogs = new GsonBuilder().create().fromJson(string.replace("\\\\", "\\"), ChatListDialogs.class);
                    commandToChatService(ChatService.ACTION_REQUEST_MESSAGES, chatListDialogs.get(0).getUser());
                }break;
                case ChatService.ACTION_REQUEST_MESSAGES: {
                    String string = resultData.getString(ChatService.EXTRA_PARAM2);
                    loadMessages(string);
                }break;
            }
        }
        void loadMessages(String string) {
            ChatListMessages listMessages=new GsonBuilder().create().fromJson(string.replace("\\\\","\\"), ChatListMessages.class);
            for (ChatMessage chatMessage:listMessages.getData()) {
                switch (chatMessage.getDir()){
                    case Keys.CHAT_DIR_INPUT:
                        view.insertMessage(
                                new Message.Builder(Message.TYPE_MESSAGE_INPUT)
                                        .username(getUnicodeString(chatMessage.getUser().getFormattedName()))
                                        .message(chatMessage.getText())
                                        .build()
                        );
                        break;
                    case Keys.CHAT_DIR_OUTPUT:
                        view.insertMessage(
                                new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                                        .username(getUnicodeString(userRepo.load().getFormattedName()))
                                        .message(chatMessage.getText())
                                        .build()
                        );
                        break;
                }
            }
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
        public void send(String string_send) {
            commandToChatService(ChatService.ACTION_SEND_MESSAGE,friend,escapeUnicodeText(string_send));
        }


    }
    class ChatResultReceiver extends ResultReceiver {

        public ChatResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            switch (resultCode){
                case ChatService.STATUS_COMPLEATE:{
                    innerThis.handleResult(resultData);
                }break;
                case ChatService.STATUS_ERROR:{
                    view.commonError();
                }break;

            }
        }
    }

}
