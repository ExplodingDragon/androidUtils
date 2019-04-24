package top.fksoft.test.android;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
    public static void showToast(Context context,int resId,Object ... data){
        showToast(context,context.getResources().getString(resId),data);
    }
    public static void showToast(Context context,String format,Object ... data){
        showToast(context,String.format(format,data));
    }
    public static void showToastLong(Context context,String str){
        Toast.makeText(context,str,Toast.LENGTH_LONG).show();
    }
    public static void showToastLong(Context context,int resId,Object ... data){
        showToastLong(context,context.getResources().getString(resId),data);
    }
    public static void showToastLong(Context context,String format,Object ... data){
        showToastLong(context,String.format(format,data));
    }
}
