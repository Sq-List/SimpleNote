package com.sqlist.simplenote.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sqlist.simplenote.MainActivity;
import com.sqlist.simplenote.R;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by asus on 2018/1/8.
 */

public class FirstFragment extends Fragment {

    private static final String TAG = "-SimpleNote-";
    private Callbacks callbacks;

    private List<Map<String, Object>> params;

    public interface Callbacks
    {
        void onItemSelected(int position);
        void onItemLongSelect(int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d(TAG, "FirstFragment onCreateView");

        params = ((MainActivity)getActivity()).getData();
        for (int i = 0; i < params.size(); i++)
        {
            Map<String, Object> tmp = params.get(i);
            String content = (String) tmp.get("content");
            String temp = content.replaceAll("/storage/emulated/0.+?\\.\\w{3}", "[图片]");
            String showContent = temp.replaceAll("\n", " ");
            Log.d(TAG, showContent);
            tmp.put("showContent", showContent);
        }

        View view = inflater.inflate(R.layout.first_fragment, container, false);
        ListView listView = (ListView) view.findViewById(R.id.firstFragmentListView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                callbacks.onItemSelected(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                callbacks.onItemLongSelect(position);
                return true;
            }
        });
        SimpleAdapter simpleAdapter  = new SimpleAdapter(getActivity(), params,
                R.layout.first_fragment_item,
                new String[] {"showContent", "time"},
                new int[] {R.id.content, R.id.time});
        listView.setAdapter(simpleAdapter);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!(context instanceof Callbacks))
        {
            throw new IllegalStateException("FirstFragment所在的Activity必须实现Callbacks接口");
        }
        callbacks = (Callbacks) context;
    }
}
