package ru.frosteye.beermap.data.entity.wrapper;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.Album;
import ru.frosteye.beermap.presentation.view.impl.widget.AlbumHeaderView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 16.08.17.
 */

public class AlbumInfo extends AdapterItem<Album, AlbumHeaderView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_album_header;
    }
}
