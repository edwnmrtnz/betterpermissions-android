package com.github.edwnmrtnz.betterpermission;

public interface SimplePermissionCallback {

    /**
     * Indicates that all requested permission are granted
     */
    void onGranted();

    /**
     * Indicates that at least one of the permissions was declined
     */
    void onDeclined();
}
