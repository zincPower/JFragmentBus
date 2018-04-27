package com.zinc.jfragmentbus;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zinc.libfragment_bus.JFragmentBus;

import static com.zinc.jfragmentbus.FragmentBusTag.FRAGMENT_ONE_SENT_TO_TWO;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/26
 * @description
 */

public class FragmentOne extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        JFragmentBus.getDefault().post(FRAGMENT_ONE_SENT_TO_TWO,"zinc");
                    }
                }.start();
            }
        });
    }

}
