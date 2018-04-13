package com.github.edwnmrtnz.betterpermission;

public interface PermissionCallback {
    /**
     * Indicates that all permissions has been granted.
     * */
    void onPermissionsGranted();

    /**
     * Indicated all granted and declined permissions
     * @param grantedPermissions - holds granted permissions.
     * @param declinedPermissions - holds declined permissions.
     */
    void onIndividualPermissions(String [] grantedPermissions, String [] declinedPermissions);

    /**
     * Indicates that all permissions was declined.
     * */
    void onPermissionsDeclined();
}
