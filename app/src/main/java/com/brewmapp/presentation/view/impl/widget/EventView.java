package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Event;
import com.brewmapp.execution.tool.HashTagHelper2;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;
import ru.frosteye.ovsa.tool.DateTools;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

/**
 * Created by ovcst on 24.08.2017.
 */

public class EventView extends BaseLinearLayout implements InteractiveModelView<Event> {

    @BindView(R.id.view_event_date) TextView date;
    @BindView(R.id.view_event_name) TextView name;
    @BindView(R.id.view_event_text) TextView text;
    @BindView(R.id.view_event_location) TextView location;
    @BindView(R.id.view_event_image) ImageView image;
    @BindView(R.id.view_event_container) View container;
    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;


    private Event model;

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
        if(isInEditMode()) return;

        ButterKnife.bind(this);
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public Event getModel() {
        return model;
    }

    @Override
    public void setModel(Event model) {
        this.model = model;
        shareLikeView.setiLikeable(model);
        name.setText(model.getName());
        new HashTagHelper2(text,model.getText());
        location.setText(model.getLocation().getFormattedAddress());
        date.setText(getString(R.string.pattern_event_start,
                DateTools.formatDottedDate(model.getDateFrom()), model.getTimeFrom()));
        if(model.getThumb() != null && !model.getThumb().isEmpty()) {
            Picasso.with(getContext()).load(model.getThumb()).fit().centerCrop().into(image);
        } else {
            image.setImageResource(R.drawable.ic_default_resto);
        }
    }

    @Override
    public void setListener(Listener listener) {
        //this.listener = listener;
        setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_EVENT, model));

    }
}
