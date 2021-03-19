package com.darren.butterknife;

import android.app.Activity;
import android.view.View;

import androidx.annotation.IdRes;

/**
 * date  3/19/21  10:22 AM
 * author  DarrenHang
 */
public class Utils {
    public static  <T> T findViewById(Activity target, @IdRes int id, Class<T> cls) {
        View view = target.findViewById(id);
        return cls.cast(view);
    }

}
