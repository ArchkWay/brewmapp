package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

import com.brewmapp.presentation.view.contract.ChatFragmentView;

import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by xpusher on 12/13/2017.
 */

public interface ChatFragmentPresenter extends LivePresenter<ChatFragmentView> {
    void connectToChat(Intent activity);
}
