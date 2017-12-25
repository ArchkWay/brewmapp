package com.brewmapp.presentation.presenter.impl;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.brewmapp.R;
import com.brewmapp.data.db.contract.UserRepo;
import com.brewmapp.data.entity.Contact;
import com.brewmapp.data.entity.User;
import com.brewmapp.data.entity.wrapper.ContactInfo;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.exchange.response.ChatListDialogs;
import com.brewmapp.execution.services.ChatService;
import com.brewmapp.execution.task.AddFriend;
import com.brewmapp.execution.task.ListFriendsTask;
import com.brewmapp.execution.task.LoadUsersTask;
import com.brewmapp.presentation.presenter.contract.MessageFragmentPresenter;
import com.brewmapp.presentation.view.contract.MessageFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;
import com.brewmapp.presentation.view.impl.fragment.Chat.ChatResultReceiver;
import com.brewmapp.presentation.view.impl.widget.ContactView;

import java.util.ArrayList;

import javax.inject.Inject;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.adapter.ModelViewHolder;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

public class MessageFragmentPresenterImpl extends BasePresenter<MessageFragmentView> implements MessageFragmentPresenter {


    private AddFriend addFriend;
    private UserRepo userRepo;
    private LoadUsersTask loadUsersTask;

    @Inject
    public MessageFragmentPresenterImpl(AddFriend addFriend,UserRepo userRepo,LoadUsersTask loadUsersTask) {
        this.addFriend = addFriend;
        this.userRepo = userRepo;
        this.loadUsersTask = loadUsersTask;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void loadDialogs(boolean subscribers) {
        enableControls(false);

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
                    loadDialogs(true);
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
                User friend=((ContactView) ((ModelViewHolder) viewHolder).view).getModel().getFriend_info();
                new DialogConfirm(
                        fragmentActivity.getString(R.string.is_delete_dialog,friend.getFormattedName()),
                        fragmentActivity.getSupportFragmentManager(),
                        new DialogConfirm.OnConfirm() {
                                    @Override
                                    public void onOk() {
                                        toChatService(ChatService.ACTION_REQUEST_DELETE_DIALOG,friend);
                                    }

                                    @Override
                                    public void onCancel() {
                                        toChatService(ChatService.ACTION_REQUEST_DIALOGS,userRepo.load());
                                    }
                                });
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(list);
    }

    @Override
    public void onResume() {
        super.onResume();
        toChatService(ChatService.ACTION_SET_RECEIVER);
    }

    @Override
    public void onPause() {
        super.onPause();
        toChatService(ChatService.ACTION_CLEAR_RECEIVER);
    }

    //*********************************************************************
    private void toChatService(String command, Object... args) {

        if(view.getActivity()==null) return;

        Intent intent=new Intent(command,null,view.getActivity(), ChatService.class);
        //prepare arguments
        switch (command) {
            case ChatService.ACTION_REQUEST_DIALOGS:
            case ChatService.ACTION_REQUEST_DELETE_DIALOG:
                intent.putExtra(ChatService.EXTRA_PARAM1, ((User) args[0]).getId());
                break;
            case ChatService.ACTION_SET_RECEIVER:
                intent.putExtra(
                        ChatService.RECEIVER,
                        new ChatResultReceiver(new Handler(view.getActivity().getMainLooper()),new SimpleSubscriber<Bundle>(){
                            @Override
                            public void onNext(Bundle bundle) {
                                super.onNext(bundle);
                                fromChatService(bundle);
                            }

                            @Override
                            public void onError(Throwable e) {
                                super.onError(e);
                                view.commonError();
                            }
                        })
                );
                break;
        }
        view.getActivity().startService(intent);
    }
    private void fromChatService(Bundle resultData) {
        String action = resultData.getString(ChatService.EXTRA_PARAM1);
        switch (action) {
            case ChatService.ACTION_REQUEST_DIALOGS:
                ChatListDialogs chatListDialogs  = (ChatListDialogs) resultData.getSerializable(ChatService.EXTRA_PARAM2);
                loadUserDetails(chatListDialogs,new ArrayList<>());
            break;
            case ChatService.ACTION_SET_RECEIVER:
            case ChatService.ACTION_REQUEST_DELETE_DIALOG:
            case ChatService.ACTION_RELOAD_DIALOG:
                toChatService(ChatService.ACTION_REQUEST_DIALOGS,userRepo.load());
            break;

        }
    }

    private void loadUserDetails(ChatListDialogs chatListDialogs,ArrayList<IFlexible> arrayList) {
        if(chatListDialogs.size()>0) {
            loadUsersTask.execute(chatListDialogs.get(0).getUser().getId(), new SimpleSubscriber<ArrayList<User>>() {
                @Override
                public void onNext(ArrayList<User> users) {
                    super.onNext(users);
                    chatListDialogs.remove(0);
                    ContactInfo contactInfo = new ContactInfo();
                    Contact contact = new Contact();
                    contact.setId(users.get(0).getId());
                    contact.setFriend_info(users.get(0));
                    contact.setUser(users.get(0));
                    contactInfo.setModel(contact);
                    arrayList.add(contactInfo);
                    loadUserDetails(chatListDialogs, arrayList);
                }

                @Override
                public void onError(Throwable e) {
                    super.onError(e);
                    chatListDialogs.clear();
                    view.commonError(e.getMessage());
                }
            });
        }else {
            view.showDialogs(arrayList);
            enableControls(true);
        }
    }
}
