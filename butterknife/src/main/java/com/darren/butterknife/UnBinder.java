package com.darren.butterknife;

import androidx.annotation.UiThread;

/**
 * date  3/19/21  10:05 AM
 * author  DarrenHang
 */
public interface UnBinder {
    @UiThread
    void unbind();

    UnBinder EMPTY = () -> { };
}
