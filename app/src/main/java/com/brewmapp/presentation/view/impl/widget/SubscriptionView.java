package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Subscription;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.DateTools;

/**
 * Created by Kras on 31.10.2017.
 */

public class SubscriptionView extends BaseLinearLayout implements InteractiveModelView<Subscription> {

    @BindView(R.id.view_subscription_avatar)    ImageView avatar;
    @BindView(R.id.view_subscription_author)    TextView author;
    @BindView(R.id.view_subscription_date)    TextView date;
    @BindView(R.id.view_subscription_arrow_right)    ImageView arrow_right;


    private Subscription model;
    private Listener listener;

    public SubscriptionView(Context context) {
        super(context);
    }

    public SubscriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubscriptionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SubscriptionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Subscription model) {
        this.model = model;
        if(model.getInformation().getGetThumb() != null) {
            Picasso.with(getContext()).load(model.getInformation().getGetThumb()).fit().centerCrop().into(avatar);
        } else if(Keys.CAP_RESTO.equals(model.getRelated_model())){
                avatar.setImageResource(R.drawable.ic_default_resto);
        }

        author.setText(model.getInformation().getName());

        SimpleDateFormat dashedDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            date.setText(getContext().getString(R.string.subscribed, DateTools.formatDottedDateWithTime(dashedDateFormat.parse(model.getCreated_at()))));
        }catch (Exception t){}

        setOnClickListener(v -> listener.onModelAction(Actions.ACTION_START_SHOW_NEWS,this.model.getInformation().getId()));
        arrow_right.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_START_DETAILS_ACTIVITY,this.model.getInformation().getId()));
    }

    @Override
    public Subscription getModel() {
        return this.model ;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
    }
}
