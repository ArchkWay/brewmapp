package com.brewmapp.presentation.view.impl.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.brewmapp.R;
import com.brewmapp.app.environment.BeerMap;
import com.brewmapp.data.entity.BeerBrand;
import com.brewmapp.data.entity.City;
import com.brewmapp.data.entity.wrapper.BeerBrandInfo;
import com.brewmapp.data.pojo.FullSearchPackage;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.task.FullSearchTask;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.Iterator;
import java.util.List;

import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.execution.task.SimpleSubscriber;
import ru.frosteye.ovsa.stub.view.RefreshableSwipeRefreshLayout;


/**
 * Created by Kras on 09.12.2017.
 */

public class DialogSearchBrand extends DialogFragment {
    private ListView lv;
    private SearchView sv;
    private CustomArrayAdapter adapter;
    private RefreshableSwipeRefreshLayout refreshableSwipeRefreshLayout;
    private FullSearchTask fullSearchTask;
    private FullSearchPackage fullSearchPackage;
    private OnSelectBrand onSelectBrand;
    private dataItem[] dataItems={};
    private List<IFlexible> iFlexiblesResult;

    public void show(FragmentManager fragmentManager,OnSelectBrand onSelectBrand){
        this.onSelectBrand=onSelectBrand;
        fullSearchPackage=new FullSearchPackage();
        fullSearchPackage.setType(Keys.TYPE_BEERBRAND);
        fullSearchTask=new FullSearchTask(
                BeerMap.getAppComponent().mainThread(),
                BeerMap.getAppComponent().executor(),
                BeerMap.getAppComponent().api()
        );

        show(fragmentManager,"qqq");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(R.string.title_dialog_brand);
        View rootView=inflater.inflate(R.layout.dialog_select_country, null);
        refreshableSwipeRefreshLayout= (RefreshableSwipeRefreshLayout) rootView.findViewById(R.id.fragment_dialog_swipe);
        lv=(ListView) rootView.findViewById(R.id.listView1);
        sv=(SearchView) rootView.findViewById(R.id.searchView1);
        sv.setIconified(false);
        lv.setAdapter(adapter);
        sv.setOnCloseListener(() -> {dismiss();return false;});
        lv.setOnItemClickListener((parent, view, position, id) -> {
            onSelectBrand.onSelect(((BeerBrandInfo)iFlexiblesResult.get(position)).getModel());
            dismiss();
        });
        sv.setQueryHint(getString(R.string.text_start_search));
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String txt) {

                if(txt.length()>0) {
                    refreshableSwipeRefreshLayout.setRefreshing(true);
                    fullSearchPackage.setStringSearch(txt);
                    fullSearchTask.execute(fullSearchPackage,new SimpleSubscriber<List<IFlexible>>(){
                        @Override
                        public void onNext(List<IFlexible> iFlexibles) {
                            super.onNext(iFlexibles);
                            iFlexiblesResult=iFlexibles;
                            dataItems=new dataItem[iFlexibles.size()];
                            for (int i=0;i<iFlexibles.size();i++) {
                                dataItem dataItem=new dataItem();
                                dataItem.text = ((BeerBrandInfo) iFlexibles.get(i)).getModel().getName();
                                dataItem.image = ((BeerBrandInfo) iFlexibles.get(i)).getModel().getGetThumb();
                                dataItems[i]=dataItem;
                            }
                            adapter = new CustomArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, dataItems);
                            lv.setAdapter(adapter);
                            refreshableSwipeRefreshLayout.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            super.onError(e);
                            resetList();
                        }
                    });
                }else {
                    resetList();
                }


                if(adapter!=null)
                    adapter.getFilter().filter(txt);
                return false;
            }
        });

        return rootView;
    }

    private void resetList() {
        adapter = new CustomArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, new dataItem[0]);
        lv.setAdapter(adapter);
        refreshableSwipeRefreshLayout.setRefreshing(false);
    }

    public interface OnSelectBrand{
        public void onSelect(BeerBrand beerBrand);
    }
    //***********************************************
    class CustomArrayAdapter extends ArrayAdapter{
        dataItem[] objects;

        public CustomArrayAdapter(@NonNull Context context, int resource, @NonNull dataItem[] objects) {
            super(context, resource, objects);
            this.objects=objects;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            dataItem dataItem=dataItems[position];
            if(convertView==null)
                convertView= super.getView(position, convertView, parent);
            ((TextView)convertView).setText(dataItem.text);
            if(dataItem.image!=null) {
                View finalConvertView = convertView;
                Picasso.with(getActivity()).load(dataItem.image).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        ImageView imageView=new ImageView(getActivity());
                        imageView.setImageBitmap(bitmap);
                        ((TextView) finalConvertView).setCompoundDrawablesWithIntrinsicBounds(null, null, null, imageView.getDrawable());
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });
            }

            return convertView;
        }
    }

    class dataItem{
        String text;
        String image;
    }

}
