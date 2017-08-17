package ru.frosteye.beermap.execution.task.base;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by oleg on 16.08.17.
 */

public abstract class LoaderSubscriber<R> extends SimpleSubscriber<R> {

    private BasicView view;
    private int counter = 0;

    public LoaderSubscriber(BasicView view) {
        this.view = view;
    }

    public LoaderSubscriber() {
    }

    @Override
    public void onError(Throwable e) {
        if(view != null) {
            view.enableControls(true, BasicView.CONTROLS_CODE_ERROR);
            view.showMessage(e.getMessage(), BasicView.CONTROLS_CODE_ERROR);
        }
    };

    @Override
    public final void onNext(R r) {
        onResult(r);
        if(counter == 0) {
            counter++;
            if(view != null) {
                view.enableControls(true, BasicView.CONTROLS_CODE_PROGRESS);
            }
            onFirst(r);
        } else {
            if(view != null) {
                view.enableControls(true, BasicView.CONTROLS_CODE_SUCCESS);
            }
            onSecond(r);
        }
    }

    public void onResult(R result) {};

    public void onFirst(R result) {};

    public void onSecond(R result) {};
}
