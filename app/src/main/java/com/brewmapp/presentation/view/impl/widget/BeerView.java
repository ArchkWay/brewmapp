package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.FilterActions;
import com.brewmapp.data.entity.Beer;
import com.brewmapp.data.entity.PriceRange;
import com.brewmapp.data.entity.Resto;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 17.11.2017.
 */

public class BeerView extends BaseLinearLayout implements InteractiveModelView<Beer> {

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.logo)
    ImageView logo;
    @BindView(R.id.chkbox)
    CheckBox restoTypeCheckbox;

    private Listener listener;
    private Beer model;

    public BeerView(Context context) {
        super(context);
    }

    public BeerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BeerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BeerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public Beer getModel() {
        return model;
    }

    @Override
    public void setModel(Beer model) {
        this.model = model;
        title.setText(model.getTitle_ru());
        if (model.getImage() == null) {
            logo.setVisibility(INVISIBLE);
        }
        if (model.isSelected()) {
            restoTypeCheckbox.setChecked(true);
        } else {
            restoTypeCheckbox.setChecked(false);
        }

        setOnClickListener(v -> listener.onModelAction(FilterActions.BEER, model));
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
