package ru.frosteye.beermap.execution.social;

/**
 * Created by oleg che on 19.03.16.
 */
public class SocialException extends Exception {
    private int network;
    private String message;

    public SocialException(int network, String message) {
        this.network = network;
        this.message = message;
    }

    public int getNetwork() {
        return network;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
