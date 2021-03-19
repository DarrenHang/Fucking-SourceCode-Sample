package com.darren.sourcecode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.darren.butterknife.ButterKnife;
import com.darren.butterknife_annotation.BindView;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tv_text)
    public TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mTextView.setText("IOC");
    }

}