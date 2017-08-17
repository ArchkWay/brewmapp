package ru.frosteye.beermap.data.entity.wrapper;

import eu.davidea.flexibleadapter.items.IFilterable;
import ru.frosteye.beermap.R;
import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.presentation.view.impl.widget.FriendsTitleView;
import ru.frosteye.beermap.presentation.view.impl.widget.PostView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 17.08.17.
 */

public class FriendsTitleInfo extends AdapterItem<String, FriendsTitleView> implements IFilterable {

    public FriendsTitleInfo(String model) {
        super(model);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_friends_title;
    }

    @Override
    public boolean filter(String constraint) {
        return false;
    }
}
