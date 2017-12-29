package com.brewmapp.presentation.presenter.contract;

import android.content.Intent;

import com.brewmapp.data.entity.Photo;
import com.brewmapp.presentation.view.contract.RestoDetailView;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;

import java.util.ArrayList;
import java.util.List;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 26.10.2017.
 */

public interface RestoDetailPresenter extends LivePresenter<RestoDetailView> {


    void changeSubscription();

    void startAddReviewRestoActivity(RestoDetailActivity restoDetailActivity);

    void parseIntent(Intent intent);

    void startShowEventFragment(RestoDetailActivity restoDetailActivity, int tab);

    void restoreSetting();

    void startShowMenu(RestoDetailActivity restoDetailActivity);

    void startShowPhoto(RestoDetailActivity restoDetailActivity, ArrayList<String> photosResto);

    void refreshContent(int mode);

    void clickLikeDislike(int type_like);

    void clickFav();

    void startMapFragment(RestoDetailActivity restoDetailActivity);

    void loadAllPhoto(SimpleSubscriber<List<Photo>> slider);

    void startChat(String user_id);
}
