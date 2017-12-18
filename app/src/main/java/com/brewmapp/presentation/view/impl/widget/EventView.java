package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Event;
import com.brewmapp.data.entity.Interest;
import com.brewmapp.data.entity.Resto;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.tool.Text2TextWithHashTag;
import com.brewmapp.presentation.view.impl.activity.RestoDetailActivity;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

import static ru.frosteye.ovsa.data.storage.ResourceHelper.getString;

/**
 * Created by ovcst on 24.08.2017.
 */

public class EventView extends BaseLinearLayout implements InteractiveModelView<Event> {

    @BindView(R.id.view_event_date) TextView date;
    @BindView(R.id.view_event_name) TextView name;
    @BindView(R.id.view_event_name_resto) TextView name_resto;
    @BindView(R.id.view_event_text) TextView text;
    @BindView(R.id.view_event_location) TextView location;
    @BindView(R.id.view_event_image)    RoundedImageView image;
    @BindView(R.id.view_event_container) View container;
    @BindView(R.id.root_view_share_like)    ShareLikeView shareLikeView;


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
        new Text2TextWithHashTag(text,model.getText());
        text.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_EVENT, model));
        location.setText(model.getLocation().getFormattedAddress());
        date.setText(model.getDateFromFormated());
        try{name_resto.setText(model.getResto().getName());
            name_resto.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), RestoDetailActivity.class);
                Interest interest=null;
                try {
                    interest=new Interest(new Resto(String.valueOf(model.getResto().getId()),model.getResto().getName()));
                }catch (Exception e){}
                if(interest!=null) {
                    intent.putExtra(Keys.RESTO_ID, interest);
                    getContext().startActivity(intent);
                }
            });
        }catch (Exception e){}
        String urlImage=null;
        try{urlImage=model.getResto().getThumb();}catch (Exception e){}
        if(urlImage != null) {
            Picasso.with(getContext()).load(urlImage).fit().centerCrop().into(image);
        } else {
            image.setImageResource(R.drawable.ic_default_resto);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
        setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_EVENT, model));

    }
}
