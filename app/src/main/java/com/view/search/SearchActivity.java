package com.view.search;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.test4s.myapp.R;
import com.view.activity.BaseActivity;

public class SearchActivity extends BaseActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setImmerseLayout(findViewById(R.id.titlebar_search));
        fragmentManager=getSupportFragmentManager();

    }

}
