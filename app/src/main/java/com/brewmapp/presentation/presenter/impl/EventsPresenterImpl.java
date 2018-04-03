package com.brewmapp.presentation.presenter.impl;

import javax.inject.Inject;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.entity.Sales;
import com.brewmapp.data.entity.container.Events;
import com.brewmapp.data.entity.container.Posts;
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

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

import com.brewmapp.presentation.presenter.contract.EventsPresenter;
import com.brewmapp.presentation.view.contract.RefreshableView;
import com.brewmapp.presentation.view.impl.activity.MainActivity;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;

import java.util.ArrayList;

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
        deleteNewsTask.cancel();
    }

    @Override
    public void onLoadItems(LoadNewsPackage request) {
        enableControls(false);
        cancelAllTasks();
        switch (request.getMode()) {
            case EventsFragment.TAB_EVENT:
                loadEventsTask.execute(request, new NewsSubscriberEvent());
                break;
            case EventsFragment.TAB_SALE:
                loadSalesTask.execute(request, new NewsSubscriberSale());
                break;
            case EventsFragment.TAB_NEWS:
                loadNewsTask.execute(request, new NewsSubscriberPosts());
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
        public void onNext(MessageResponse messageResponse) {
            iLikeable.increaseLikes();
            refreshableView.refreshState();
        }
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            ((MainActivity)view.getActivity()).commonError(e.getMessage());
        }

    }

    class NewsSubscriberPosts extends SimpleSubscriber<Posts> {
        @Override
        public void onNext(Posts posts) {
            enableControls(true);
            view.appendItems(new ArrayList<>(posts.getModels()));
        }
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            ((MainActivity)view.getActivity()).commonError(e.getMessage());
        }

    }

    class NewsSubscriberEvent extends SimpleSubscriber<Events> {
        @Override
        public void onNext(Events events) {
            enableControls(true);
            view.appendItems(new ArrayList<>(events.getModels()));
        }
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            ((MainActivity)view.getActivity()).commonError(e.getMessage());
        }

    }
    class NewsSubscriberSale extends SimpleSubscriber<Sales> {
        @Override
        public void onNext(Sales sales) {
            enableControls(true);
            view.appendItems(new ArrayList<>(sales.getModels()));
        }
        @Override
        public void onError(Throwable e) {
            enableControls(true);
            ((MainActivity)view.getActivity()).commonError(e.getMessage());
        }

    }

}
