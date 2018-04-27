package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.container.Albums;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface AlbumsView extends BasicView {
    void showAlbums(Albums albums);

    void commonError(String... strings);

    void removeAlbumSuccess();
}
