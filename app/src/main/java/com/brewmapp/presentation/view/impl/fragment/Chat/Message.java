package com.brewmapp.presentation.view.impl.fragment.Chat;

/**
 * Created by Kras on 14.12.2017.
 */

public class Message {

    public static final int TYPE_MESSAGE_INPUT = 0;
    public static final int TYPE_MESSAGE_OUTPUT = 3;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    private String mMessage;
    private String mUsername;
    private String mDirection;
    private int mId;
    private boolean mStateSending=false;
    private String image;
    private int imageHeight;
    private int imageWidth;

    private Message() {}

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(int imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean ismStateSending() {
        return mStateSending;
    }

    public void setmStateSending(boolean mStateSending) {
        this.mStateSending = mStateSending;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public int getmId() {
        return mId;
    }

    public int getType() {
        return mType;
    };

    public String getMessage() {
        return mMessage;
    };

    public String getUsername() {
        return mUsername;
    };

    public String getmDirection() {
        return mDirection;
    }

    public void setmDirection(String mDirection) {
        this.mDirection = mDirection;
    }

    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;
        private int id;
        private boolean mStateSending=false;
        private String image;
        private int imageHeight;
        private int imageWidth;


        public int getImageHeight() {
            return imageHeight;
        }

        public Builder setImageHeight(int imageHeight) {
            this.imageHeight = imageHeight;
            return this;
        }

        public int getImageWidth() {
            return imageWidth;
        }

        public Builder setImageWidth(int imageWidth) {
            this.imageWidth = imageWidth;
            return this;
        }

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Builder stateSending(boolean state) {
            mStateSending = state;
            return this;
        }

        public Builder setImage(String path) {
            image = path;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.mType = mType;
            message.mUsername = mUsername;
            message.mMessage = mMessage;
            message.mId = id;
            message.mStateSending = mStateSending;
            message.image=image;
            message.imageHeight=imageHeight;
            message.imageWidth=imageWidth;

            return message;
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }
    }
}
