package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.entity.Post;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by Kras on 15.10.2017.
 */

public interface PostDetailsView extends BasicView {
    void fillContent(Post post);
}
