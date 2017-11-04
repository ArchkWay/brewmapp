package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import com.brewmapp.data.entity.Review;

import ru.frosteye.ovsa.presentation.view.InteractiveModelView;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by Kras on 04.11.2017.
 */

public class ReviewView extends BaseLinearLayout implements InteractiveModelView<Review> {

    private Review model;

    public ReviewView(Context context) {
        super(context);
    }

    public ReviewView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReviewView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void setModel(Review model) {
        this.model = model;
    }

    @Override
    public Review getModel() {
        return model;
    }

    @Override
    public void setListener(Listener listener) {

    }

    @Override
    protected void prepareView() {

    }
}
