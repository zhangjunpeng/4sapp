package com.view.Evaluation;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tools.MyLog;
import com.test4s.account.MyAccount;
import com.test4s.myapp.R;
import com.test4s.net.BaseParams;
import com.view.messagecenter.DeleListView;

import org.xutils.common.Callback;
import org.xutils.x;

/**
 * A placeholder fragment containing a simple view.
 */
public class EvaluationListFragment extends Fragment {

   View view;

    private DeleListView deleListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_evaluationlist, container, false);
        deleListView= (DeleListView) view.findViewById(R.id.listview_pclist);
        
        initData("1");
        return view;
    }

    private void initData(String p) {
        BaseParams baseParams=new BaseParams("test/gamelist");
        baseParams.addParams("token", MyAccount.getInstance().getToken());
        baseParams.addParams("p",p);
        baseParams.addSign();
        x.http().post(baseParams.getRequestParams(), new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                MyLog.i("pclist==="+result);
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
}
