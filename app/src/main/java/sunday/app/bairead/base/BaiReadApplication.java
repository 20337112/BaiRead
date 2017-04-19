package sunday.app.bairead.base;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by sunday on 2016/12/13.
 */

public class BaiReadApplication extends Application {

    public interface INetworkListener{
        void networkChange(boolean connect,int type);
        void unConnect();
    }

    //private INetworkListener networkListener;

    public static final String TECENT_BUGLY_APP_ID = "babf2b978b";
    public static final String ALIBAICHUAN_APP_ID = "23691798";
    public static final String UMENG_APP_ID = "58c554c2aed17906550017a0";
    @Override
    public void onCreate() {
        super.onCreate();
        /**
            第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            输出详细的Bugly SDK的Log；
            每一条Crash都会被立即上报；
            自定义日志将会在Logcat中输出。
            建议在测试阶段建议设置成true，发布时设置为false。
         **/
//        CrashReport.initCrashReport(getApplicationContext(), TECENT_BUGLY_APP_ID, true);
//
//
//        FeedbackAPI.init(this, ALIBAICHUAN_APP_ID);

        registerReceiver();

        //createDir();

    }







    private void registerReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
    }

    private ArrayList<INetworkListener> mListenerList = new ArrayList<>();

    public void addListener(INetworkListener listener){
        mListenerList.add(listener);
    }

    public void removeListener(INetworkListener listener){
        mListenerList.remove(listener);
    }

    public void clearListener(){
        mListenerList.clear();
    }


    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterReceiver(connectionReceiver);
    }

    BroadcastReceiver connectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
            Network[] networks = connectMgr.getAllNetworks();
            if(networks.length > 0) {
                for (Network network : networks) {
                    //NetworkInfo networkInfo = connectMgr.getNetworkInfo(network);
                    NetworkInfo networkInfo = connectMgr.getNetworkInfo(network);
                    boolean isConnected = false;
                    int type = -1;//ConnectivityManager.TYPE_NONE;
                    if (networkInfo == null) {
                        isConnected = false;
                        Toast.makeText(context, "network is unConnect", Toast.LENGTH_SHORT).show();
                    } else if (networkInfo.isConnected()) {
                        isConnected = true;
                        type = networkInfo.getType();
                        if (type == ConnectivityManager.TYPE_MOBILE) {
                            //Toast.makeText(context,"Mobile Network is Connect",Toast.LENGTH_SHORT).show();
                        } else if (type == ConnectivityManager.TYPE_WIFI) {
                            //Toast.makeText(context,"WIFI is Connect",Toast.LENGTH_SHORT).show();
                        }
                    }

                    for (INetworkListener networkListener : mListenerList) {
                        networkListener.networkChange(isConnected, type);
                    }


                }
            }else {
                for (INetworkListener networkListener : mListenerList) {
                    networkListener.unConnect();
                }
            }


        }
    };
}
