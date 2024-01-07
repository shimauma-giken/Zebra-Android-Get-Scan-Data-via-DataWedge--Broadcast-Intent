package com.example.basic_intent_study_01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private final static String TAB = "SampleAppActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        IntentFilter filter = new IntentFilter();
        filter.addCategory(Intent.CATEGORY_DEFAULT);

        // DataWedge> Intent Action の設定内容と連動させること
        filter.addAction(getResources().getString(R.string.activity_intent_filter_action));
        registerReceiver(myBroadcastReceiver, filter);

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(myBroadcastReceiver);
    }


    private void logv(String msg){
        String log_tag = "TEST-V";
        Log.v(log_tag, msg);
    }

    private void toast(String msg){
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(this /* MyActivity */, msg, duration);
        toast.show();
    }


    // BroadCast Receiver の定義
    // - 別なクラスとしても定義が可能
    // - Extra KeysはString.xml に記載
    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            //  デバッグ用
            // 通知されたIntent種を取得
            logv("Action: "+ action);

            Bundle b = intent.getExtras();

            //  デバッグ用
            // getExtra で取得可能なKey:Dataのリストを取得
            for (String key : b.keySet())
            {
                logv(key + " : " + intent.getStringExtra(key));
            }

            if (action.equals(getResources().getString(R.string.activity_intent_filter_action))) {
                //  Received a barcode scan
                try {
                    displayScanResult(intent, "via Broadcast");
                } catch (Exception e) {
                    //  Catch if the UI does not exist when we receive the broadcast
                    logv(e.toString());
                    toast(e.toString());
                }
            }
        }
    };

    // UI 表示用
    private void displayScanResult(Intent initiatingIntent, String howDataReceived)
    {
        String decodedSource = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_source));
        String decodedData = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_data));
        String decodedLabelType = initiatingIntent.getStringExtra(getResources().getString(R.string.datawedge_intent_key_label_type));

        final TextView lblScanSource = (TextView) findViewById(R.id.lblScanSource);
        final TextView lblScanData = (TextView) findViewById(R.id.lblScanData);
        final TextView lblScanLabelType = (TextView) findViewById(R.id.lblScanDecoder);

        lblScanSource.setText(decodedSource + " " + howDataReceived);
        lblScanData.setText(decodedData);
        lblScanLabelType.setText(decodedLabelType);
    }
}
