package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;

import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.ChatDialog;
import com.brewmapp.data.entity.ChatListMessages;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.presenter.contract.ChatFragmentPresenter;
import com.brewmapp.presentation.view.contract.ChatFragmentView;
import com.brewmapp.presentation.view.impl.fragment.SimpleFragment.ChatFragment.ListChatDialog;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

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


    @Inject
    public ChatFragmentPresenterImpl(){

    }

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
        mSocket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, args -> view.getActivity().runOnUiThread(()->chatSwap.receiveDialogs(args)));
        mSocket.on(Keys.CHAT_EVENT_LOAD_SUCCESS, args -> view.getActivity().runOnUiThread(()->chatSwap.receiveMessage(args)));

        try {
            friend = (User) intent.getSerializableExtra(RequestCodes.INTENT_EXTRAS);
            mSocket.connect();
        }catch (Exception e){
            view.commonError(e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        mSocket.disconnect();
        mSocket.off();
    }

    class ChatSwap{
        public void authorization() {
            if(!isConnected) {
                isConnected = true;
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put(Keys.ID, friend.getId());
                    jsonObject.put(Keys.TOKEN, friend.getToken());
                    mSocket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                    view.commonError(e.getMessage());
                }
            }
        }

        public void receiveDialogs(Object[] args) {
            ListChatDialog listChatDialog=new GsonBuilder().create().fromJson(String.valueOf(args[0]), ListChatDialog.class);
                try {
                    ChatDialog chatDialog=listChatDialog.get(0);
                    if(listChatDialog.size()!=1){
                        view.commonError("Dialog mast be only one!!!");
                    }else {
                        requestMessages(chatDialog);
                    }
                }catch (Exception e){view.commonError(e.getMessage());}
        }

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

        public void requestDialogs() {
            mSocket.emit(Keys.CHAT_EVENT_ROOMS);
        }

        public void setHandlersErrors() {
            mSocket.on(Socket.EVENT_DISCONNECT,args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Socket.EVENT_CONNECT_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Keys.CHAT_EVENT_AUTH_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
            mSocket.on(Keys.CHAT_EVENT_LOAD_ERROR, args -> view.getActivity().runOnUiThread(()->view.commonError()));
        }

        public void receiveMessage(Object[] args) {
            ChatListMessages listMessages=new GsonBuilder().create().fromJson(String.valueOf(args[0]), ChatListMessages.class);
        }
    }


}
