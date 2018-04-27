package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.ChatDialog;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.ActivetyUsers;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;
import com.brewmapp.data.entity.Contact;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 17.08.17.
 */

public class ContactView extends BaseLinearLayout implements InteractiveModelView<Contact> {

    @BindView(R.id.view_contact_avatar) ImageView avatar;
    @BindView(R.id.view_contact_subtitle) TextView subtitle;
    @BindView(R.id.view_contact_username) TextView username;
    //@BindView(R.id.view_contact_online) TextView online;
    @BindView(R.id.view_contact_contaiter1)    LinearLayout contaiter1;
    @BindView(R.id.view_contact_contaiter0)    LinearLayout contaiter0;
    @BindView(R.id.view_contact_last_message)    TextView last_message;
    @BindView(R.id.view_contact_badget)    TextView badget;
    @BindView(R.id.view_contact_button_friend_accept)    Button button_friend_accept;
    @BindView(R.id.view_contact_button_friend_delete)    Button button_friend_delete;
    @BindView(R.id.view_contact_button_friend_cancel)    Button button_friend_cancel;

    @Inject
    ActivetyUsers activetyUsers;


    private Contact model;
    private Listener listener;
    private final int MODE_VIEW_FRIENDS=0;
    private final int MODE_VIEW_MESSAGE=1;
    private final int MODE_VIEW_USERS_LIST =2;
    private int mode;

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
        BeerMap.getAppComponent().plus(new PresenterModule(this)).inject(this);
    }

    @Override
    public Contact getModel() {
        return model;
    }

    @Override
    public void setModel(Contact model) {
        this.model = model;

        setOnClickListener(view -> {
            listener.onModelAction(FriendsView.FRIENDS_ACTION_CLICK,model);
        });

        //region fill text image
        User userShow ;
        try {
            userShow = model.getFriend_info();
        }catch (Exception e){return;};

        username.setText(userShow.getFormattedName());

        String pathAvatar=model.getFriend_info().getThumbnail();
        if(pathAvatar==null)
            pathAvatar=model.getUser_getThumb_Formatted();
        if (pathAvatar != null) {
            Picasso.with(getContext()).load(pathAvatar).fit().centerCrop().into(avatar);
        } else {
            try {
                avatar.setImageResource(userShow.getGender() == 1 ? R.drawable.ic_user_man : R.drawable.ic_user_woman);
            } catch (Exception e) {
                avatar.setImageResource(R.drawable.ic_user_man);
            }
        }
        //endregion

        //region fill date
        String last_login=model.getFriend_info().getLastLogin();
        subtitle.setText(MapUtils.FormatDate(last_login));

        RequestParams requestParams=new RequestParams();
        requestParams.addParam(Keys.USER_ID,model.getFriend_info().getId());
//        activetyUsers.execute(requestParams,new SimpleSubscriber<List<User>>(){
//            @Override
//            public void onNext(List<User> users) {
//                super.onNext(users);
//                Drawable drawable;
//                if(users.size()>0)
//                    drawable=(getContext().getResources().getDrawable(R.drawable.bg_round_green));
//                else
//                    drawable=(getContext().getResources().getDrawable(R.drawable.bg_round_gray));
//                online.setBackground(drawable);
//            }
//        });
        //endregion

        //region Visible
        ChatDialog chatDialog=model.getChatDialog();
        if(chatDialog==null) {
            if (model.getUser() == null)
                mode = MODE_VIEW_USERS_LIST;
            else
                mode = MODE_VIEW_FRIENDS;
        }else {
            mode = MODE_VIEW_MESSAGE;
        }

        button_friend_accept.setVisibility(GONE);
        button_friend_delete.setVisibility(GONE);
        button_friend_cancel.setVisibility(GONE);
        badget.setVisibility(GONE);
        last_message.setVisibility(GONE);
        contaiter1.setOrientation(VERTICAL);
        switch (mode){
            case MODE_VIEW_FRIENDS:
                //region MODE_VIEW_FRIENDS
                int status=model.getStatus();
                switch (status){
                    case FriendsView.FRIENDS_REQUEST_IN:
                        button_friend_accept.setVisibility(VISIBLE);
                        button_friend_delete.setVisibility(VISIBLE);
                        button_friend_accept.setOnClickListener(v->listener.onModelAction(FriendsView.FRIENDS_ACTION_ACCEPT,model));
                        button_friend_delete.setOnClickListener(v->listener.onModelAction(FriendsView.FRIENDS_ACTION_DELETE,model));
                        break;
                    case FriendsView.FRIENDS_NOW:
                        button_friend_delete.setVisibility(VISIBLE);
                        button_friend_delete.setOnClickListener(v->listener.onModelAction(FriendsView.FRIENDS_ACTION_DELETE,model));
                        break;
                    case FriendsView.FRIENDS_REQUEST_OUT:
                        button_friend_cancel.setVisibility(VISIBLE);
                        button_friend_cancel.setOnClickListener(v->listener.onModelAction(FriendsView.FRIENDS_ACTION_DELETE,model));
                        break;
                }
                //endregion
                break;
            case MODE_VIEW_MESSAGE:
                //region MODE_VIEW_MESSAGE
                contaiter1.setOrientation(HORIZONTAL);
                last_message.setVisibility(VISIBLE);
                last_message.setText(chatDialog.getLastMessage().getText());
                if(chatDialog.getUnread()>0){
                    badget.setVisibility(VISIBLE);
                    badget.setText(String.valueOf(chatDialog.getUnread()));
                }
                //endregion
                break;
            case MODE_VIEW_USERS_LIST:
                button_friend_accept.setVisibility(VISIBLE);
                button_friend_accept.setOnClickListener(v->listener.onModelAction(FriendsView.FRIENDS_ACTION_REQUEST_ACCEPT,model));
                break;
        }

        //endregion
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
