package ru.frosteye.beermap.execution.social;

/**
 * Created by oleg on 14.12.16.
 */

public class SocialResult<T> {
    private boolean succesful;
    private T payload;

    public SocialResult() {
    }

    public SocialResult(boolean succesful, T payload) {
        this.succesful = succesful;
        this.payload = payload;
    }

    public boolean isSuccesful() {
        return succesful;
    }

    public T getPayload() {
        return payload;
    }

    public void setSuccesful(boolean succesful) {
        this.succesful = succesful;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
