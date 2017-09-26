package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;

import com.brewmapp.data.entity.CardMenuField;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

/**
 * Created by ovcst on 02.08.2017.
 */

public class CardOptionRow extends BaseRelativeLayout implements ModelView<CardMenuField> {

    @BindView(R.id.view_cardOption_icon) ImageView icon;
    @BindView(R.id.view_cardOption_name) TextView title;
    @BindView(R.id.view_cardOption_bottom) View bottom;

    private CardMenuField model;

    public CardOptionRow(Context context) {
        super(context);
    }

    public CardOptionRow(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CardOptionRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CardOptionRow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public CardMenuField getModel() {
        return model;
    }

    public void setModel(CardMenuField model) {
        this.model = model;
        this.title.setText(model.getTitle());
        this.icon.setImageResource(model.getIcon());
        this.bottom.setVisibility(model.isExtraSpaceBottom() ? View.VISIBLE : View.GONE);
    }
}
