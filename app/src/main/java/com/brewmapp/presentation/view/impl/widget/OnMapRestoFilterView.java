package com.brewmapp.presentation.view.impl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.FilterRestoOnMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 04.12.2017.
 */

public class OnMapRestoFilterView extends BaseLinearLayout implements InteractiveModelView<FilterRestoOnMap> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.city)
    TextView city;

    private Listener listener;
    private FilterRestoOnMap model;

    public OnMapRestoFilterView(Context context) {
        super(context);
    }

    public OnMapRestoFilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OnMapRestoFilterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public OnMapRestoFilterView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public FilterRestoOnMap getModel() {
        return model;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setModel(FilterRestoOnMap model) {
        this.model = model;
        title.setTypeface(null, Typeface.BOLD_ITALIC);
        title.setText(model.getName());
        setOnClickListener(v -> listener.onModelAction(Actions.ACTION_RESTO, model));
        city.setText(model.getCity());
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
