package com.brewmapp.data.pojo;

import com.brewmapp.execution.exchange.request.base.Keys;
import com.brewmapp.execution.exchange.request.base.WrapperParams;
import com.brewmapp.execution.exchange.request.base.Wrappers;

/**
 * Created by oleg on 25.09.17.
 */

public class ClaimPackage {
    private int variantId;
    private int modelId;
    private String model;
    private String text;

    public ClaimPackage(int id, int modelId, String model) {
        this.variantId = id;
        this.modelId = modelId;
        this.model = model;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getVariantId() {
        return variantId;
    }

    public int getModelId() {
        return modelId;
    }

    public String getModel() {
        return model;
    }
}
