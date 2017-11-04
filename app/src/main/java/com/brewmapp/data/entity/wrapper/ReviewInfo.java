package com.brewmapp.data.entity.wrapper;

import com.brewmapp.R;
import com.brewmapp.data.entity.Review;
import com.brewmapp.presentation.view.impl.widget.ReviewView;
import ru.frosteye.ovsa.presentation.adapter.AdapterItem;


/**
 * Created by Kras on 04.11.2017.
 */

public class ReviewInfo extends AdapterItem<Review, ReviewView> {
    @Override
    public int getLayoutRes() {
        return R.layout.view_review;
    }
}
