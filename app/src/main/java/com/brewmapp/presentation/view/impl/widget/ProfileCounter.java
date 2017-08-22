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

public class ProfileCounter extends BaseLinearLayout {

    @BindView(R.id.view_profileCounter_counter) TextView counter;
    @BindView(R.id.view_profileCounter_name) TextView title;

    private String titleText;

    public ProfileCounter(Context context) {
        super(context);
    }

    public ProfileCounter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProfileCounter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ProfileCounter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] getStyleableResource() {
        return R.styleable.ProfileCounter;
    }

    @Override
    protected int getLayoutToInflate() {
        return R.layout.view_profile_counter;
    }

    @Override
    protected void onTypedArrayReady(TypedArray array) {
        titleText = array.getString(R.styleable.ProfileCounter_counterTitle);
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
        title.setText(titleText);
    }

    public void setCount(int count) {
        this.counter.setText(String.valueOf(count));
        this.counter.setVisibility(VISIBLE);
    }
}
