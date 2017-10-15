package com.brewmapp.presentation.view.contract;

import com.brewmapp.data.model.ILikeable;

/**
 * Created by Kras on 15.10.2017.
 */

public interface ShareDialog {
    void showShareDialog(int items, ILikeable iLikeable);
    void onDelete();
}
