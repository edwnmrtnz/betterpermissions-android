package com.github.edwnmrtnz.betterpermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
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

    public BetterPermission setPermissions(String...permissions){
        this.permissions = permissions;
        return this;
    }

    public BetterPermission requestPermissions(PermissionCallback permissionCallback){
        this.callback = permissionCallback;
        return this;
    }

    public void execute(){
        requestPermissions();
    }

    private void requestPermissions() {
        if(permissions == null || permissions.length == 0) throw new NullPointerException("Please add a valid permissions via setPermissions(...)");

        if(context == null) throw new NullPointerException("Context cannot be null. Please pass a valid context via constructor(Context)");

        checkPermissionsExistenceInManifest();

        ActivityCompat.requestPermissions((Activity) context, permissions, REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_CODE:{

                handleResult(permissions, grantResults);

                this.permissions = null;

                break;
            }
        }
    }

    private void handleResult(String[] _permissions, int[] grantResults) {

        List<String> grantedPermissions = new ArrayList<>();

        List<String> deniedPermissions = new ArrayList<>();

        for (String permission : _permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                deniedPermissions.add(permission);
            }else{
                grantedPermissions.add(permission);
            }
        }

        if(grantedPermissions.size() == _permissions.length){
            callback.onPermissionsGranted();
        } else if(grantedPermissions.size() == 0){
            callback.onPermissionsDeclined();
        } else{
            String [] grantedPermissionsArr = grantedPermissions.toArray(new String[grantedPermissions.size()]);

            String [] deniedPermissionsArr = deniedPermissions.toArray(new String[deniedPermissions.size()]);

            callback.onIndividualPermissions(grantedPermissionsArr, deniedPermissionsArr);
        }
    }

    /**
     * Check if permission is actually declared inside the
     * manifest.
     * */
    private void checkPermissionsExistenceInManifest() {
        for (String permission : permissions) {
            if (!isDeclaredInManifest(permission)) {
                throw new RuntimeException("Please declare " + permission + " inside your manifest");
            }
        }
    }

    /**
     * Check existence of individual permission
     * @param permission - Permission to be checked.
     * */
    private boolean isDeclaredInManifest(String permission) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            if (info.requestedPermissions != null) {
                for (String p : info.requestedPermissions) {
                    if (p.equals(permission)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
