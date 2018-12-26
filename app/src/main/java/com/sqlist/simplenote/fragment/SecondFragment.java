package com.sqlist.simplenote.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sqlist.simplenote.R;

/**
 * Created by asus on 2018/1/8.
 */

public class SecondFragment extends Fragment {

    private Callbacks callbacks;

    public interface Callbacks
    {
        void onClick(View view);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);

        final Button md5Btn = view.findViewById(R.id.md5);
        md5Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onClick(md5Btn);
            }
        });

        final Button base64Btn = view.findViewById(R.id.base64);
        base64Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onClick(base64Btn);
            }
        });

        final Button readBtn = view.findViewById(R.id.read);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onClick(readBtn);
            }
        });

        final Button randomBtn = view.findViewById(R.id.color);
        randomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbacks.onClick(randomBtn);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks))
        {
            throw new IllegalStateException("SecondFragment所在的Activity必须实现Callbacks接口");
        }
        callbacks = (Callbacks) context;
    }
}
