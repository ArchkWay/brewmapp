package com.brewmapp.presentation.view.contract;

import android.content.Context;

import java.util.List;

import eu.davidea.flexibleadapter.items.IFlexible;
import ru.frosteye.ovsa.presentation.view.BasicView;

public interface MessageFragmentView extends BasicView {
    void showFriends(List<IFlexible> list);

    Context getActivity();

    void commonError(String... messages);
}
