package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.PropertyFilterBeer;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 13.02.2018.
 */

public class PropertyFilterBeerView extends BaseLinearLayout implements InteractiveModelView<PropertyFilterBeer> {

    @BindView(R.id.text_property_beer)
    TextView title;
    @BindView(R.id.chkbox)
    CheckBox checkbox;

    private PropertyFilterBeer model;
    private Listener listener;

    public PropertyFilterBeerView(Context context) {
        super(context);
    }

    public PropertyFilterBeerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PropertyFilterBeerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PropertyFilterBeerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(PropertyFilterBeer model) {
            title.setText(model.getName());
        checkbox.setChecked(model.isSelected());

        setOnClickListener(view -> {
            if (!model.isSelected()) {
                model.setSelected(true);
                checkbox.setChecked(true);
            } else {
                model.setSelected(false);
                checkbox.setChecked(false);
            }
            listener.onModelAction(0,model);
        });

    }

    @Override
    public PropertyFilterBeer getModel() {
        return model;
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }
}
