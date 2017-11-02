package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.Actions;
import com.brewmapp.data.entity.Photo;
import com.brewmapp.data.entity.RestoType;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by nixus on 01.11.2017.
 */

public class RestoTypeView extends BaseLinearLayout implements InteractiveModelView<RestoType> {

    @BindView(R.id.type_name)
    TextView typeName;

    private Listener listener;
    private RestoType model;

    public RestoTypeView(Context context) {
        super(context);
    }

    public RestoTypeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RestoTypeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RestoTypeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
//        container.setOnClickListener(v -> listener.onModelAction(Actions.ACTION_SELECT_SALE, model));
    }

    @Override
    public RestoType getModel() {
        return model;
    }

    @Override
    public void setModel(RestoType model) {
        this.model = model;
        typeName.setText(model.getName());
    }

    @Override
    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
