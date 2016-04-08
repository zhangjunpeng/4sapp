package com.view.accountsetting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tools.ClearWindows;
import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.account.UserInfo;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/2/22.
 */
public class SetAddressFragment extends BaseFragment implements View.OnClickListener{

    View view;
    private TextView selectcity;
    private EditText addr;
    private TextView save;
    private ImageView back;
    private TextView title;

    String address;

    String province;
    String province_id;
    String city;
    String city_id;
    String county;
    String county_id;

    UserInfo userInfo;

    Bundle bundle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_setaddress,null);

        setImmerseLayout(view.findViewById(R.id.title_setaddress));

        selectcity= (TextView) view.findViewById(R.id.city_setaddress);
        addr= (EditText) view.findViewById(R.id.address_setaddress);
        save= (TextView) view.findViewById(R.id.save_savebar);
        title= (TextView) view.findViewById(R.id.textView_titlebar_save);
        back= (ImageView) view.findViewById(R.id.back_savebar);

        selectcity.setOnClickListener(this);
        save.setOnClickListener(this);
        back.setOnClickListener(this);

        userInfo=MyAccount.getInstance().getUserInfo();

        title.setText("地 址");

        if (userInfo.getProvince_name().equals("")){
            selectcity.setText("点击选择省份 城市");
        }else {
            selectcity.setText(userInfo.getProvince_name()+" "+userInfo.getCity_name()+" "+userInfo.getCounty_name());
            province_id=userInfo.getProvince();
            province=userInfo.getProvince_name();
            city_id=userInfo.getCity();
            city=userInfo.getCity_name();
            county_id=userInfo.getCounty();
            county=userInfo.getCounty_name();
        }

        bundle=getArguments();
        if (bundle!=null){
            province_id=bundle.getString("province_id","");
            province=bundle.getString("province","");
            city_id=bundle.getString("city_id","");
            city=bundle.getString("city","");
            county=bundle.getString("county","");
            county_id=bundle.getString("county_id","");
            selectcity.setText(province+" "+city+" "+county);
        }


        return view;
    }

    private void changeAddress() {
        BaseParams baseParams=new BaseParams("user/chgcity");
        baseParams.addParams("token",MyAccount.getInstance().getToken());
        baseParams.addParams("province",province_id);
        baseParams.addParams("city",city_id);
        baseParams.addParams("county",county_id);
        baseParams.addParams("addr",address);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("changeaddress==="+result);
                try {
                    JSONObject js=new JSONObject(result);
                    boolean issu=js.getBoolean("success");
                    int code=js.getInt("code");
                    if (issu&&code==200){
                        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();


                        ClearWindows.clearInput(getActivity(),addr);

                        userInfo.setProvince(province_id);
                        userInfo.setCity(city_id);
                        userInfo.setCounty(county_id);
                        userInfo.setProvince_name(province);
                        userInfo.setCity_name(city);
                        userInfo.setCounty_name(county);

                        MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                        FragmentTransaction transaction= getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                        transaction.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
            case R.id.city_setaddress:
                SelectCityFragment selectCityFragment=new SelectCityFragment();
                FragmentTransaction transaction=getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left);
                transaction.replace(R.id.contianner_mysetting,selectCityFragment).commit();
                break;
            case R.id.save_savebar:
                if (TextUtils.isEmpty(province_id)||TextUtils.isEmpty(city_id)||TextUtils.isEmpty(county_id)){
                    return;
                }
                address=addr.getText().toString();
                if (TextUtils.isEmpty(address)){
                    return;
                }

                changeAddress();
                break;
            case R.id.back_savebar:
                MyAcountSettingFragment myAcountSettingFragment=new MyAcountSettingFragment();
                FragmentTransaction transaction1= getActivity().getSupportFragmentManager().beginTransaction();
                transaction1.setCustomAnimations(R.anim.in_form_left,R.anim.out_to_right);
                transaction1.replace(R.id.contianner_mysetting,myAcountSettingFragment).commit();
                break;
        }
    }
}
