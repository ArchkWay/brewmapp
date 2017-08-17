package ru.frosteye.beermap.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.SocialContact;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.TextTools;

/**
 * Created by oleg on 17.08.17.
 */

public class InviteView extends BaseLinearLayout implements InteractiveModelView<SocialContact> {

    @BindView(R.id.view_invite_avatar) ImageView avatar;
    @BindView(R.id.view_invite_button) Button invite;
    @BindView(R.id.view_invite_contact) TextView contact;
    @BindView(R.id.view_invite_username) TextView username;

    private SocialContact model;
    private Listener listener;

    public InviteView(Context context) {
        super(context);
    }

    public InviteView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InviteView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InviteView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        username.setMovementMethod(new ScrollingMovementMethod());
        contact.setMovementMethod(new ScrollingMovementMethod());
        invite.setOnClickListener(v -> {
            if(listener != null) {
                listener.onModelAction(0, model);
            }
        });
    }

    @Override
    public SocialContact getModel() {
        return model;
    }

    @Override
    public void setModel(SocialContact model) {
        this.model = model;
        invite.setEnabled(!model.isInviteSent());
        invite.setText(!model.isInviteSent() ? R.string.invite : R.string.invited);
        if(!model.getFormattedName().isEmpty()) {
            username.setText(model.getFormattedName());
        } else if(model.getEmail() != null) {
            username.setText(model.getEmail());
        } else {
            username.setText(model.getPhone());
        }
        if(model.getPhone() != null) {
            contact.setText(model.getPhone());
        } else {
            contact.setText(model.getEmail());
        }
        contact.setVisibility(TextTools.isTrimmedEmpty(contact) ? GONE : VISIBLE);
        if(model.getPictureUrl() != null && !model.getPictureUrl().isEmpty()) {
            Picasso.with(getContext()).load(model.getPictureUrl()).fit().centerCrop().into(avatar);
        } else if(model.getLocalPictureUrl() != null) {
            avatar.setImageURI(model.getLocalPictureUrl());
        } else avatar.setImageDrawable(null);

    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
