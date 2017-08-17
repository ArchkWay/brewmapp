package ru.frosteye.beermap.execution.exchange.request.base;

import java.io.File;

import ru.frosteye.ovsa.execution.network.request.MultipartRequestParams;
import ru.frosteye.ovsa.execution.network.request.RequestParams;

/**
 * Created by oleg on 16.08.17.
 */

public class WrapperMultipartParams extends MultipartRequestParams {
    private String wrapper;

    public WrapperMultipartParams(String wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public MultipartRequestParams addPart(String key, String value) {
        return super.addPart(wrapper + "[" + key + "]", value);
    }

    @Override
    public MultipartRequestParams addPart(String key, int value) {
        return super.addPart(wrapper + "[" + key + "]", value);
    }

    @Override
    public MultipartRequestParams addPart(String key, double value) {
        return super.addPart(wrapper + "[" + key + "]", value);
    }

    @Override
    public MultipartRequestParams addPart(String key, boolean value) {
        return super.addPart(wrapper + "[" + key + "]", value);
    }

    @Override
    public MultipartRequestParams addPart(String key, File file) {
        return super.addPart(wrapper + "[" + key + "]", file);
    }
}
