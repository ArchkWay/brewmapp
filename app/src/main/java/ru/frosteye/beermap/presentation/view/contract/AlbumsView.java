package ru.frosteye.beermap.presentation.view.contract;

import ru.frosteye.beermap.data.entity.container.Albums;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface AlbumsView extends BasicView {
    void showAlbums(Albums albums);
}
