package co.yulu.assignment.util.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.yulu.assignment.application.base.BaseActivity
import co.yulu.assignment.application.base.BaseFragment

const val LOCATION_PERMISSION_REQUEST_CODE = 113

object PermissionUtil {

    fun isPermissionGranted(context: Context, permission: String): Boolean {
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
        var allPermissionsGranted = true
        for (singlePermission in permissions) {
            allPermissionsGranted = allPermissionsGranted &&
                    isPermissionGranted(context, singlePermission)
        }
        return allPermissionsGranted
    }

    fun requestPermissions(activity: BaseActivity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode)
    }

    fun requestPermissions(fragment: BaseFragment, permissions: Array<String>, requestCode: Int) {
        fragment.requestPermissions(permissions, requestCode)
    }
}