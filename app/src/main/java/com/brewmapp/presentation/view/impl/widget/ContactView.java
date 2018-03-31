package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.task.ActivetyUsers;
import com.brewmapp.presentation.view.contract.FriendsView;
import com.brewmapp.utils.events.markerCluster.MapUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;
import com.brewmapp.data.entity.Contact;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.network.request.RequestParams;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 17.08.17.
 */

public class ContactView extends BaseLinearLayout implements InteractiveModelView<Contact> {

    @BindView(R.id.view_contact_avatar) ImageView avatar;
    @BindView(R.id.view_contact_subtitle) TextView subtitle;
    @BindView(R.id.view_contact_username) TextView username;
    @BindView(R.id.view_contact_online) TextView online;

    @Inject
    ActivetyUsers activetyUsers;


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
            listener.onModelAction(0,model);
        });

        //region fill text image
        User userShow ;
        try {
            userShow = model.getFriend_info();
        }catch (Exception e){return;};

        username.setText(userShow.getFormattedName());
        if (userShow.getThumbnail() != null) {
            Picasso.with(getContext()).load(userShow.getThumbnail()).fit().centerCrop().into(avatar);
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
        activetyUsers.execute(requestParams,new SimpleSubscriber<List<User>>(){
            @Override
            public void onNext(List<User> users) {
                super.onNext(users);
                Drawable drawable;
                if(users.size()>0)
                    drawable=(getContext().getResources().getDrawable(R.drawable.bg_round_green));
                else
                    drawable=(getContext().getResources().getDrawable(R.drawable.bg_round_gray));
                online.setBackground(drawable);
            }
        });
        //endregion
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
