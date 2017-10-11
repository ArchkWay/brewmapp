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

    @Inject
    public EventsPresenterImpl(UserRepo userRepo, LoadNewsTask loadNewsTask,
                               LoadEventsTask loadEventsTask, LoadSalesTask loadSalesTask,
                               LikeTask likeTask,DeleteNewsTask deleteNewsTask) {
        this.userRepo = userRepo;
        this.loadNewsTask = loadNewsTask;
        this.loadEventsTask = loadEventsTask;
        this.loadSalesTask = loadSalesTask;
        this.likeTask = likeTask;
        this.deleteNewsTask = deleteNewsTask;
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
        Context context=((EventsFragment) view).getContext();
        new DialogShare(
                context,
                context.getResources().getStringArray(R.array.share_items_sale),
                (dialog, which) -> {
            switch (which){
                case 0:
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                    break;
                case 1:
                    Intent intent=new Intent(context,NewPostActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:
                    showMessage(" разработке");
                    break;
            }
        });
    }

    @Override
    public void onSharePost(Post payload) {
        Context context=((EventsFragment) view).getContext();
        String[] items=userRepo.load().getId()==payload.getUser().getId()
                ?context.getResources().getStringArray(R.array.share_items_post)
                :context.getResources().getStringArray(R.array.share_items_sale);
        new DialogShare(context,
                items,
                (dialog, which) -> {
          switch (which){
              case 0:
                  Intent sendIntent = new Intent();
                  sendIntent.setAction(Intent.ACTION_SEND);
                  sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                  sendIntent.setType("text/plain");
                  context.startActivity(sendIntent);
                  break;
              case 1:
                  Intent intent=new Intent(context,NewPostActivity.class);
                  context.startActivity(intent);
                  break;
              case 2:
                  showMessage(" разработке");
                  break;
              case 3:
                  deleteNewsTask.execute(payload,new SimpleSubscriber<SingleResponse<Post>>(){
                      @Override
                      public void onError(Throwable e) {

                          showMessage(e.getMessage());
                      }

                      @Override
                      public void onNext(SingleResponse<Post> string) {
                          showMessage(getString(R.string.post_created));

                      }

                  });
                  break;
          }
        });
    }

    private class DialogShare extends AlertDialog.Builder{
        public DialogShare(@NonNull Context context,String[] items, DialogInterface.OnClickListener onClickListener) {
            super(context);
            //setItems(items, onClickListener);
            final ArrayAdapter<String> arrayAdapter =
                    new ArrayAdapter<String>(  context, android.R.layout.simple_list_item_1 ){
                        @NonNull
                        @Override
                        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                            View view=super.getView(position, convertView, parent);
                            ((TextView) view.findViewById(android.R.id.text1)).setTextColor(position==3?Color.RED:Color.BLACK);
                            return view;
                        }
                    };
            for (String s:items) arrayAdapter.add(s);
            setAdapter(arrayAdapter,onClickListener);
            show();
        }
    }

    private class LikeSubscriber extends SimpleSubscriber<MessageResponse> {

        private ILikeable iLikeable;

        private LikeSubscriber(ILikeable iLikeable) {
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
