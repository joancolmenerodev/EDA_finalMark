package com.appenjoyer.eda;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Joan on 24/05/2016.
 */
public class ShowCustomToast {

    static void ShowToast(Activity c, String str){
        LayoutInflater inflater = LayoutInflater.from(c);
        View toastLayout = inflater.inflate(R.layout.custom_toast, (ViewGroup) c.findViewById(R.id.custom_toast_layout));
        TextView tv = (TextView) toastLayout.findViewById(R.id.custom_toast_message);
        tv.setText(str);
        Toast toast = new Toast(c);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(toastLayout);
        toast.show();
    }
}
