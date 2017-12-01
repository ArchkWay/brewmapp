package com.brewmapp.presentation.view.impl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Country;
import com.brewmapp.data.entity.FilterRestoField;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 02.12.2017.
 */

public class CountryView extends BaseLinearLayout implements InteractiveModelView<Country> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;

    private Listener listener;
    private Country model;

    public CountryView(Context context) {
        super(context);
    }

    public CountryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CountryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CountryView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Country getModel() {
        return model;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setModel(Country model) {
        this.model = model;
        title.setText(model.getName());
        if(model.getGetThumb() != null && !model.getGetThumb().isEmpty()) {
            Picasso.with(getContext()).load(model.getGetThumb()).fit().centerCrop().into(logo);
        } else {
            logo.setVisibility(INVISIBLE);
        }

        setOnClickListener(v -> listener.onModelAction(FilterRestoField.CITY, model));
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
