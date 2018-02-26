package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Album;
import com.brewmapp.presentation.view.impl.widget.AlbumHeaderView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumInfo extends AdapterItem<Album, AlbumHeaderView> {

    public AlbumInfo(Album model) {
        super(model);
    }

    public AlbumInfo() {
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_album_header;
    }
}
