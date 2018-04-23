package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

import com.brewmapp.R;
import com.brewmapp.app.environment.RequestCodes;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.execution.task.AddFriend;
import com.brewmapp.execution.task.FullSearchTask;
import com.brewmapp.execution.task.ListFriendsTask;
import com.brewmapp.presentation.view.contract.FriendsView;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;
import com.brewmapp.presentation.presenter.contract.FriendsPresenter;
import com.brewmapp.presentation.view.contract.MultiFragmentActivityView;
import com.brewmapp.presentation.view.impl.activity.MultiFragmentActivity;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;
import com.brewmapp.presentation.view.impl.dialogs.DialogManageContact;
import com.brewmapp.presentation.view.impl.fragment.FriendsFragment;
import com.brewmapp.presentation.view.impl.widget.ContactView;

public class FriendsPresenterImpl extends BasePresenter<FriendsView> implements FriendsPresenter {

    private ListFriendsTask listFriendsTask;
    private AddFriend addFriend;
    private FullSearchTask fullSearchTask;
    @Inject
    public FriendsPresenterImpl(ListFriendsTask listFriendsTask,AddFriend addFriend,FullSearchTask fullSearchTask) {
        this.listFriendsTask = listFriendsTask;
        this.addFriend = addFriend;
        this.fullSearchTask = fullSearchTask;
    }

    @Override
    public void onDestroy() {
        listFriendsTask.cancel();
    }

    @Override
    public void loadFriends(boolean subscribers) {
        enableControls(false);
        listFriendsTask.cancel();
        listFriendsTask.execute(null, new SimpleSubscriber<List<IFlexible>>() {
            @Override
            public void onError(Throwable e) {
                enableControls(true);
                showMessage(e.getMessage());
            }

            @Override
            public void onNext(List<IFlexible> result) {
                enableControls(true);
                view.showFriends(result);
            }
        });
    }

    @Override
    public void requestNewFriend(Intent data) {
        try {
            WrapperParams wrapperParams = new WrapperParams(Wrappers.USER_FRIENDS);
            wrapperParams.addParam(Keys.FRIEND_ID, data.getData().toString());
            addFriend.execute(wrapperParams,new SimpleSubscriber<String>(){
                @Override
                public void onNext(String s) {
                    super.onNext(s);
                    loadFriends(true);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    showMessage(e.getMessage());
                }
            });

        }catch (Exception e){showMessage(e.getMessage());}

    }

    @Override
    public void setItemTouchHelper(RecyclerView list) {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                FragmentActivity fragmentActivity= (FragmentActivity) list.getContext();
                if(fragmentActivity==null) return;
                Contact contact=((ContactView) ((ModelViewHolder) viewHolder).view).getModel();
                new DialogManageContact(fragmentActivity,fragmentActivity.getSupportFragmentManager(),contact,FriendsPresenterImpl.this);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);

    }

    @Override
    public void onClickItem(int code, Object payload, FragmentActivity fragmentActivity) {
        Contact contact=((Contact) payload);
        switch (contact.getStatus()){
            case FriendsView.FRIENDS_REQUEST_IN:
            case FriendsView.FRIENDS_REQUEST_OUT:
                new DialogManageContact(fragmentActivity,fragmentActivity.getSupportFragmentManager(),contact,FriendsPresenterImpl.this);
                break;
            case FriendsView.FRIENDS_NOW:{
                Intent intent=new Intent(MultiFragmentActivityView.MODE_CHAT, null, fragmentActivity, MultiFragmentActivity.class);
                User user=contact.getFriend_info();
                User friend=new User();
                friend.setId(user.getId());
                friend.setFirstname(user.getFirstname());
                friend.setLastname(user.getLastname());
                intent.putExtra(RequestCodes.INTENT_EXTRAS,friend);
                fragmentActivity.startActivity(intent);
            }
        }

    }

    @Override
    public void searchFriends(FullSearchPackage fullSearchPackage) {
        listFriendsTask.cancel();
        enableControls(false);
        fullSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
            @Override
            public void onNext(List<IFlexible> iFlexibles) {
                super.onNext(iFlexibles);
                enableControls(true);
                view.showFriends(iFlexibles);
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                enableControls(true);
                showMessage(e.getMessage());
            }
        });

    }
}
