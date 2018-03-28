package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.MenuResto;
import com.brewmapp.presentation.view.impl.widget.MenuRestoView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;

public class MenuInfo extends AdapterItem<MenuResto, MenuRestoView> {
    public MenuInfo(MenuResto menuResto) {
        setModel(menuResto);
    }
    public MenuInfo() {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_menu_resto;
    }
}
