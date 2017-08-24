package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Album;
import com.brewmapp.data.entity.Event;
import com.brewmapp.presentation.view.impl.widget.AlbumHeaderView;
import com.brewmapp.presentation.view.impl.widget.EventView;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 16.08.17.
 */

public class EventInfo extends AdapterItem<Event, EventView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_event;
    }
}
