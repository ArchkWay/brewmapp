package com.brewmapp.execution.exchange.common;


import com.brewmapp.app.di.qualifier.ChatUrl;
import java.net.URISyntaxException;
import javax.inject.Inject;
import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by Kras on 20.12.2017.
 */

public class ChatClient {
    private Socket socket;

    @Inject
    public ChatClient(@ChatUrl String chatUrl){
        try {
            socket = IO.socket(chatUrl);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket(){return socket;}
}
