package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.FilterActions;
import com.brewmapp.data.entity.Kitchen;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 02.11.2017.
 */

public class KitchenView extends BaseLinearLayout implements InteractiveModelView<Kitchen> {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.logo) ImageView logo;
    @BindView(R.id.chkbox) CheckBox restoTypeCheckbox;
    @BindView(R.id.container) ConstraintLayout rootView;

    private Listener listener;
    private Kitchen model;

    public KitchenView(Context context) {
        super(context);
    }

    public KitchenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public KitchenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public KitchenView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Kitchen getModel() {
        return model;
    }

    @Override
    public void setModel(Kitchen model) {
        this.model = model;
        title.setText(model.getName());
        if(model.getImage() != null && !model.getImage().isEmpty()) {
            Picasso.with(getContext()).load(model.getImage()).fit().centerCrop().into(logo);
        } else {
            logo.setVisibility(INVISIBLE);
        }

        if (model.isSelected()) {
            restoTypeCheckbox.setChecked(true);
        } else {
            restoTypeCheckbox.setChecked(false);
        }
        setOnClickListener(v -> listener.onModelAction(FilterActions.KITCHEN, model));
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }

}
