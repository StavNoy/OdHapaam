package project.stav.odhapaam2;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

class PermissionManager {
    //checks for needed permission - and request if not granted yet
    static void check(Activity activity, String permission, int requestCode){
        if(ActivityCompat.checkSelfPermission(activity,permission) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(activity, new String[]{permission},requestCode);
    }
}
