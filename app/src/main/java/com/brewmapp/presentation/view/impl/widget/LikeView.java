package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IntegerRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

import com.brewmapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.widget.BaseFrameLayout;

/**
 * Created by oleg on 26.09.17.
 */

public class LikeView extends BaseFrameLayout {

    @BindView(R.id.view_like_counter) TextView counter;

    public LikeView(Context context) {
        super(context);
    }

    public LikeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LikeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LikeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int getLayoutToInflate() {
        return R.layout.view_like;
    }

    @Override
    protected void prepareView() {
        ButterKnife.bind(this);
    }

    public void setCount(int likes) {
        this.counter.setText(String.valueOf(likes));
    }

    public void increase() {
        try {
            counter.setText(String.valueOf(Integer.parseInt(counter.getText().toString()) + 1));
        } catch (Exception e) {

        }
    }
}
