package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.brewmapp.data.entity.Subscription;

import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 31.10.2017.
 */

public class SubscriptionView extends BaseLinearLayout implements InteractiveModelView<Subscription> {

    private Subscription model;

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
    }

    @Override
    public Subscription getModel() {
        return this.model ;
    }

    @Override
    public void setListener(Listener listener) {

    }

    @Override
    protected void prepareView() {

    }
}
