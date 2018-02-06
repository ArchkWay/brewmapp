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
import com.brewmapp.data.entity.RestoType;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 01.11.2017.
 */

public class TypeView extends BaseLinearLayout implements InteractiveModelView<RestoType> {

    @BindView(R.id.title) TextView title;
    @BindView(R.id.logo) ImageView logoRestoType;
    @BindView(R.id.chkbox) CheckBox restoTypeCheckbox;
    @BindView(R.id.container) ConstraintLayout viewRoot;

    private RestoType model;
    private Listener listener;

    public TypeView(Context context) {
        super(context);
    }

    public TypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TypeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public RestoType getModel() {
        return model;
    }

    @Override
    public void setModel(RestoType model) {
        this.model = model;
        title.setText(model.getName());
        if(model.getGetThumb() != null && !model.getGetThumb().isEmpty()) {
            Picasso.with(getContext()).load(model.getGetThumb()).fit().centerCrop().into(logoRestoType);
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
