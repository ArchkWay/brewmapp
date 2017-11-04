package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Review;
import com.brewmapp.data.entity.wrapper.ReviewInfo;

import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by Kras on 05.11.2017.
 */

public class ReviewDeserializer extends AdapterItemDeserializer<Review, ReviewInfo> {
    @Override
    protected Class<Review> provideType() {
        return Review.class;
    }

    @Override
    protected Class<ReviewInfo> provideWrapperType() {
        return ReviewInfo.class;
    }
}
