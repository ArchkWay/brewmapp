package com.brewmapp.presentation.view.impl.fragment.Chat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.ChatFragmentPresenter;
import com.brewmapp.presentation.view.contract.ChatFragmentView;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;
import com.brewmapp.presentation.view.impl.fragment.RestoEditFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;

/**
 * Created by Kras on 13.12.2017.
 */

public class ChatFragment extends BaseFragment implements ChatFragmentView {

    @BindView(R.id.fragment_chat_messages)    RecyclerView recyclerView;
    @BindView(R.id.fragment_chat_message_input) EditText mInputMessageView;
    @BindView(R.id.fragment_chat_send_button)    ImageButton send_button;

    private RecyclerView.Adapter mAdapter;
    private List<Message> mMessages = new ArrayList<Message>();

    private OnFragmentInteractionListener mListener;

    @Inject ChatFragmentPresenter presenter;

    @Override
    protected int getFragmentLayout() {return R.layout.fragment_chat;}

    @Override
    protected void initView(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new MessageAdapter(getActivity(), mMessages);
        recyclerView.setAdapter(mAdapter);
        mInputMessageView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int id, KeyEvent event) {
                    presenter.attemptSend(v);
                    return true;
            }
        });
        send_button.setOnClickListener(v -> presenter.attemptSend(mInputMessageView));

    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
        presenter.connectToChat(getActivity().getIntent());
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
    public void addMessage(Message message) {
        mMessages.add(message);
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    @Override
    public void sendSuccess() {
        mInputMessageView.setText("");
    }

    @Override
    public void insertMessage(Message message) {
        mMessages.add(0,message);
        scrollToBottom();
    }

    private void scrollToBottom() {
        recyclerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void commonError(String... message);
    }



}
