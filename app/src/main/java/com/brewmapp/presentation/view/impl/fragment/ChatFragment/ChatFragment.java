package com.brewmapp.presentation.view.impl.fragment.ChatFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by Kras on 13.12.2017.
 */

public class ChatFragment extends Fragment {

    private Socket mSocket;
    private Boolean isConnected = false;
    private User frend;

    private OnFragmentInteractionListener mListener;

    public ChatFragment(){
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BeerMap app = (BeerMap) getActivity().getApplication();
        mSocket = app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on(Keys.CHAT_EVENT_AUTH_SUCCESS, onAuthSuccess);
        mSocket.on(Keys.CHAT_EVENT_AUTH_ERROR, onConnectError);
        mSocket.on(Keys.CHAT_EVENT_ROOMS_SUCCESS, onRoomSuccess);

        try {
            frend= (User) getActivity().getIntent().getSerializableExtra(RequestCodes.INTENT_EXTRAS);
            mSocket.connect();
        }catch (Exception e){
            mListener.commonError(e.getMessage());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();

        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off(Keys.CHAT_EVENT_AUTH_SUCCESS, onAuthSuccess);
        mSocket.off(Keys.CHAT_EVENT_AUTH_ERROR, onConnectError);
        mSocket.off(Keys.CHAT_EVENT_ROOMS_SUCCESS, onRoomSuccess);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RestoEditFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!isConnected) {
                        isConnected = true;
                        try {
                            JSONObject jsonObject=new JSONObject();
                            jsonObject.put(Keys.ID,frend.getId());
                            jsonObject.put(Keys.TOKEN,frend.getToken());
                            mSocket.emit(Keys.CHAT_EVENT_AUTH,jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            mListener.commonError(e.getMessage());
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onAuthSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSocket.emit(Keys.CHAT_EVENT_ROOMS);
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isConnected = false;
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                }
            });
        }
    };


    private Emitter.Listener onRoomSuccess = new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void commonError(String... message);
    }

}
