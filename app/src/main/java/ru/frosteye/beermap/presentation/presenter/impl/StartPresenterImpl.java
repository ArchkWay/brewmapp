package ru.frosteye.beermap.presentation.presenter.impl;

import javax.inject.Inject;

import ru.frosteye.beermap.data.db.contract.UserRepo;
import ru.frosteye.beermap.presentation.view.contract.StartView;

import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import ru.frosteye.beermap.presentation.presenter.contract.StartPresenter;

public class StartPresenterImpl extends BasePresenter<StartView> implements StartPresenter {

    private UserRepo userRepo;

    @Inject
    public StartPresenterImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void onAttach(StartView startView) {
        super.onAttach(startView);
        if(userRepo.load() != null) {
            view.proceed();
        }
    }

    @Override
    public void onDestroy() {

    }
}
