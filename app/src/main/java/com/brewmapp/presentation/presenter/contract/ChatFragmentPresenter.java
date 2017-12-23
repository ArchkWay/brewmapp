package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.widget.TextView;

import com.brewmapp.presentation.view.contract.ChatFragmentView;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatFragment;
import com.brewmapp.presentation.view.impl.fragment.Chat.Message;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.listener.SelectListener;

/**
 * Created by xpusher on 12/13/2017.
 */

public interface ChatFragmentPresenter extends LivePresenter<ChatFragmentView> {
    void connectToChat(Intent activity);

        void sendMessage(TextView editText);

    void nextPage(Message message);

    SelectListener getSelectListenerPhoto(ChatFragment chatFragment);

    void deleteDialog();
}
