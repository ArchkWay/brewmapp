package nl.psdcompany.duonavigationdrawer.widgets;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.view.View;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;

/**
 * Created by ovcst on 03.08.2017.
 */

public class CustomDuoDrawerToggle extends DuoDrawerToggle {

    public interface Listener {
        void onDrawerClosed();
    }

    private Listener listener;

    public  <T extends Drawable & DrawerToggle> CustomDuoDrawerToggle(
            Activity activity, Toolbar toolbar,
            DuoDrawerLayout duoDrawerLayout, T slider, @StringRes int openDrawerContentDescRes,
            @StringRes int closeDrawerContentDescRes, Listener listener) {
        super(activity, toolbar, duoDrawerLayout, slider, openDrawerContentDescRes, closeDrawerContentDescRes);
        this.listener = listener;
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        listener.onDrawerClosed();
    }
}
