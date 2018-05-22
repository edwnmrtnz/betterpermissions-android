# betterpermissions-android
Handle your runtime permissions with ease!

[![](https://jitpack.io/v/edwnmrtnz/betterpermissions-android.svg)](https://jitpack.io/#edwnmrtnz/betterpermissions-android)

### Prerequisites
Add this in your root build.gradle file (not your module build.gradle file):

```gradle
allprojects {
    repositories {
	    ...
	    maven { url 'https://jitpack.io' }
	}
}
```
### Dependency
Add this to your module's build.gradle file (make sure the version matches the JitPack badge above):
```gradle
dependencies {
    implementation 'com.github.edwnmrtnz:betterpermissions-android:v.1.0.0'
}
```

### Usage

Declare an instance of BetterPermission then pass current context to its contructor

```java   
BetterPermission betterPermission = new BetterPermission(this);
```

To start requesting for permissions, Just add it via setPermissions(varargs) method. This method has varargs so you can pass manifest as many times as you want.

```java
betterPermission.setPermissions(Manifest.permission.CALL_PHONE, Manifest.permission.SEND_SMS, Manifest.permission.READ_EXTERNAL_STORAGE);
```
Call execute to start your request.

```java
betterPermission.execute();
```

To get the result, override your activity's onRequestPermissionsResult() and pass its arguments to betterpemissions.

```java
@Override
   public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    betterPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```

Implement the listener to get your filtered results.

```java
betterPermission.requestPermissions(new PermissionCallback() {
     @Override
     public void allPermissionsAreAlreadyGranted() {
         //Indicates that all permissions are already granted and so there's no need to request it.
     }

     @Override
     public void onPermissionsGranted() {
         //Indicates that all requested permissions were granted
     }

     @Override
     public void onIndividualPermissions(String[] grantedPermissions, String[] declinedPermissions) {
         //Filters granted and declined permissions
     }

     @Override
     public void onPermissionsDeclined() {
         //Indicates that all permissions were declined
     }
    });
```
### Tip

You can actually chain your calls like
```java
betterPermission
    .setPermissions(...)
    .requestPermerssions(PermissionCallback)
    .execute().
```


### Note
Your app will crash if you attempt to request a permission without declaring it inside your manfiest file. You can always check the log
to see what permissions needs to be added in the manifest.


