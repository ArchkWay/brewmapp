package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.task.DeleteNewsTask;
import com.brewmapp.execution.task.LikeTask;
import com.brewmapp.execution.task.LoadEventsTask;
import com.brewmapp.execution.task.LoadNewsTask;
import com.brewmapp.execution.task.LoadSalesTask;
import com.brewmapp.presentation.view.contract.EventsView;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;

import java.util.List;

public class EventsPresenterImpl extends BasePresenter<EventsView> implements EventsPresenter {

    private UserRepo userRepo;
    private LoadNewsTask loadNewsTask;
    private LoadEventsTask loadEventsTask;
    private LoadSalesTask loadSalesTask;
    private LikeTask likeTask;
    private DeleteNewsTask deleteNewsTask;
    private UiSettingRepo uiSettingRepo;

    @Inject
    public EventsPresenterImpl(UserRepo userRepo, LoadNewsTask loadNewsTask,
                               LoadEventsTask loadEventsTask, LoadSalesTask loadSalesTask,
                               LikeTask likeTask, DeleteNewsTask deleteNewsTask, UiSettingRepo uiSettingRepo) {
        this.userRepo = userRepo;
        this.loadNewsTask = loadNewsTask;
        this.loadEventsTask = loadEventsTask;
        this.loadSalesTask = loadSalesTask;
        this.likeTask = likeTask;
        this.deleteNewsTask = deleteNewsTask;
        this.uiSettingRepo=uiSettingRepo;
    }

    @Override
    public void onAttach(EventsView eventsView) {
        super.onAttach(eventsView);
    }

    @Override
    public void onDestroy() {
        cancelAllTasks();
    }

    private void cancelAllTasks() {
        loadNewsTask.cancel();
        loadEventsTask.cancel();
        loadSalesTask.cancel();
        likeTask.cancel();
    }

    @Override
    public void onLoadItems(LoadNewsPackage request) {
        enableControls(false);
        cancelAllTasks();
        switch (request.getMode()) {
            case EventsFragment.TAB_EVENT:
                loadEventsTask.execute(request, new NewsSubscriber());
                break;
            case EventsFragment.TAB_SALE:
                loadSalesTask.execute(request, new NewsSubscriber());
                break;
            case EventsFragment.TAB_POST:
                loadNewsTask.execute(request, new NewsSubscriber());
                break;
        }
    }

    @Override
    public void storeTabActive(int position) {
        uiSettingRepo.setnActiveTabEventFragment(position);
    }

    @Override
    public void onLike(ILikeable iLikeable,RefreshableView refreshableView) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        if(iLikeable instanceof Event)
            likeDislikePackage.setModel(Keys.CAP_EVENT, ((Event)iLikeable).getId());
        else if (iLikeable instanceof Sale)
            likeDislikePackage.setModel(Keys.CAP_SHARE, ((Sale)iLikeable).getId());
        else if (iLikeable instanceof Post)
            likeDislikePackage.setModel(Keys.CAP_NEWS, ((Post)iLikeable).getId());

        likeTask.execute(likeDislikePackage, new LikeSubscriber(iLikeable, refreshableView));
    }

    @Override
    public int getStoredActiveTab() {
        return uiSettingRepo.getnActiveTabEventFragment();
    }

    class LikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private ILikeable iLikeable;
        private RefreshableView refreshableView;

        LikeSubscriber(ILikeable iLikeable,RefreshableView refreshableView) {
            this.iLikeable = iLikeable;
            this.refreshableView = refreshableView;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(MessageResponse messageResponse) {
            iLikeable.increaseLikes();
            refreshableView.refreshState();
        }
    }

    class NewsSubscriber extends SimpleSubscriber<List<IFlexible>> {
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            //showMessage(e.getMessage());
            ((MainActivity)view.getActivity()).commonError(e.getMessage());
        }

        @Override
        public void onNext(List<IFlexible> iFlexibles) {
            enableControls(true);
            view.appendItems(iFlexibles);
        }
    }
}
