package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterRestoField;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.ModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseRelativeLayout;

import static com.brewmapp.data.entity.FilterRestoField.FEATURES;
import static com.brewmapp.data.entity.FilterRestoField.METRO;

/**
 * Created by nlbochas on 28/10/2017.
 */

public class FilterRestoRowField extends BaseRelativeLayout implements InteractiveModelView<FilterRestoField> {

    @BindView(R.id.filter_name) TextView filterTitle;
    @BindView(R.id.selected_filter) TextView selectedFilter;
    @BindView(R.id.icon_filter) ImageView icon;
    @BindView(R.id.clear_filter) ImageView clear_filter;

    private FilterRestoField model;
    private Listener listener;

    public FilterRestoRowField(Context context) {
        super(context);
    }

    public FilterRestoRowField(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FilterRestoRowField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FilterRestoRowField(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    @Override
    public FilterRestoField getModel() {
        return model;
    }

    public void setModel(FilterRestoField model) {
        boolean itemEnable =true;

        switch (model.getId()){
            case METRO:
                itemEnable =false;
                break;
            case FEATURES:
                itemEnable =false;
                break;
            default:
                itemEnable =true;
        }

        this.model = model;
        this.filterTitle.setText(model.getTitle());
        this.selectedFilter.setTypeface(null, Typeface.BOLD_ITALIC);
        this.selectedFilter.setText(model.getSelectedFilter());
        this.icon.setImageResource(model.getIcon());
        if(itemEnable) {
            setOnClickListener(view -> listener.onModelAction(FilterRestoField.CODE_CLICK_FILTER_START_SELECTION, model));
            icon.setAlpha(1.0f);
            selectedFilter.setAlpha(1.0f);
        }else {
            setOnClickListener(view -> listener.onModelAction(FilterRestoField.CODE_CLICK_FILTER_ERROR, model));
            icon.setAlpha(0.3f);
            selectedFilter.setAlpha(0.3f);
        }
        clear_filter.setVisibility(model.getSelectedItemId()==null?GONE:VISIBLE);
        clear_filter.setOnClickListener(view -> listener.onModelAction(FilterRestoField.CODE_CLICK_FILTER_CLEAR,model));

    }

    @Override
    public void setListener(Listener listener) {
        this.listener=listener;
    }
}

