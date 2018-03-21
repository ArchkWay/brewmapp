package com.brewmapp.presentation.presenter.contract;

import android.app.Activity;
import android.content.Intent;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.RestoDetail;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;

import java.io.File;
import java.util.List;

import ru.frosteye.ovsa.execution.executor.Callback;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailPresenter extends LivePresenter<RestoDetailView> {


    void changeSubscription();

    boolean parseIntent(Intent intent);

    void startShowEventFragment(RestoDetailActivity restoDetailActivity, int tab);

    void restoreSetting();

    void startShowMenu(RestoDetailActivity restoDetailActivity);

    void refreshContent(int mode);

    void clickLikeDislike(int type_like);

    void clickFav();

    void startMapFragment(RestoDetailActivity restoDetailActivity);

    void loadAllPhoto(SimpleSubscriber<List<Photo>> slider);

    void startChat(Activity activity, String user_id);

    RestoDetail getRestoDetails();

    void uploadPhoto(File file,Callback<Integer> callback);

    void sendResultReceiver(int actionStopBlur);
}
