package com.darren.butterknife;

import android.app.Activity;

import java.lang.reflect.Constructor;

/**
 * date  3/19/21  9:35 AM
 * author  DarrenHang
 */
public class ButterKnife {
    public static UnBinder bind(Activity activity) {
        try {
            Class<?> clazz = Class.forName(activity.getClass().getName() + "_ViewBinding");
            Constructor<?> cons = clazz.getConstructor(activity.getClass());
            UnBinder unbinder = (UnBinder) cons.newInstance(activity);
            return unbinder;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return UnBinder.EMPTY;
    }
}
