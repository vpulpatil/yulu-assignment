package co.yulu.assignment.util

import android.widget.Toast
import co.yulu.assignment.application.base.BaseActivity

class UIUtil {

    companion object {
        fun showShortToast(activity: BaseActivity, message: String) {
            activity.runOnUiThread {
                Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
            }
        }

        fun showLongToast(activity: BaseActivity, message: String) {
            activity.runOnUiThread {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
            }
        }
    }
}