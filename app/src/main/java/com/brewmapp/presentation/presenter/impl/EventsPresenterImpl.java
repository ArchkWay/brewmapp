package com.brewmapp.presentation.presenter.impl;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import javax.inject.Inject;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.Sale;
import com.brewmapp.data.model.ILikeable;
import com.brewmapp.data.pojo.LikeDislikePackage;
import com.brewmapp.data.pojo.LoadNewsPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.response.base.MessageResponse;
import com.brewmapp.execution.exchange.response.base.SingleResponse;
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
import com.brewmapp.presentation.view.impl.activity.NewPostActivity;
import com.brewmapp.presentation.view.impl.fragment.EventsFragment;

import java.util.List;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

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
        eventsView.setTabActive(uiSettingRepo.getnActiveTabEventFragment());
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

    @Override
    public void onLikePost(Post post) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_NEWS, post.getId());
        likeTask.execute(likeDislikePackage, new LikeSubscriber(post));
    }

    @Override
    public void onLikeSale(Sale sale) {
        LikeDislikePackage likeDislikePackage = new LikeDislikePackage(LikeDislikePackage.TYPE_LIKE);
        likeDislikePackage.setModel(Keys.CAP_SHARE, sale.getId());
        likeTask.execute(likeDislikePackage, new LikeSubscriber(sale));
    }

    @Override
    public void onShareSale(Sale payload) {
        view.showShareDialog(R.array.share_items_sale,payload);
    }

    @Override
    public void onSharePost(Post payload) {

        if(userRepo.load().getId()==payload.getUser().getId())
            view.showShareDialog(R.array.share_items_post,payload);
        else
            view.showShareDialog(R.array.share_items_sale,payload);

    }

    @Override
    public void storeTabActive(int position) {
        uiSettingRepo.setnActiveTabEventFragment(position);
    }

    @Override
    public void onDeleteNewsTask(Post post) {
        deleteNewsTask.execute(post,new SimpleSubscriber<SingleResponse<Post>>(){
            @Override
            public void onError(Throwable e) {

                showMessage(e.getMessage());
            }

            @Override
            public void onNext(SingleResponse<Post> string) {
                showMessage(getString(R.string.post_created));

            }

        });
    }


    class LikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private ILikeable iLikeable;

        LikeSubscriber(ILikeable iLikeable) {
            this.iLikeable = iLikeable;
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(MessageResponse messageResponse) {
            iLikeable.increaseLikes();
            view.refreshState();
        }
    }

    class NewsSubscriber extends SimpleSubscriber<List<IFlexible>> {
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
