package com.brewmapp.presentation.presenter.impl;

import com.brewmapp.presentation.presenter.contract.AssessmentsPresenter;
import com.brewmapp.presentation.view.contract.AssessmentsView;

import javax.inject.Inject;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by Kras on 20.10.2017.
 */

public class AssessmentsPresenterImpl extends BasePresenter<AssessmentsView>  implements AssessmentsPresenter{

    @Inject
    public AssessmentsPresenterImpl(){}

    @Override
    public void onAttach(AssessmentsView assessmentsView) {
        super.onAttach(assessmentsView);
    }

    @Override
    public void onDestroy() {

    }
}
