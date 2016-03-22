package com.view.myattention;

import com.test4s.account.MyAccount;
import com.test4s.net.BaseParams;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by Administrator on 2016/3/21.
 */
public class AttentionChange {
    public static void addAttention(String identity_cat, String care_id, Callback.CommonCallback<String> callback){
        BaseParams baseParams=new BaseParams("care/care4s");
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addParams("care_id",care_id);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("caretype","care");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(),callback);
    }
    public static void removeAttention(String identity_cat,String care_id,Callback.CommonCallback<String> callback){
        BaseParams baseParams=new BaseParams("care/care4s");
        baseParams.addParams("identity_cat",identity_cat);
        baseParams.addParams("care_id",care_id);
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("caretype","uncare");
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(),callback);
    }
}
