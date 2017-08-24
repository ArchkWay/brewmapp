package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;
import com.brewmapp.data.entity.Contact;

import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 17.08.17.
 */

public class ContactView extends BaseLinearLayout implements InteractiveModelView<Contact> {

    @BindView(R.id.view_contact_avatar) ImageView avatar;
    @BindView(R.id.view_contact_subtitle) TextView subtitle;
    @BindView(R.id.view_contact_username) TextView username;

    private Contact model;
    private Listener listener;

    public ContactView(Context context) {
        super(context);
    }

    public ContactView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContactView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ContactView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Contact getModel() {
        return model;
    }

    @Override
    public void setModel(Contact model) {
        this.model = model;
        username.setText(model.getTitle());
        if(model.getImageUrl() != null) {
            Picasso.with(getContext()).load(model.getImageUrl()).fit().centerCrop().into(avatar);
        } else {
            avatar.setImageResource(R.drawable.ic_user_man);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
