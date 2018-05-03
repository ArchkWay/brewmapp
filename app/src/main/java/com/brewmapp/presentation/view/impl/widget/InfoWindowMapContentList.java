package com.brewmapp.presentation.view.impl.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.brewmapp.R;
import com.brewmapp.data.entity.FilterRestoLocation;
import com.brewmapp.presentation.view.contract.InfoWindowMapContent_view;
import com.brewmapp.presentation.view.impl.fragment.MapFragment;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.Cluster;

import java.util.Collection;
import java.util.Iterator;

import ru.frosteye.ovsa.presentation.view.widget.BaseLinearLayout;

public class InfoWindowMapContentList extends BaseLinearLayout implements InfoWindowMapContent_view {

    private int cntCompleateRequest=0;
    private Handler.Callback listenerFinishLoadData;



    public InfoWindowMapContentList(Context context) {
        super(context);
    }

    public InfoWindowMapContentList(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoWindowMapContentList(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InfoWindowMapContentList(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void prepareView() {

    }

    @Override
    public void requestData() {
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.content_info_window_info_list);

        for (int i=0;i<linearLayout.getChildCount();i++){
            InfoWindowMapContent infoWindowMap= (InfoWindowMapContent) linearLayout.getChildAt(i);
            infoWindowMap.setListenerFinishLoadData(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    cntCompleateRequest++;
                    if(linearLayout.getChildCount()==cntCompleateRequest){
                        listenerFinishLoadData.handleMessage(new Message());
                    }
                    return false;
                }
            });
            infoWindowMap.requestData();
        }

    }

    @Override
    public void setListenerFinishLoadData(Handler.Callback listenerFinishLoadData) {
        this.listenerFinishLoadData = listenerFinishLoadData;
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public void setMarker(Marker marker) {

    }

    public void setContent(Cluster<FilterRestoLocation> cluster, MapFragment mapFragment) {
        LinearLayout linearLayout= (LinearLayout) findViewById(R.id.content_info_window_info_list);
        Collection<FilterRestoLocation> collection=cluster.getItems();
        Iterator<FilterRestoLocation> iterator=collection.iterator();
        while (iterator.hasNext()) {
            InfoWindowMapContent infoWindowMap=mapFragment.createInfoWindowForOneResto(iterator.next());
            linearLayout.addView(infoWindowMap);
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
    }
}
