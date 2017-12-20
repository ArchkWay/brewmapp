package com.brewmapp.presentation.view.impl.fragment.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brewmapp.R;

import java.util.List;

/**
 * Created by Kras on 14.12.2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private List<Message> mMessages;
    private int[] mUsernameColors;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
        mUsernameColors = context.getResources().getIntArray(R.array.username_colors);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        switch (viewType) {
            case Message.TYPE_MESSAGE_INPUT:
                layout = R.layout.item_chat_message_input;
                break;
            case Message.TYPE_MESSAGE_OUTPUT:
                layout = R.layout.item_chat_message_output;
                break;
            case Message.TYPE_LOG:
                layout = R.layout.item_chat_log;
                break;
            case Message.TYPE_ACTION:
                layout = R.layout.item_chat_action;
                break;
            default:
                layout = R.layout.item_chat_error;
        }
        View v = LayoutInflater
                .from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);
        viewHolder.setMessage(message.getMessage());
        viewHolder.setStatus(message.ismStateSending());

    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public List<Message> getmMessages() {
        return mMessages;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mMessageView;
        private TextView mStatusSendingView;


        public ViewHolder(View itemView) {
            super(itemView);

            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mStatusSendingView = (TextView) itemView.findViewById(R.id.status);
        }


        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }

        private int getUsernameColor(String username) {
            int hash = 7;
            for (int i = 0, len = username.length(); i < len; i++) {
                hash = username.codePointAt(i) + (hash << 5) - hash;
            }
            int index = Math.abs(hash % mUsernameColors.length);
            return mUsernameColors[index];
        }

        public void setStatus(boolean b) {
            if(mStatusSendingView!=null)
                mStatusSendingView.setVisibility(b?View.VISIBLE:View.GONE);
        }
    }
}