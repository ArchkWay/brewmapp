package ru.frosteye.beermap.execution.social;

import android.os.AsyncTask;

/**
 * Created by oleg che on 21.03.16.
 */
@Deprecated
public class BaseSocialLoader<Params, Result> extends AsyncTask<Params, Void, Result> {
    private Exception exception;
    @Override
    protected Result doInBackground(Params... params) {
        return null;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
