package com.test4s.myapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test4s.activity.BaseActivity;
import com.test4s.fragment.GameFragment;
import com.test4s.fragment.IndexFragment;
import com.test4s.fragment.InformationFragment;
import com.test4s.fragment.MySettingFragment;
import com.test4s.gdb.CPDao;
import com.test4s.gdb.DaoMaster;
import com.test4s.gdb.DaoSession;

import org.xutils.DbManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements View.OnClickListener{

    DaoSession daoSession;
    private List<Fragment> fragments;
    FragmentManager fm;

    TextView title_bar;

    String[] titles={"首页","游戏","资讯","我的"};

    //底部导航栏
    List<ImageView> imageViewList;
    List<TextView>  textViewList;

    LinearLayout indexLinear;
    LinearLayout gameLinear;
    LinearLayout infoLinear;
    LinearLayout myLinear;

    ImageView indexImage;
    ImageView gameImage;
    ImageView infoImage;
    ImageView myImage;

    TextView indexText;
    TextView gameText;
    TextView infoText;
    TextView myText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBottomView();

        title_bar= (TextView) findViewById(R.id.title_titlebar);

        fm=getSupportFragmentManager();

        daoSession=MyApplication.daoSession;
        CPDao cpDao=daoSession.getCPDao();

        fragments=new ArrayList<>();

        Fragment indexFragment=new IndexFragment();
        Fragment gameFragment=new GameFragment();
        Fragment informationFragment=new InformationFragment();
        Fragment mySettingFragment=new MySettingFragment();


        fragments.add(indexFragment);
        fragments.add(gameFragment);
        fragments.add(informationFragment);
        fragments.add(mySettingFragment);

        setImageColor(0);
        fm.beginTransaction().replace(R.id.frameLayout_main,indexFragment).commit();

    }

    private void initBottomView() {

        indexLinear= (LinearLayout) findViewById(R.id.index_linear);
        gameLinear= (LinearLayout) findViewById(R.id.game_linear);
        infoLinear= (LinearLayout) findViewById(R.id.info_linear);
        myLinear= (LinearLayout) findViewById(R.id.my_linear);

        indexLinear.setOnClickListener(this);
        gameLinear.setOnClickListener(this);
        infoLinear.setOnClickListener(this);
        myLinear.setOnClickListener(this);


        imageViewList=new ArrayList<>();
        textViewList=new ArrayList<>();

        indexImage= (ImageView) findViewById(R.id.index_image);
        gameImage= (ImageView) findViewById(R.id.game_image);
        infoImage= (ImageView) findViewById(R.id.info_imgae);
        myImage= (ImageView) findViewById(R.id.my_image);

        imageViewList.add(indexImage);
        imageViewList.add(gameImage);
        imageViewList.add(infoImage);
        imageViewList.add(myImage);

        indexText= (TextView) findViewById(R.id.text_index);
        gameText= (TextView) findViewById(R.id.text_game);
        infoText= (TextView) findViewById(R.id.text_info);
        myText= (TextView) findViewById(R.id.text_my);

        textViewList.add(indexText);
        textViewList.add(gameText);
        textViewList.add(infoText);
        textViewList.add(myText);

    }

    private void setImageColor(int position){
        title_bar.setText(titles[position]);
        if (imageViewList==null||textViewList==null){
            return;
        }

        for (int i=0;i<imageViewList.size();i++){
            imageViewList.get(i).setSelected(false);
            textViewList.get(i).setTextColor(Color.rgb(76,76,76));

        }

        imageViewList.get(position).setSelected(true);
        textViewList.get(position).setTextColor(Color.rgb(55,229,144));


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.index_linear:
                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(0)).commit();
                setImageColor(0);
                break;
            case R.id.game_linear:
                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(1)).commit();
                setImageColor(1);
                break;
            case R.id.info_linear:
                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(2)).commit();
                setImageColor(2);
                break;
            case R.id.my_linear:
                fm.beginTransaction().replace(R.id.frameLayout_main,fragments.get(3)).commit();
                setImageColor(3);
                break;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_main,menu);
        return true;
    }
}