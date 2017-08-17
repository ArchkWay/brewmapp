package ru.frosteye.beermap.presentation.view.contract;

import ru.frosteye.beermap.data.entity.container.AlbumPhotos;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface AlbumView extends BasicView {
    void showPhotos(AlbumPhotos photos);
}
