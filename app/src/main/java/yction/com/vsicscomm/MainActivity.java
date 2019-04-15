package yction.com.vsicscomm;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "MainActivity";

    private NetService netService;                                         // 网络服务service

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 启动服务
        Intent serviceIntent = new Intent(this, NetService.class);
        bindService(serviceIntent, this, BIND_AUTO_CREATE);

        final Button btnLogin = (Button) findViewById(R.id.ButtonReport);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(netService!=null){
                    Log.i(TAG, "report ADAS");
                    netService.reportADAS();
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        NetService.NetBinder binder=(NetService.NetBinder)service;
        netService=binder.getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }
}
