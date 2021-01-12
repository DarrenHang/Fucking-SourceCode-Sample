package com.darren.sourcecode.handler;

/**
 * date  1/12/21  2:16 PM
 * author  DarrenHang
 */
public class TestActivity extends Activity {

    TextView textView;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText((String) msg.obj);
            System.out.println("线程名称"+Thread.currentThread());
        }
    };

    @Override
    protected void onCreate() {
        super.onCreate();
        System.out.println("----- onCreate -----");
        textView = findViewById();
        new Thread() {
            @Override
            public void run() {
                super.run();
                System.out.println("线程名称"+Thread.currentThread());
                Message message = new Message();
                message.obj = "测试";
                handler.sendMessage(message);
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("----- onResume -----");
    }
}
