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
import com.brewmapp.data.entity.BeerBrand;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 28.11.2017.
 */

public class BeerBrandView extends BaseLinearLayout implements InteractiveModelView<BeerBrand> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.chkbox)
    CheckBox restoTypeCheckbox;
    @BindView(R.id.container)
    ConstraintLayout rootView;

    private Listener listener;
    private BeerBrand model;

    public BeerBrandView(Context context) {
        super(context);
    }

    public BeerBrandView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeerBrandView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeerBrandView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public BeerBrand getModel() {
        return model;
    }

    @Override
    public void setModel(BeerBrand model) {
        this.model = model;
        title.setText(model.getName());
        restoTypeCheckbox.setChecked(model.isSelected());
        if(model.getGetThumb()!=null&&model.getGetThumb().length()>0){
            Picasso.with(getContext()).load(model.getGetThumb()).fit().centerInside().into(logo);
        } else {
            Picasso.with(getContext()).load(R.drawable.ic_brand).fit().centerCrop().into(logo);
        }

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
