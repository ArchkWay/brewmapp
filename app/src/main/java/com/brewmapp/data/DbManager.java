package com.brewmapp.data;

import android.content.Context;

import io.paperdb.Paper;

public class DbManager {

    public DbManager(Context context) {

    }


    public void load() {

    }

    public void destroy() {
        Paper.book().destroy();
    }
}
