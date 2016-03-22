package com.view.setting;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.tools.MyLog;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/18.
 */
public class aboutusfragment extends Fragment implements View.OnClickListener {

    SettingActivity activity;

    LinearLayout phone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        activity= (SettingActivity) getActivity();
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_aboutus,null);
        initData();
        view.findViewById(R.id.service_deal_aboutus).setOnClickListener(this);
        phone= (LinearLayout) view.findViewById(R.id.phone_aboutus);
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
//                EditText et_phonenumber = (EditText)findViewById(R.id.phonenumber);

                String phonenum=getString(R.string.phone_kefu);
                //用intent启动拨打电话
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phonenum));
                startActivity(intent);
            }
        });
        return view;
    }

    private void initData() {
        BaseParams baseParams=new BaseParams("setting/singlepage");
        baseParams.addParams("singleCat","aboutus");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("aboutus=="+result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.service_deal_aboutus:
                activity.toServiceDeal();
                break;
        }
    }

}
