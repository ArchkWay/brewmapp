package nl.psdcompany.duonavigationdrawer.views;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ScrollerCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by xpusher on 3/14/2018.
 */

public class CustomDuoDrawerLayout extends DuoDrawerLayout {
    public CustomDuoDrawerLayout(Context context) {
        super(context);
    }

    public CustomDuoDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDuoDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void openDrawer() {
        super.openDrawer();
        breakAnimation();
    }

    @Override
    public void closeDrawer() {
        super.closeDrawer();
        breakAnimation();
    }

    private void breakAnimation(){
        try {
            Field f_mViewDragHelper=getClass().getSuperclass().getDeclaredField("mViewDragHelper");
            f_mViewDragHelper.setAccessible(true);
            Object o_mViewDragHelper=f_mViewDragHelper.get(this);

            Field f_mScroller=o_mViewDragHelper.getClass().getDeclaredField("mScroller");
            f_mScroller.setAccessible(true);
            ScrollerCompat mScroller= (ScrollerCompat) f_mScroller.get(o_mViewDragHelper);

            mScroller.abortAnimation();
            ViewCompat.postInvalidateOnAnimation(CustomDuoDrawerLayout.this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
