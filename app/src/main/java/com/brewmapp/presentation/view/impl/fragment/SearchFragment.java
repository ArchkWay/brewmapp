package com.brewmapp.presentation.view.impl.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.brewmapp.R;
import com.brewmapp.app.di.component.PresenterComponent;
import com.brewmapp.presentation.presenter.contract.SearchAllPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.widget.TabsView;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import ru.frosteye.ovsa.data.storage.ResourceHelper;
import ru.frosteye.ovsa.presentation.presenter.LivePresenter;
import ru.frosteye.ovsa.stub.impl.SimpleTabSelectListener;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragment extends BaseFragment implements SearchAllView {

    @BindView(R.id.fragment_search_list) RecyclerView list;
    @BindView(R.id.fragment_search_swipe) SwipeRefreshLayout swipe;
    @BindView(R.id.fragment_search_tabs) TabsView tabsView;

    @Inject SearchAllPresenter presenter;

    private String[] tabContent = ResourceHelper.getResources()
            .getStringArray(R.array.search_beer);

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        tabsView.setItems(Arrays.asList(tabContent), new SimpleTabSelectListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void attachPresenter() {
        presenter.onAttach(this);
    }

    @Override
    protected LivePresenter<?> getPresenter() {
        return presenter;
    }

    @Override
    public int getMenuToInflate() {
        return 0;
    }

    @Override
    protected void inject(PresenterComponent component) {
        component.inject(this);
    }

    @Override
    public CharSequence getTitle() {
        return ResourceHelper.getString(R.string.search_beer);
    }


    @Override
    public void enableControls(boolean enabled, int code) {

    }

    @Override
    protected void prepareView(View view) {
        super.prepareView(view);
        if(interractor()!=null)   view.post(() -> interractor().processShow(true,true));
    }

}
