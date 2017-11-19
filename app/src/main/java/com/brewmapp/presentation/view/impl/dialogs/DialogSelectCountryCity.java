package com.brewmapp.presentation.view.impl.dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.di.module.PresenterModule;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.User;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;
import com.brewmapp.execution.task.LoadCityTask;
import com.brewmapp.execution.task.LoadClaimTypesTask;
import com.brewmapp.presentation.view.impl.activity.BaseActivity;

import org.greenrobot.eventbus.util.ErrorDialogManager;

import java.sql.Wrapper;
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
    TextView text_city;
    User user;
    List<City> cityList;

    @Inject
    LoadCityTask loadCityTask;


    @SuppressLint("ValidFragment")
    public DialogSelectCountryCity(Context context, FragmentManager supportManagerFragment, TextView text_city, User user){
        this.context=context;
        this.text_city=text_city;
        this.user=user;


        BeerMap.getAppComponent().plus(new PresenterModule((BaseActivity) getActivity())).inject(this);
        show(supportManagerFragment,"ssss");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                for(City s:cityList)
                    if(s.getName().equals(((ListView) parent).getAdapter().getItem(position))) {
                        text_city.setText("Россия,"+s.getName());
                        user.setCityId(s.getId());
                        user.setCountryId(1);
                        getActivity().invalidateOptionsMenu();
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
                    WrapperParams wrapperParams = new WrapperParams(Wrappers.CITY);
                    wrapperParams.addParam(Keys.NAME, txt);
                    wrapperParams.addParam(Keys.COUNTRY_ID, "1");
                    loadCityTask.execute(wrapperParams, new SimpleSubscriber<List<City>>() {
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
}
