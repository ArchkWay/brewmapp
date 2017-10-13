package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.entity.Sale;
import com.brewmapp.presentation.view.contract.SaleDetailsView;

import ru.frosteye.ovsa.data.storage.ActiveBox;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.SaleDetailsPresenter;

public class SaleDetailsPresenterImpl extends BasePresenter<SaleDetailsView> implements SaleDetailsPresenter {

    private ActiveBox activeBox;

    @Inject
    public SaleDetailsPresenterImpl(ActiveBox activeBox) {
        this.activeBox=activeBox;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onAttach(SaleDetailsView saleDetailsView) {
        super.onAttach(saleDetailsView);
        view.showSaleDetails(activeBox.getActive(Sale.class));
    }
}
