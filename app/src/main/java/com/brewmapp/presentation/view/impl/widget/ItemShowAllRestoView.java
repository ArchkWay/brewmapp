package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 16.01.2018.
 */

public class ItemShowAllRestoView extends BaseLinearLayout implements InteractiveModelView<Void> {

    private Listener listener;

    public ItemShowAllRestoView(Context context) {
        super(context);
    }

    public ItemShowAllRestoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemShowAllRestoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ItemShowAllRestoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {

    }

    @Override
    public void setModel(Void model) {
        setOnClickListener(v->listener.onModelAction(Actions.ACTION_SHOW_ALL_RESTO_BY_BEER,null));
    }

    @Override
    public Void getModel() {
        return null;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener=listener;
    }
}
