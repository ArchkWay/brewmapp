package ru.frosteye.beermap.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.MenuField;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

/**
 * Created by ovcst on 02.08.2017.
 */

public class MenuFieldRow extends BaseRelativeLayout implements ModelView<MenuField> {

    @BindView(R.id.view_menuField_icon) ImageView icon;
    @BindView(R.id.view_menuField_title) TextView title;

    private MenuField model;

    public MenuFieldRow(Context context) {
        super(context);
    }

    public MenuFieldRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuFieldRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MenuFieldRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public MenuField getModel() {
        return model;
    }

    public void setModel(MenuField model) {
        this.model = model;
        this.title.setText(model.getTitle());
        this.icon.setImageResource(model.getIcon());
    }
}
