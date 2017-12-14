package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.widget.TextView;

import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.ChatDialog;
import com.brewmapp.data.entity.ChatListMessages;
import com.brewmapp.data.entity.ChatMessage;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ChatFragmentPresenter;
import com.brewmapp.presentation.view.contract.ChatFragmentView;
import com.brewmapp.execution.exchange.response.ChatListDialogs;
import com.brewmapp.presentation.view.impl.fragment.Chat.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;

import io.socket.client.Socket;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by xpusher on 12/13/2017.
 */

public class ChatFragmentPresenterImpl extends BasePresenter<ChatFragmentView> implements ChatFragmentPresenter {

    private Socket mSocket;
    private Boolean isConnected = false;
    private User friend;
    private ChatSwap chatSwap=new ChatSwap();
    private UserRepo userRepo;

    @Inject
    public ChatFragmentPresenterImpl(UserRepo userRepo){this.userRepo = userRepo;}

    @Override
    public void onAttach(ChatFragmentView chatFragmentView) {
        super.onAttach(chatFragmentView);
    }

    @Override
    public void connectToChat(Intent intent) {

        mSocket=BeerMap.getAppComponent().getSocket();

        chatSwap.setHandlersErrors();

        mSocket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, args -> view.getActivity().runOnUiThread(()->chatSwap.requestDialogs()));
        mSocket.on(Socket.EVENT_CONNECT,args ->  view.getActivity().runOnUiThread(()->chatSwap.authorization()));
        mSocket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, args -> view.getActivity().runOnUiThread(()->chatSwap.loadDialogs(args)));
        mSocket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, args -> view.getActivity().runOnUiThread(()->chatSwap.loadMessages(args)));
        mSocket.on(Keys.CHAT_EVENT_SEND_SUCCESS, args -> view.getActivity().runOnUiThread(()->view.sendSuccess()));
        mSocket.on(Socket.EVENT_MESSAGE, args -> view.getActivity().runOnUiThread(()->chatSwap.receive(args)));


        try {
            friend = (User) intent.getSerializableExtra(RequestCodes.INTENT_EXTRAS);
            mSocket.connect();
        }catch (Exception e){
            view.commonError(e.getMessage());
        }

    }

    @Override
    public void send(TextView editText) {
        String string_send=editText.getText().toString().trim();

        view.addMessage(
                new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                        .username(userRepo.load().getFormattedName())
                        .message(string_send)
                        .build()
                    );

        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put(Keys.USER_ID,friend.getId());
            jsonObject.put(Keys.CHAT_KEY_MSG_TEXT,chatSwap.escapeUnicodeText(string_send));
            mSocket.emit(Keys.CHAT_EVENT_SEND,jsonObject);
        } catch (JSONException e) {
            view.commonError(e.getMessage());
        }

    }

    @Override
    public void onDestroy() {chatSwap.disconnect();}

    class ChatSwap{
        //Start
        public void authorization() {
            if(!isConnected) {
                isConnected = true;
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(Keys.ID, userRepo.load().getId());
                    jsonObject.put(Keys.TOKEN, userRepo.load().getToken());
                    mSocket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.commonError(e.getMessage());
                }
            }
        }
        //Dialogs
        public void requestDialogs() {
            mSocket.emit(Keys.CHAT_EVENT_ROOMS);
        }
        public void loadDialogs(Object[] args) {
            ChatListDialogs chatListDialogs =new GsonBuilder().create().fromJson(String.valueOf(args[0]).replace("\\\\","\\"), ChatListDialogs.class);
            for(ChatDialog chatDialog:chatListDialogs )
                if(chatDialog.getUser().getId()==friend.getId()){
                    requestMessages(chatDialog);
                }
        }
        //Messages
        private void requestMessages(ChatDialog chatDialog) {
            JSONObject jsonObject=new JSONObject();
            try {
                jsonObject.put(Keys.USER_ID,chatDialog.getUser().getId());
                jsonObject.put(Keys.CHAT_KEY_LAST_MSG,"*");
                mSocket.emit(Keys.CHAT_EVENT_LOAD,jsonObject);
            } catch (JSONException e) {
                view.commonError(e.getMessage());
            }

        }
        public void loadMessages(Object[] args) {
            try {
                ChatListMessages listMessages=new GsonBuilder().create().fromJson(String.valueOf(args[0]).replace("\\\\","\\"), ChatListMessages.class);
                for (ChatMessage chatMessage:listMessages.getData()) {
                    switch (chatMessage.getDir()){
                        case Keys.CHAT_DIR_INPUT:
                            view.insertMessage(
                                    new Message.Builder(Message.TYPE_MESSAGE_INPUT)
                                            .username(chatSwap.getUnicodeString(chatMessage.getUser().getFormattedName()))
                                            .message(chatMessage.getText())
                                            .build()
                            );
                            break;
                        case Keys.CHAT_DIR_OUTPUT:
                            view.insertMessage(
                                    new Message.Builder(Message.TYPE_MESSAGE_OUTPUT)
                                            .username(chatSwap.getUnicodeString(userRepo.load().getFormattedName()))
                                            .message(chatMessage.getText())
                                            .build()
                            );
                            break;
                    }
                }

            }catch (Exception e){view.commonError(e.getMessage());}
        }
        //Utils
        public String escapeUnicodeText(String input) {

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
        public String getUnicodeString(String myString) {
            String text = "";
            try {

                byte[] utf8Bytes = myString.getBytes("UTF8");
                text = new String(utf8Bytes, "UTF8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return text;
        }
        //Message
        public void receive(Object[] args) {
            ChatReceiveMessage chatReceiveMessage=new Gson().fromJson(String.valueOf(args[0]).replace("\\\\","\\"), ChatReceiveMessage.class);
            if(Keys.CHAT_DIR_INPUT.equals(chatReceiveMessage.getDir())) {
                Message message=new Message
                        .Builder(Message.TYPE_MESSAGE_INPUT)
                        .message(chatReceiveMessage.getText())
                        .username(chatReceiveMessage.getFrom().getFormattedName())
                        .build();
                view.getActivity().runOnUiThread(() -> view.addMessage(message));
            }
        }
        //Errors
        public void setHandlersErrors() {
            mSocket.on(Socket.EVENT_DISCONNECT,args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Keys.CHAT_EVENT_SEND_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
        }
        //Disconnect
        public void disconnect() {
            mSocket.disconnect();
            mSocket.off();
        }
    }

}
