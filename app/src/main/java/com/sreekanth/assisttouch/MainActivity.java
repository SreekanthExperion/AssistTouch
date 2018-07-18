package com.sreekanth.assisttouch;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button btnMngService;
    private static final int OVERLAY_PERMISSION_CODE = 5463;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMngService = (Button) findViewById(R.id.btnmngservice);
        btnMngService.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                boolean getServiceState = isMyServiceRunning();
                if (getServiceState) {
                    stopService(new Intent(MainActivity.this, HUD.class));
                    btnMngService.setText(R.string.start);

                } else {
                    startServiceOnClick();


                }

            }
        });

    }

    private void startServiceOnClick() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (!Settings.canDrawOverlays(this)) {

                // Open the permission page
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_CODE);
                return;
            }
        }
        goToReactActivity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // If the permission has been checked
        if (requestCode == OVERLAY_PERMISSION_CODE) {
            if (Build.VERSION.SDK_INT >= 23
                    && Settings.canDrawOverlays(this)) {
                goToReactActivity();
            }
        }
    }

    private void goToReactActivity() {
        startService(new Intent(MainActivity.this, HUD.class));
        btnMngService.setText(R.string.stop);
    }

    @Override
    protected void onResume() {
        super.onResume();

        boolean getServiceState = isMyServiceRunning();
        if (getServiceState) {
            btnMngService.setText(R.string.stop);
        } else {
            btnMngService.setText(R.string.start);
        }

    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (HUD.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}

