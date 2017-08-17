package ru.frosteye.beermap.data.entity.wrapper;

import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.presentation.view.impl.widget.PostView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class PostInfo extends AdapterItem<Post, PostView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_post;
    }
}
