package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.presenter.contract.FilterMapPresenter;
import com.brewmapp.presentation.view.contract.FilterMapView;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilerMapPresenterImpl extends BasePresenter<FilterMapView> implements FilterMapPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;

    @Inject
    public FilerMapPresenterImpl(Context context, UiSettingRepo uiSettingRepo) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
    }

    @Override
    public void onAttach(FilterMapView filterMapView) {
        super.onAttach(filterMapView);
        Paper.init(context);
        if (Paper.book().read("restoCategoryList") == null) {
            Paper.book().write("restoCategoryList", FilterRestoField.createDefault(context));
        }
        if (Paper.book().read("beerCategoryList") == null) {
            Paper.book().write("beerCategoryList", FilterBeerField.createDefault(context));
        }
        if (uiSettingRepo.getnActiveFragment() == 0 || uiSettingRepo.getnActiveFragment() == -1) {
            view.showRestoFilters(Paper.book().read("restoCategoryList"));
        } else {
            view.showBeerFilters(Paper.book().read("beerCategoryList"));
        }
        filterMapView.setTabActive(uiSettingRepo.getnActiveTabEventFragment());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void storeTabActive(int position) {
        uiSettingRepo.setnActiveTabEventFragment(position);
    }
}
