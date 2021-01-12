package com.darren.sourcecode.handler;

/**
 * date  1/12/21  2:17 PM
 * author  DarrenHang
 */
public class TextView {

    private Thread mThread;

    public TextView(){
        mThread = Thread.currentThread();
    }

    public void setText(CharSequence text){
        checkThread();
        System.out.println("----- TextView更新UI成功 -----");
    }

    void checkThread() {
        if (mThread != Thread.currentThread()) {
            throw new RuntimeException(
                    "Only the original thread that created a view hierarchy can touch its views.");
        }
    }

}
