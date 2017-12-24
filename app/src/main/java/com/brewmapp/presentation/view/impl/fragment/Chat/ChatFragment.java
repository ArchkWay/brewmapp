package com.brewmapp.presentation.view.impl.fragment.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.data.entity.ChatReceiveMessage;
import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.presenter.contract.ChatFragmentPresenter;
import com.brewmapp.presentation.view.contract.ChatFragmentView;
import com.brewmapp.presentation.view.impl.dialogs.DialogConfirm;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.impl.EndlessRecyclerOnScrollListener;
import ru.frosteye.ovsa.stub.listener.SelectListener;

/**
 * Created by Kras on 13.12.2017.
 */

public class ChatFragment extends BaseFragment implements ChatFragmentView {

    @BindView(R.id.fragment_chat_messages)    RecyclerView recyclerView;
    @BindView(R.id.fragment_chat_message_input) EditText mInputMessageView;
    @BindView(R.id.fragment_chat_send_button)    ImageButton send_button;

    private MessageAdapter mAdapter;
    private List<Message> mMessages = new ArrayList<Message>();
    private OnFragmentInteractionListener mListener;

    @Inject ChatFragmentPresenter presenter;

    @Override
    protected int getFragmentLayout() {return R.layout.fragment_chat;}

    @Override
    protected void initView(View view) {
        setHasOptionsMenu(true);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager );
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                mAdapter = new MessageAdapter(getActivity(), mMessages,recyclerView.getWidth());
                recyclerView.setAdapter(mAdapter);
            }
        });
        mInputMessageView.setOnEditorActionListener((v, id, event) -> {presenter.sendMessage(v);return true;});
        send_button.setOnClickListener(v -> {presenter.sendMessage(mInputMessageView);mInputMessageView.setText("");});
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean needCalcDownload=false;
            int countCommonItems=0;
            int numberPositionDownload=3;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(countCommonItems!=manager.getItemCount()) {
                    countCommonItems = manager.getItemCount();
                    needCalcDownload=true;
                }

                if(needCalcDownload){
                    if(manager.findFirstVisibleItemPosition()<=numberPositionDownload){
                        needCalcDownload=false;
                        //startDownload
                        if(mAdapter.getItemCount()>0)
                            presenter.nextPage(mAdapter.getmMessages().get(0));
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.connectToChat(getActivity().getIntent());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.photo,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_photo:
                mListener.selectPhoto(presenter.getSelectListenerPhoto(this));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RestoEditFragment.OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return null;
    }

    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    public void commonError(String... messages) {getActivity().runOnUiThread(()->mListener.commonError(messages));}

    @Override
    public void addMessages(List<Message> messages, boolean insert) {
        if(insert){
            int size=mMessages.size();
            for (int i=0;i<messages.size();i++) {
                mMessages.add(0, messages.get(i));
                mAdapter.notifyItemInserted(i);
            }
            if(size==0)
                scrollToBottom();
        }else {
            mMessages.add(messages.get(0));
            mAdapter.notifyItemInserted(mMessages.size() - 1);
            scrollToBottom();
        }
    }

    @Override
    public void setFriend(User friend) {
        mListener.setTitle(friend.getFormattedName());
    }

    @Override
    public void clearMessages() {
        mMessages.clear();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setStatusMessage(ChatReceiveMessage chatReceiveMessage) {
        for (Message message:mMessages){
            if(message.getmId()==0&&message.getMessage().equals(chatReceiveMessage.getText())){
                message.setmStateSending(false);
                message.setmId(Integer.valueOf(chatReceiveMessage.getId()));
                mAdapter.notifyDataSetChanged();
                return;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void commonError(String... message);

        void setTitle(CharSequence title);

        void selectPhoto(SelectListener selectListener);
    }



}
