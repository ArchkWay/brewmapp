package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.brewmapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.frosteye.ovsa.presentation.view.widget.BaseFrameLayout;

/**
 * Created by oleg on 23.08.17.
 */

public class TabsView extends BaseFrameLayout {
    @BindView(R.id.view_tabs_tabs) TabLayout tabs;

    public TabsView(Context context) {
        super(context);
    }

    public TabsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TabsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TabsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected int[] getStyleableResource() {
        return R.styleable.TabsView;
    }

    @Override
    protected int getLayoutToInflate() {
        return R.layout.view_tabs;
    }

    @Override
    protected void onTypedArrayReady(TypedArray array) {
    }

    @Override
    protected void prepareView() {

        ButterKnife.bind(this);
    }

    public void setItems(List<String> items, TabLayout.OnTabSelectedListener listener) {
        for(String string: items) {
            TabLayout.Tab tab = tabs.newTab().setText(string);
            tabs.addTab(tab);
        }
        tabs.addOnTabSelectedListener(listener);
    }
}
