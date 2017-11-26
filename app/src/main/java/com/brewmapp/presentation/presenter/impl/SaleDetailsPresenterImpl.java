package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.Sale;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.presentation.view.contract.SaleDetailsView;

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;

public class SaleDetailsPresenterImpl extends BasePresenter<SaleDetailsView> implements SaleDetailsPresenter {

    private ActiveBox activeBox;
    private LikeTask likeTask;


    @Inject
    public SaleDetailsPresenterImpl(ActiveBox activeBox,LikeTask likeTask) {
        this.activeBox=activeBox;
        this.likeTask=likeTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(SaleDetailsView saleDetailsView) {
        super.onAttach(saleDetailsView);
        view.setModel(activeBox.getActive(Sale.class));
    }

}
