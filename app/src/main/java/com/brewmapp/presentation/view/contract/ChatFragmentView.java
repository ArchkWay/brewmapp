package com.brewmapp.presentation.view.contract;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.impl.fragment.Chat.Message;

import java.util.List;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 12/13/2017.
 */

public interface ChatFragmentView extends BasicView {
    void commonError(String... message);

    FragmentActivity getActivity();

    void addMessages(List<Message> messages, boolean insert);

    void setFriend(User friend);
}
