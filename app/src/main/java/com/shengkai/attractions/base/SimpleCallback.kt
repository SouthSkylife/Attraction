package com.shengkai.attractions.base

import retrofit2.Call
import retrofit2.Response

class SimpleCallback<T> {
    /**
     * After Connecting
     */
    fun initial() {

    }

    /**
     * After onResponse
     *
     * @param call
     * @param response
     */
    fun onResponse(call: Call<T>, response: Response<T>) {

    }

    /**
     * After onCallback
     *
     * @param t
     */
    fun onCallback(t: T) {

    }

    /**
     * After onFail
     *
     * @param t
     */
    fun onFail(t: T) {

    }

    /**
     * After onFail and Throw Exception
     *
     * @param t
     */
    fun onFail(t: Throwable) {

    }

    /**
     * After onComplete
     */
    fun onComplete() {

    }
}