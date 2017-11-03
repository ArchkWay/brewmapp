package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.Feature;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 03.11.2017.
 */

public class FeatureView extends BaseLinearLayout implements InteractiveModelView<Feature> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logoRestoType;
    @BindView(R.id.chkbox)
    CheckBox restoTypeCheckbox;

    private Listener listener;
    private Feature model;

    public FeatureView(Context context) {
        super(context);
    }

    public FeatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FeatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FeatureView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
//        container.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
    }

    @Override
    public Feature getModel() {
        return model;
    }

    @Override
    public void setModel(Feature model) {
        this.model = model;
        title.setText(model.getName());
        if(model.getGetThumb() != null && !model.getGetThumb().isEmpty()) {
            Picasso.with(getContext()).load(model.getGetThumb()).fit().centerCrop().into(logoRestoType);
        }
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
