package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.LoadEventsTask;
import com.brewmapp.execution.task.LoadNewsTask;
import com.brewmapp.execution.task.LoadSalesTask;
import com.brewmapp.presentation.view.contract.EventsView;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.EventsPresenter;

import java.util.List;

public class EventsPresenterImpl extends BasePresenter<EventsView> implements EventsPresenter {

    private UserRepo userRepo;
    private LoadNewsTask loadNewsTask;
    private LoadEventsTask loadEventsTask;
    private LoadSalesTask loadSalesTask;

    @Inject
    public EventsPresenterImpl(UserRepo userRepo, LoadNewsTask loadNewsTask,
                               LoadEventsTask loadEventsTask, LoadSalesTask loadSalesTask) {
        this.userRepo = userRepo;
        this.loadNewsTask = loadNewsTask;
        this.loadEventsTask = loadEventsTask;
        this.loadSalesTask = loadSalesTask;
    }

    @Override
    public void onDestroy() {
        cancelAllTasks();
    }

    private void cancelAllTasks() {
        loadNewsTask.cancel();
        loadEventsTask.cancel();
        loadSalesTask.cancel();
    }

    @Override
    public void onLoadItems(LoadNewsPackage request) {
        enableControls(false);
        cancelAllTasks();
        switch (request.getMode()) {
            case 0:
                loadEventsTask.execute(request, new NewsSubscriber());
                break;
            case 1:
                loadSalesTask.execute(request, new NewsSubscriber());
                break;
            case 2:
                loadNewsTask.execute(request, new NewsSubscriber());
                break;
        }
    }

    private class NewsSubscriber extends SimpleSubscriber<List<IFlexible>> {
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            showMessage(e.getMessage());
        }

        @Override
        public void onNext(List<IFlexible> iFlexibles) {
            enableControls(true);
            view.appendItems(iFlexibles);
        }
    }
}
