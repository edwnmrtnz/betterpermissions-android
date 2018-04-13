package com.github.edwnmrtnz.betterpermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Pair;

import java.util.HashMap;
import java.util.List;

public class BetterPermission {
    private static final String TAG = BetterPermission.class.getSimpleName();

    private static final int REQUEST_CODE = 404;

    private Context context;

    private String[] permissions;

    private PermissionCallback callback;

    public BetterPermission(Context context) {
        this.context = context;
    }

    public BetterPermission(Context context, String...permissions){
        this.context = context;
        this.permissions = permissions;
    }

    public void requestPermissions(PermissionCallback permissionCallback){
        this.callback = permissionCallback;

        requestPermissions();
    }

    public void requestPermissions(PermissionCallback callback, String...permissions){
        this.permissions = permissions;
        this.callback = callback;
    }

    private void requestPermissions() {
        if(permissions == null) throw new NullPointerException("Please add a valid permissions via Constructor(context, permissions)");
        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_CODE:{



                break;
            }
        }
    }

}
