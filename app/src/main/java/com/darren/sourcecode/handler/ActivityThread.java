package com.darren.sourcecode.handler;



/**
 * date  1/12/21  2:21 PM
 * author  DarrenHang
 */
public class ActivityThread {

    final H mH = new H();

    /**
     * 主入口
     * @param args
     */
    public static void main(String[] args) {
        Looper.prepare();
        ActivityThread thread = new ActivityThread();
        thread.attach(false);
        Looper.loop();
    }

    private void attach(boolean b) {
        Activity testActivity = new TestActivity();
        testActivity.onCreate();

        Message message = new Message();
        message.obj = testActivity;
        //通过 Handler 执行生命周期
        mH.sendMessage(message);
    }

    class H extends Handler {

        public void handleMessage(Message msg) {
            Activity activity = (Activity) msg.obj;
            activity.onResume();
        }

    }

}
