package com.test4s.net;

import android.text.TextUtils;

import com.app.tools.MD5Test;
import com.app.tools.MyLog;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/1/12.
 */
public class Url {
    //网络
    public static String  url_main="http://app.4stest.com/";

    public static String url_index=url_main+"index/index.html";

    public static String url_cplist=url_main+"index/cplist";

    public static String url_investorlist=url_main+"index/investorlist";


    //参数
    public static String key="52game.com!2015168";

    //图片前缀
    public static String prePic="";

    public static String getSign(Set<Map.Entry<String, String>> set) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        StringBuffer params = new StringBuffer();

        for ( Map.Entry<String, String> entry:set) {
            params.append(entry.getKey());
            params.append("=");
            params.append(entry.getValue());
            params.append("&");

        }
        if (params.length() > 0) {
            params.deleteCharAt(params.length() - 1);
        }
        params.append("&");
        params.append(key);
        MyLog.i("unsign===" + params);

        return MD5Test.getMD5(params.toString());

    }
}
