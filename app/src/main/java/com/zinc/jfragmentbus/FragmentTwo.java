package com.zinc.jfragmentbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zinc.libfragment_bus.JFragmentBus;
import com.zinc.libfragment_bus.ThreadMode;
import com.zinc.libfragment_bus.annotation.Subscribe;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description
 */

public class FragmentTwo extends Fragment {

    TextView tv_content;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JFragmentBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv_content = view.findViewById(R.id.tv_content);
    }

    @Subscribe(value = FragmentBusTag.FRAGMENT_ONE_SENT_TO_TWO,threadMode = ThreadMode.MAIN)
    public void test(String content) {
        Toast.makeText(getContext(), "收到" + content, Toast.LENGTH_SHORT).show();
        tv_content.setText("收到信息" + content);
//        Log.i("zinc", "test: "+content);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        JFragmentBus.getDefault().unregister(this);
    }
}
