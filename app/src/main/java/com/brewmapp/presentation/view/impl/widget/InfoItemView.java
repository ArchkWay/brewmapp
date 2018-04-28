package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import ru.frosteye.ovsa.presentation.view.ModelView;

/**
 * Created by oleg on 17.08.17.
 */

public class InfoItemView extends AppCompatTextView implements ModelView<String> {

    private String model;

    public InfoItemView(Context context) {
        super(context);
    }

    public InfoItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
        setText(model);
    }
}
