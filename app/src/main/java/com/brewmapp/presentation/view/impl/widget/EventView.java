package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Event;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by ovcst on 24.08.2017.
 */

public class EventView extends BaseLinearLayout implements InteractiveModelView<Event> {

    @BindView(R.id.view_event_date) TextView date;
    @BindView(R.id.view_event_name) TextView name;
    @BindView(R.id.view_event_text) TextView text;
    @BindView(R.id.view_event_location) TextView location;
    @BindView(R.id.view_event_image) ImageView image;

    private Event model;
    private Listener listener;

    public EventView(Context context) {
        super(context);
    }

    public EventView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EventView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public EventView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Event getModel() {
        return model;
    }

    @Override
    public void setModel(Event model) {
        this.model = model;
        name.setText(model.getName());
        text.setText(model.getText() != null ? Html.fromHtml(model.getText()) : null);
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
