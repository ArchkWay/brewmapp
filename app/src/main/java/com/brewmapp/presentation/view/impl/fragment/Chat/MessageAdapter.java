package com.brewmapp.presentation.view.impl.fragment.Chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.Url;

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
        viewHolder.setImage(message.getImage());
        viewHolder.setImageHeight(message.getImageHeight());
        viewHolder.setImageWidth(message.getImageWidth());

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
        private ImageView mImageView;
        private int imageHeight;
        private int imageWidth;


        public ViewHolder(View itemView) {
            super(itemView);

            mMessageView = (TextView) itemView.findViewById(R.id.message);
            mStatusSendingView = (TextView) itemView.findViewById(R.id.status);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
        }

        public void setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
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

        public void setImage(String path){
            if (path!=null){
                mImageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        mImageView.getViewTreeObserver().removeOnPreDrawListener(this);
                        //resize
                        float ratio=(float) mImageView.getWidth()/(float) imageWidth;
                        LinearLayout.LayoutParams p= (LinearLayout.LayoutParams) mImageView.getLayoutParams();
                        p.height=(int) (imageHeight*ratio);
                        mImageView.setLayoutParams(p);
                        //load
                        mImageView.post(() -> {
                            //Picasso.with(mImageView.getContext()).load(path).into(mImageView);
                            if(path.startsWith("http"))
                                Picasso.with(mImageView.getContext()).load(path).fit().centerCrop().into(mImageView);
                            else
                                Picasso.with(mImageView.getContext()).load(new File(path)).fit().centerCrop().into(mImageView);

                        });

                        return true;
                    }
                });
                mImageView.setVisibility(View.VISIBLE);
            }else {
                mImageView.setVisibility(View.GONE);
            }
        }
    }
}
