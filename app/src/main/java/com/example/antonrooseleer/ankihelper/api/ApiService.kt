package com.example.antonrooseleer.ankihelper.api

import io.reactivex.disposables.Disposable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiService {

     val jishoService by lazy {
        ApiService.createJishoService()
    }

    var disposable : Disposable? = null

    companion object {

        val jishoBaseUrl = "http://jisho.org/api/v1/"

        fun createJishoService() : JishoApi {
            val jishoService = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(jishoBaseUrl)
                    .build()
            return jishoService.create(JishoApi::class.java)
        }
    }


}