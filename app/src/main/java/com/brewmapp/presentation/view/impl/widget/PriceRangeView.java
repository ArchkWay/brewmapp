package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.data.entity.PriceRange;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 03.11.2017.
 */

public class PriceRangeView extends BaseLinearLayout implements InteractiveModelView<PriceRange> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.chkbox)
    CheckBox restoTypeCheckbox;

    private Listener listener;
    private PriceRange model;

    public PriceRangeView(Context context) {
        super(context);
    }

    public PriceRangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PriceRangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PriceRangeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public PriceRange getModel() {
        return model;
    }

    @Override
    public void setModel(PriceRange model) {
        this.model = model;
        title.setText(model.getName());
        if (model.getImage() == null) {
            logo.setVisibility(INVISIBLE);
        }
        restoTypeCheckbox.setChecked(model.isSelected());

        setOnClickListener(view -> {
            if (!model.isSelected()) {
                model.setSelected(true);
                restoTypeCheckbox.setChecked(true);
            } else {
                model.setSelected(false);
                restoTypeCheckbox.setChecked(false);
            }
            listener.onModelAction(0,model);
        });
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
