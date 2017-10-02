package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.model.ICommonItem;
import com.brewmapp.presentation.view.impl.widget.CommonItemView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

/**
 * Created by oleg on 30.09.17.
 */

public class CommonItemInfo extends AdapterItem<ICommonItem, CommonItemView> {

    public CommonItemInfo(ICommonItem model) {
        super(model);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.view_common_item;
    }

    public static List<CommonItemInfo> prepareItems(Collection<? extends ICommonItem> items) {
        List<CommonItemInfo> itemInfos = new ArrayList<>();
        for(ICommonItem iCommonItem: items) {
            itemInfos.add(new CommonItemInfo(iCommonItem));
        }

        return itemInfos;
    }
}
