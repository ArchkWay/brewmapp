package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Post;
import com.brewmapp.presentation.view.impl.widget.PostView;
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
