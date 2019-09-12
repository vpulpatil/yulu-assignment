package co.yulu.assignment.network.handler

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import co.yulu.assignment.application.AppExecutors
import co.barfi.network.handler.*


abstract class NetworkBoundResource<ResultType, RequestType> @MainThread
constructor(private val appExecutors: AppExecutors) {
    private val result = MediatorLiveData<Resource<ResultType>>()

    val asLiveData: LiveData<Resource<ResultType>>
        get() = result

    init {
        setValue(Resource.loading(null))
        if (shouldfetchDataFromDbBeforeNetwork()) {
            val dbSource: LiveData<ResultType>? = loadFromDb()
            if (dbSource != null) {
                result.addSource(dbSource) { data ->
                    result.removeSource(dbSource)
                    if (shouldFetchFromNetwork(data)) {
                        fetchFromNetwork(dbSource)
                    } else {
                        result.addSource(dbSource) { newData ->
                            setValue(
                                Resource.success(
                                    newData
                                )
                            )
                        }
                    }
                }
            }else{
                fetchFromNetworkWithDummyDbSource()
            }
        } else {
            fetchFromNetworkWithDummyDbSource()
        }
    }

    fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall()
        result.addSource(dbSource) { newData -> setValue(Resource.loading(newData)) }

        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)

            when (response) {
                is ApiSuccessResponse -> {
                    if (shouldStoreDataInDbAfterNetwork()) {
                        saveResultAndReInit(response.body)
                    } else {
                        val resultType = parseNetworkResponse(response.body)
                        if (resultType != null) {
                            result.addSource(resultType) { newData ->
                                setValue(
                                    Resource.success(
                                        newData
                                    )
                                )
                            }
                        } else {
                            result.addSource(dbSource) { newData -> result.setValue(
                                Resource.error(
                                    "Something went wrong",
                                    newData
                                )
                            ) }
                        }

                    }
                }
                is ApiEmptyResponse -> {
                    onFetchFailed()
                    result.removeSource(dbSource)
                    result.addSource(dbSource) { newData -> result.setValue(
                        Resource.error(
                            "Something went wrong",
                            newData
                        )
                    ) }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.removeSource(dbSource)
                    if(dbSource.value != null) {
                        result.addSource(dbSource) { newData ->
                            result.setValue(Resource.error(response.errorMessage, newData))
                        }
                    } else {
                        setValue(Resource.error(response.errorMessage, null))
                    }
                }
            }
        }
    }

    @MainThread
    private fun saveResultAndReInit(response: RequestType) {
        appExecutors.diskIO().execute {
            saveCallResult(response)
            appExecutors.mainThread().execute {
                // we specially request a new live data,
                // otherwise we will get immediately last cached value,
                // which may not be updated with latest results received from network.
                result.addSource(parseNetworkResponse(response)) { newData ->
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    private fun fetchFromNetworkWithDummyDbSource() {
        val tempSource = MutableLiveData<ResultType>()
        fetchFromNetwork(tempSource)
    }

    @MainThread
    protected fun shouldFetchFromNetwork(data: ResultType?): Boolean {
        return true
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    protected abstract fun loadFromDb(): LiveData<ResultType>?

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>

    @MainThread
    protected abstract fun parseNetworkResponse(body: RequestType): MutableLiveData<ResultType>

    @MainThread
    protected fun onFetchFailed() {

    }


    /**
     * ====================================================================================================================================
     * Override these 2 methods when we dont want to save the reponse in db or we dont want to handle Db transactions related to dis query.
     * ====================================================================================================================================
     */

    /**
     * return false when we dont want to save database transactions for this response.
     */
    @MainThread
    protected open fun shouldfetchDataFromDbBeforeNetwork(): Boolean {
        return true
    }

    /**
     * return false when we dont want to save database transactions for this response.
     */
    @MainThread
    protected open fun shouldStoreDataInDbAfterNetwork(): Boolean {
        return true
    }
}
