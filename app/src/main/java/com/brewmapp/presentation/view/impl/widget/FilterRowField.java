package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterField;
import com.brewmapp.data.entity.MenuField;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterRowField extends BaseRelativeLayout implements ModelView<FilterField> {

    @BindView(R.id.filter_name) TextView filterTitle;
    @BindView(R.id.selected_filter) TextView selectedFilter;
    @BindView(R.id.icon_filter) ImageView icon;
    @BindView(R.id.line_bottom) View lineBottom;

    private FilterField model;

    public FilterRowField(Context context) {
        super(context);
    }

    public FilterRowField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterRowField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FilterRowField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public FilterField getModel() {
        return model;
    }

    public void setModel(FilterField model) {
        this.model = model;
        this.filterTitle.setText(model.getTitle());
        this.selectedFilter.setTypeface(null, Typeface.BOLD_ITALIC);
        this.selectedFilter.setText(model.getSelectedFilter());
        this.icon.setImageResource(model.getIcon());
    }

    public void hideBottomLine() {
        this.lineBottom.setVisibility(GONE);
    }
}

