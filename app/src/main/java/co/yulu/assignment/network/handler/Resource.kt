package co.barfi.network.handler

import co.yulu.assignment.network.handler.Status
import co.yulu.assignment.network.handler.Status.ERROR
import co.yulu.assignment.network.handler.Status.LOADING
import co.yulu.assignment.network.handler.Status.SUCCESS


class Resource<T> private constructor(val status: Status, val data: T?, val message: String?) {

    val isSuccess: Boolean
        get() = status == SUCCESS && data != null

    val isLoading: Boolean
        get() = status == LOADING

    companion object {

        fun <T> success(data: T): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }


}

