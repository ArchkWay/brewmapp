package com.brewmapp.presentation.view.impl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.FilterRestoField;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 17.11.2017.
 */

public class BeerView extends BaseLinearLayout implements InteractiveModelView<Beer> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    private Listener listener;
    private Beer model;

    public BeerView(Context context) {
        super(context);
    }

    public BeerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Beer getModel() {
        return model;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setModel(Beer model) {
        this.model = model;
        String titleRu = (model.getTitleRU() == null || TextUtils.isEmpty(model.getTitleRU()) ? "" : " (" + model.getTitleRU() + ")");
        title.setText(model.getTitle() + titleRu);
        if(model.getGetThumb() != null && !model.getGetThumb().isEmpty()) {
            Picasso.with(getContext()).load(model.getGetThumb()).fit().centerCrop().into(logo);
        } else {
            logo.setVisibility(INVISIBLE);
        }

        setOnClickListener(v -> listener.onModelAction(FilterRestoField.BEER, model));

    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
