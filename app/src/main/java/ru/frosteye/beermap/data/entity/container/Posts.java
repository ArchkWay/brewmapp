package ru.frosteye.beermap.data.entity.container;

import android.support.annotation.NonNull;

import java.util.List;

import ru.frosteye.beermap.data.entity.Post;
import ru.frosteye.beermap.data.entity.wrapper.PostInfo;
import ru.frosteye.beermap.execution.exchange.response.base.ListResponse;

/**
 * Created by oleg on 17.08.17.
 */

public class Posts extends ListResponse<PostInfo> {

    public Posts(@NonNull List<PostInfo> models) {
        super(models);
    }
}
