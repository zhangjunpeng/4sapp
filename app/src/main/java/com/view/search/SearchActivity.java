package com.view.search;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.tools.ClearWindows;
import com.test4s.gdb.History;
import com.test4s.gdb.HistoryDao;
import com.test4s.myapp.MyApplication;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.activity.BaseActivity;
import com.view.setting.Settingfragment;

import org.xutils.common.Callback;
import org.xutils.x;

import java.util.List;

import de.greenrobot.dao.query.Query;

public class SearchActivity extends BaseActivity implements View.OnClickListener {

    FragmentManager fragmentManager;

    private EditText editText;
    private ImageView clearInput;
    private ImageView search;
    private ImageView back;
    private HistoryDao historyDao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setImmerseLayout(findViewById(R.id.titlebar_search));
        historyDao= MyApplication.daoSession.getHistoryDao();

        editText= (EditText) findViewById(R.id.edit_search);
        clearInput= (ImageView) findViewById(R.id.clearinput_search);
        search= (ImageView) findViewById(R.id.search_search);
        back= (ImageView) findViewById(R.id.back_search);
//        editText.clearFocus();


        fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
        fragmentManager.beginTransaction().replace(R.id.contianer_search,new SearchIndex()).commit();


//        getFocus(editText);
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        search.setOnClickListener(this);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()>0){
                    clearInput.setVisibility(View.VISIBLE);
                }else {
                    clearInput.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        clearInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_search:
                ClearWindows.clearInput(this,editText);
                finish();
                break;
            case R.id.search_search:
                String message=editText.getText().toString();
                if (TextUtils.isEmpty(message)) {
                }else {
                    Intent intent=new Intent(this,SearchEndActivity.class);
                    intent.putExtra("keyword",message);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_right,R.anim.out_to_left);
                    addHistory(message);
                }
                break;
        }
    }
    public void addHistory(String message){
        History history=new History();
        history.setKeyword(message);
        historyDao.insert(history);
    }



}
