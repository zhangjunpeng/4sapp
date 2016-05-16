package com.app.tools;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Administrator on 2016/4/27.
 */
public class CusToast {

    public static  void showToast(Context context,String mess,int lenth){
        Toast toast=Toast.makeText(context,mess,lenth);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();
    }
}
