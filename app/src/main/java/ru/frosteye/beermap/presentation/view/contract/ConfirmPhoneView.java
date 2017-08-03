package ru.frosteye.beermap.presentation.view.contract;

import ru.frosteye.ovsa.presentation.view.BasicView;

public interface ConfirmPhoneView extends BasicView {
    void proceed();
    void die();
    void startCounter();
}
