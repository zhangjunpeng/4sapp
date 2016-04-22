package com.view.accountsetting;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;

import org.w3c.dom.Text;

/**
 * Created by Administrator on 2016/3/7.
 */
public class BindotherFragment extends BaseFragment implements View.OnClickListener {

    View view;

    private LinearLayout weixin_linear;
    private LinearLayout qq_linear;
    private LinearLayout sina_linear;

    private TextView weixin_text;
    private TextView qq_text;
    private TextView sina_text;

    private com.test4s.account.UserInfo userinfo;

    private ImageView back;
    private TextView title;
    private TextView save;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_bindother,null);

        setImmerseLayout(view.findViewById(R.id.title_bindother));

        weixin_linear= (LinearLayout) view.findViewById(R.id.linear_weixin_bind);
        qq_linear= (LinearLayout) view.findViewById(R.id.linear_qq_bind);
        sina_linear= (LinearLayout) view.findViewById(R.id.linear_sina_bind);

        weixin_text= (TextView) view.findViewById(R.id.text_weixin_bind);
        qq_text= (TextView) view.findViewById(R.id.text_qq_bind);
        sina_text= (TextView) view.findViewById(R.id.text_sina_bind);

        userinfo= MyAccount.getInstance().getUserInfo();

        weixin_linear.setOnClickListener(this);
        qq_linear.setOnClickListener(this);
        sina_linear.setOnClickListener(this);

        initView();

        return view;
    }

    private void initView() {
        MyLog.i("qq_sina=="+userinfo.getQq_sign());
        MyLog.i("weixin_sina=="+userinfo.getWeixin_sign());
        MyLog.i("sina=="+userinfo.getSina_sign());


        if (!TextUtils.isEmpty(userinfo.getQq_sign())){
            qq_linear.setClickable(false);
            qq_text.setText("已绑定");
            qq_text.setTextColor(Color.rgb(124,124,124));
        }
        if (!TextUtils.isEmpty(userinfo.getWeixin_sign())){
            weixin_linear.setClickable(false);
            weixin_text.setText("已绑定");
            qq_text.setTextColor(Color.rgb(124,124,124));

        }
        if (!TextUtils.isEmpty(userinfo.getSina_sign())){
            sina_linear.setClickable(false);
            sina_text.setText("已绑定");
            qq_text.setTextColor(Color.rgb(124,124,124));

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_weixin_bind:
                break;
            case R.id.linear_qq_bind:
                break;
            case R.id.linear_sina_bind:
                break;
        }
    }
}
