package com.brewmapp.presentation.view.contract;

import android.support.v4.app.Fragment;

import com.brewmapp.data.entity.User;
import com.brewmapp.presentation.view.impl.fragment.BaseFragment;

import ru.frosteye.ovsa.presentation.view.BasicView;

/**
 * Created by xpusher on 11/20/2017.
 */

public interface MultiFragmentActivityView extends BasicView{
    void commonError(String... strings);

    String MODE_ABOUT="0";
    String MODE_WEBVIEW ="1";
    String MODE_BEER_EDIT ="2";
    String MODE_RESTO_EDIT ="3";
    String MODE_CHAT ="4";
    String MODE_FORM_I_OWNER ="5";

    void showFragment(Fragment fragment);
}
