package com.brewmapp.data.model;

import android.support.annotation.DrawableRes;

import java.io.File;

/**
 * Created by oleg on 30.09.17.
 */

public interface ICommonItem {

    String title();
    int id();
    ImageSource image();

    interface ImageSource {
        enum Source {
            FILE, URL, RESOURCE
        }
        Source source();
        File file();
        String url();
        @DrawableRes int resource();
    }
}
