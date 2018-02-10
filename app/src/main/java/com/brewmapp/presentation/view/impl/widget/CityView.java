package com.brewmapp.presentation.view.impl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.City;
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

public class CityView extends BaseLinearLayout implements InteractiveModelView<City> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.chkbox)
    CheckBox cityCheckbox;
    private Listener listener;
    private City model;

    public CityView(Context context) {
        super(context);
    }

    public CityView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CityView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public City getModel() {
        return model;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void setModel(City model) {
        this.model = model;
        title.setText(model.getName());
        logo.setVisibility(INVISIBLE);
        setOnClickListener(view -> {
            if (!model.isSelected()) {
                model.setSelected(true);
                cityCheckbox.setChecked(true);
            } else {
                model.setSelected(false);
                cityCheckbox.setChecked(false);
            }
            listener.onModelAction(0,model);
        });
        cityCheckbox.setChecked(model.isSelected());
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
