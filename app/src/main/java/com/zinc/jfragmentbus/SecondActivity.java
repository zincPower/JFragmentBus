package com.zinc.jfragmentbus;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.zinc.libfragment_bus.JFragmentBus;
import com.zinc.libfragment_bus.annotation.Subscribe;

import static com.zinc.jfragmentbus.FragmentBusTag.FRAGMENT_ONE_SENT_TO_TWO;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description
 */

public class SecondActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        JFragmentBus.getDefault().register(this);

        Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JFragmentBus.getDefault().post(FRAGMENT_ONE_SENT_TO_TWO, "zinc");
            }
        });

    }

    @Subscribe(FragmentBusTag.FRAGMENT_ONE_SENT_TO_TWO)
    public void second(String content) {
        Toast.makeText(this, "收到" + content, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        JFragmentBus.getDefault().unregister(this);
    }
}
