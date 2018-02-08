package com.brewmapp.presentation.presenter.impl;

import android.content.Context;

import com.brewmapp.data.db.contract.UiSettingRepo;
import com.brewmapp.data.entity.FilterBeerField;
import com.brewmapp.data.entity.FilterBreweryField;
import com.brewmapp.data.entity.FilterRestoField;
import com.brewmapp.presentation.presenter.contract.SearchFragmentPresenter;
import com.brewmapp.presentation.view.contract.SearchAllView;
import com.brewmapp.presentation.view.impl.fragment.SearchFragment;

import java.util.List;

import javax.inject.Inject;

import io.paperdb.Paper;
import ru.frosteye.ovsa.presentation.presenter.BasePresenter;

/**
 * Created by ovcst on 24.08.2017.
 */

public class SearchFragmentPresenterImpl extends BasePresenter<SearchAllView> implements SearchFragmentPresenter {

    private UiSettingRepo uiSettingRepo;
    private Context context;

    @Inject
    public SearchFragmentPresenterImpl(Context context, UiSettingRepo uiSettingRepo) {
        this.uiSettingRepo = uiSettingRepo;
        this.context = context;
    }

    @Override
    public void onAttach(SearchAllView searchAllView) {
        super.onAttach(searchAllView);
    }

    @Override
    public void onDestroy() {
    }

    @Override
    public void setTabActive(int position) {
        switch (position) {
            case SearchFragment.TAB_RESTO: {
                List<FilterRestoField> list=Paper.book().read(SearchFragment.CATEGORY_LIST_RESTO);
                if(list==null)
                {
                    list = FilterRestoField.createDefault(context);
                    Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, list);
                }
                view.showRestoFilters(list);
            }break;
            case SearchFragment.TAB_BEER: {
                List<FilterBeerField> list=Paper.book().read(SearchFragment.CATEGORY_LIST_BEER);
                if(list==null)
                {
                    list = FilterBeerField.createDefault(context);
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, list);
                }
                view.showBeerFilters(list);
            }break;
            case 2:
                List<FilterBreweryField> list=Paper.book().read(SearchFragment.CATEGORY_LIST_BREWERY);
                if(list==null)
                {
                    list = FilterBreweryField.createDefault(context);
                    Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, list);
                }
                view.showBreweryFilters(list);
                break;
        }
    }


    @Override
    public void saveRestoFilterChanges(List<FilterRestoField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_RESTO, fields)).start();
    }

    @Override
    public void saveBeerFilterChanges(List<FilterBeerField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_BEER, fields)).start();
    }

    @Override
    public void saveBreweryFilterChanges(List<FilterBreweryField> fields) {
        new Thread(() -> Paper.book().write(SearchFragment.CATEGORY_LIST_BREWERY, fields)).start();
    }
}
