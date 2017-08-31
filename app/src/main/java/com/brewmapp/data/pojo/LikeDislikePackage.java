package com.brewmapp.data.pojo;

import com.brewmapp.data.entity.contract.Postable;
import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;

/**
 * Created by oleg on 31.08.17.
 */

public class LikeDislikePackage implements Postable {
    public static final int TYPE_LIKE = 1;
    public static final int TYPE_DISLIKE = 0;
    private int type;

    private String model;
    private int modelId;

    public void setModel(String model, int modelId) {
        this.model = model;
        this.modelId = modelId;
    }

    public LikeDislikePackage(int type) {
        this.type = type;
    }

    @Override
    public WrapperParams createParams() {
        WrapperParams params = new WrapperParams(Wrappers.LIKE);
        params.addParam(Keys.TYPE, type);
        params.addParam(Keys.RELATED_MODEL, model);
        params.addParam(Keys.RELATED_ID, modelId);
        params.addParam(Keys.TYPE, type);
        return params;
    }
}
