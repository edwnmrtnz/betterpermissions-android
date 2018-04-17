package com.github.edwnmrtnz.betterpermissions;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.edwnmrtnz.betterpermission.BetterPermission;
import com.github.edwnmrtnz.betterpermission.PermissionCallback;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    BetterPermission betterPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        betterPermission = new BetterPermission(this);

        betterPermission.setPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE)
                .requestPermissions(new PermissionCallback() {
                    @Override
                    public void onPermissionsGranted() {
                        Log.d(TAG, "onPermissionsGranted: ");
                    }

                    @Override
                    public void onIndividualPermissions(String[] grantedPermissions, String[] declinedPermissions) {
                        Log.d(TAG, "onIndividualPermissions: ");

                        Log.d(TAG, "\n*** Grated Permissions ***");
                        for(String permission : grantedPermissions){
                            Log.d(TAG, permission);
                        }

                        Log.d(TAG, "*** Denied Permissions ***");
                        for(String permission : declinedPermissions){
                            Log.d(TAG, permission);
                        }

                    }

                    @Override
                    public void onPermissionsDeclined() {
                        Log.d(TAG, "onPermissionsDeclined: ");
                    }
                }).execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        betterPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
