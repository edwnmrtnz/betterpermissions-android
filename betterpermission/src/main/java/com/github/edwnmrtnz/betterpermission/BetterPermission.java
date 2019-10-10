package com.github.edwnmrtnz.betterpermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BetterPermission {

    private static final String TAG = BetterPermission.class.getSimpleName();

    private static final int REQUEST_CODE = 404;

    private Context context;

    private List<String> permissions;

    private PermissionCallback permissionCallback;

    private SimplePermissionCallback simplePermissionCallback;

    public BetterPermission(Context context) {
        this.context = context;
    }

    public BetterPermission setPermissions(String...permissions){
        this.permissions = Arrays.asList(permissions);
        return this;
    }

    public BetterPermission requestPermissions(PermissionCallback permissionCallback){
        this.permissionCallback = permissionCallback;
        return this;
    }

    public BetterPermission requestPermissions(SimplePermissionCallback simplePermissionCallback) {
        this.simplePermissionCallback = simplePermissionCallback;
        return this;
    }

    public void execute(){
        requestPermissions();
    }

    public void executeFromFragment(Fragment fragment) {
        requestPermissionsFromFragment(fragment);
    }

    private void requestPermissionsFromFragment(Fragment fragment) {
        if(permissions == null || permissions.size() == 0) throw new NullPointerException("Please add a valid permissions via setPermissions(...)");

        if(context == null) throw new NullPointerException("Context cannot be null. Please pass a valid context via constructor(Context)");

        checkPermissionsExistenceInManifest();

        List<String> filteredPermissions = filterNotGrantedPermissions();

        if(filteredPermissions.size() > 0){
            fragment.requestPermissions(filteredPermissions.toArray(new String[filteredPermissions.size()]), REQUEST_CODE);
        }else{
            if(permissionCallback != null)  {
                permissionCallback.allPermissionsAreAlreadyGranted();
            }
            if(simplePermissionCallback != null) {
                simplePermissionCallback.onGranted();
            }
            clearPermissions();
        }
    }

    private void requestPermissions() {
        if(permissions == null || permissions.size() == 0) throw new NullPointerException("Please add a valid permissions via setPermissions(...)");

        if(context == null) throw new NullPointerException("Context cannot be null. Please pass a valid context via constructor(Context)");

        checkPermissionsExistenceInManifest();

        List<String> filteredPermissions = filterNotGrantedPermissions();

        if(filteredPermissions.size() > 0){
            ActivityCompat.requestPermissions((Activity) context, filteredPermissions.toArray(new String[filteredPermissions.size()]), REQUEST_CODE);
        }else{
            if(permissionCallback != null)  {
                permissionCallback.allPermissionsAreAlreadyGranted();
            }
            if(simplePermissionCallback != null) {
                simplePermissionCallback.onGranted();
            }
            clearPermissions();
        }
    }

    private void clearPermissions() {
        permissions.clear();
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

        if(grantedPermissions.size() == _permissions.length) {

            if(permissionCallback != null)
                permissionCallback.onPermissionsGranted();

            if(simplePermissionCallback != null)
                simplePermissionCallback.onGranted();

        } else if(grantedPermissions.size() == 0) {

            if(permissionCallback != null)
                permissionCallback.onPermissionsDeclined();

            if(simplePermissionCallback != null)
                simplePermissionCallback.onGranted();

        } else {
            String [] grantedPermissionsArr = grantedPermissions.toArray(new String[grantedPermissions.size()]);

            String [] deniedPermissionsArr = deniedPermissions.toArray(new String[deniedPermissions.size()]);

            if(permissionCallback != null) permissionCallback.onIndividualPermissions(grantedPermissionsArr, deniedPermissionsArr);

            if(simplePermissionCallback != null) simplePermissionCallback.onDeclined();
        }

        clearPermissions();
    }

    /**
     * Filters not granted permissions
     * */
    private List<String> filterNotGrantedPermissions() {
        List<String> filteredPermissions = new ArrayList<>();
        for(String permission : permissions){
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                filteredPermissions.add(permission);
            }
        }
        return filteredPermissions;
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
