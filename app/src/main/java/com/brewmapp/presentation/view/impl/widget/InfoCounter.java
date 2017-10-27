package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.brewmapp.R;
import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

/**
 * Created by oleg on 16.08.17.
 */

public class InfoCounter extends BaseLinearLayout {

    @BindView(R.id.view_profileCounter_counter) TextView counter;
    @BindView(R.id.view_profileCounter_name) TextView title;

    private String titleText;

    public InfoCounter(Context context) {
        super(context);
    }

    public InfoCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InfoCounter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] getStyleableResource() {
        return R.styleable.InfoCounter;
    }

    @Override
    protected int getLayoutToInflate() {
        return R.layout.view_profile_counter;
    }

    @Override
    protected void onTypedArrayReady(TypedArray array) {
        titleText = array.getString(R.styleable.InfoCounter_counterTitle);
    }

    @Override
    protected void prepareView() {
        if(isInEditMode()) return;
        ButterKnife.bind(this);
        title.setText(titleText);
    }

    public void setCount(int count) {
        this.counter.setText(String.valueOf(count));
        this.counter.setVisibility(VISIBLE);
    }
}
