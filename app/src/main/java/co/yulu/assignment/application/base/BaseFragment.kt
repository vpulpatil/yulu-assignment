package co.yulu.assignment.application.base

import android.content.Context
import android.view.View

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected lateinit var mContext: Context
    protected var rootview: View? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

}
