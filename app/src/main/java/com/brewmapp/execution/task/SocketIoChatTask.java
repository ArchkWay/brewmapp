package com.brewmapp.execution.task;

import javax.inject.Inject;

import io.socket.client.Socket;


/**
 * Created by xpusher on 12/15/2017.
 */

public class SocketIoChatTask {

    private Socket socket;

    @Inject SocketIoChatTask(Socket socket){this.socket=socket;}

    public Socket getSocket(){
        return socket;
    }

}
