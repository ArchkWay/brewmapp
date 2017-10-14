package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Sale;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface SaleDetailsView extends BasicView {
    void showSaleDetails(Sale active);


}
