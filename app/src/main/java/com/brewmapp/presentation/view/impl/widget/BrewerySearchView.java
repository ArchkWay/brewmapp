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
import com.brewmapp.data.entity.Brewery;
import com.brewmapp.data.entity.FilterBreweryField;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 16.12.2017.
 */

public class BrewerySearchView extends BaseLinearLayout implements InteractiveModelView<Brewery> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    private Listener listener;
    private Brewery model;

    public BrewerySearchView(Context context) {
        super(context);
    }

    public BrewerySearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BrewerySearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BrewerySearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Brewery getModel() {
        return model;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setModel(Brewery model) {
        this.model = model;
//        String titleRu = (model.getName_ru() == null || TextUtils.isEmpty(model.getName_ru()) ? "" : " (" + model.getName_ru() + ")");
        title.setText(model.getName());
        if(model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(getContext()).load(model.getImage()).fit().centerCrop().into(logo);
        } else {
            logo.setVisibility(INVISIBLE);
        }

        setOnClickListener(v -> listener.onModelAction(FilterBreweryField.NAME, model));

    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
