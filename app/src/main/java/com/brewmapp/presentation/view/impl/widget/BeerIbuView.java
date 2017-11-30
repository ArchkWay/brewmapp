package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.BeerIbu;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 30.11.2017.
 */

public class BeerIbuView extends BaseLinearLayout implements InteractiveModelView<BeerIbu> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.chkbox)
    CheckBox restoTypeCheckbox;
    @BindView(R.id.container)
    ConstraintLayout rootView;

    private Listener listener;
    private BeerIbu model;

    public BeerIbuView(Context context) {
        super(context);
    }

    public BeerIbuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeerIbuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeerIbuView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public BeerIbu getModel() {
        return model;
    }

    @Override
    public void setModel(BeerIbu model) {
        this.model = model;
        title.setText(model.getName());

        logo.setVisibility(INVISIBLE);
        restoTypeCheckbox.setChecked(model.isSelected());

        setOnClickListener(view -> {
            if (!model.isSelected()) {
                model.setSelected(true);
                restoTypeCheckbox.setChecked(true);
            } else {
                model.setSelected(false);
                restoTypeCheckbox.setChecked(false);
            }
        });
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
