package com.brewmapp.presentation.view.impl.dialogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.pojo.GeoPackage;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;

/**
 * Created by Kras on 19.11.2017.
 */

@SuppressLint("ValidFragment")
public class DialogSelectCountryCity extends DialogFragment {
    Context context;
    ListView lv;
    SearchView sv;
    ArrayAdapter<String> adapter;
    RefreshableSwipeRefreshLayout refreshableSwipeRefreshLayout;
    String[] players={};
    List<City> cityList;
    private OnSelectCity onSelectCity;

    @Inject
    LoadCityTask loadCityTask;


    @SuppressLint("ValidFragment")
    public DialogSelectCountryCity(Context context, FragmentManager supportManagerFragment, OnSelectCity onSelectCity){
        this.context=context;
        this.onSelectCity=onSelectCity;

        BeerMap.getAppComponent().plus(new PresenterModule((BaseActivity) getActivity())).inject(this);
        show(supportManagerFragment,"ssss");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle(R.string.title_dialog_search_city);

        // TODO Auto-generated method stub
        View rootView=inflater.inflate(R.layout.dialog_select_country, null);

        refreshableSwipeRefreshLayout= (RefreshableSwipeRefreshLayout) rootView.findViewById(R.id.fragment_dialog_swipe);
        lv=(ListView) rootView.findViewById(R.id.listView1);
        sv=(SearchView) rootView.findViewById(R.id.searchView1);
        sv.setIconified(false);
        lv.setAdapter(adapter);
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                dismiss();
                return false;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(City city:cityList)
                    if(city.getName().equals(((ListView) parent).getAdapter().getItem(position))) {
                        onSelectCity.onSelect(city);
                        dismiss();
                    }

            }
        });
        sv.setQueryHint("Search..");
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                // TODO Auto-generated method stub
                return false;
            }
            @Override
            public boolean onQueryTextChange(String txt) {
                // TODO Auto-generated method stub
                loadCityTask.cancel();
                if(txt.length()>0) {
                    GeoPackage geoPackage = new GeoPackage();
                    geoPackage.setCityName(txt);
                    //geoPackage.setCountryId("1");
                    loadCityTask.execute(geoPackage, new SimpleSubscriber<List<City>>() {
                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            dismiss();
                        }

                        @Override
                        public void onNext(List<City> cities) {
                            super.onNext(cities);
                            cityList = cities;
                            refreshableSwipeRefreshLayout.setRefreshing(false);
                            players = new String[cities.size()];
                            for (int i = 0; i < cities.size(); i++)
                                players[i] = cities.get(i).getName();
                            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, players);
                            lv.setAdapter(adapter);
                            if (sv.getQuery().toString().length() > 0)
                                adapter.getFilter().filter(sv.getQuery().toString());
                        }

                        @Override
                        public void onComplete() {
                            super.onComplete();
                            refreshableSwipeRefreshLayout.setRefreshing(false);
                        }
                    });
                    refreshableSwipeRefreshLayout.setRefreshing(true);
                }else {
                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, new String[0]);
                    lv.setAdapter(adapter);
                    refreshableSwipeRefreshLayout.setRefreshing(false);
                }


                if(adapter!=null)
                    adapter.getFilter().filter(txt);
                return false;
            }
        });

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadCityTask.cancel();
    }

    public interface OnSelectCity{
        public void onSelect(City city);
    }
}
