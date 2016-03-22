package com.view.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.test4s.myapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/3/22.
 */
public class SearchIndex extends Fragment{

    private int[] rmid={R.id.rm_search1,R.id.rm_search2,R.id.rm_search3,
                        R.id.rm_search4,R.id.rm_search5,R.id.rm_search6,
                        R.id.rm_search7,R.id.rm_search8,R.id.rm_search9,
                        R.id.rm_search10,R.id.rm_search11,R.id.rm_search12};
    private List<TextView> rms;
    View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        rms=new ArrayList<>();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_search_index,null);

        for (int i=0;i<rmid.length;i++){
            TextView textView= (TextView) view.findViewById(rmid[i]);
            rms.add(textView);
        }
        return view;
    }
}
