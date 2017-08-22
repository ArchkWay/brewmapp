package com.brewmapp.execution.exchange.common.base;

import com.brewmapp.data.entity.Post;
import com.brewmapp.data.entity.wrapper.PostInfo;
import ru.frosteye.ovsa.execution.serialization.AdapterItemDeserializer;

/**
 * Created by oleg on 16.08.17.
 */

public class PostDeserializer extends AdapterItemDeserializer<Post, PostInfo> {
    @Override
    protected Class<Post> provideType() {
        return Post.class;
    }

    @Override
    protected Class<PostInfo> provideWrapperType() {
        return PostInfo.class;
    }
}
