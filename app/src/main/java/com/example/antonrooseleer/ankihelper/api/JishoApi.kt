package com.example.antonrooseleer.ankihelper.api

import com.example.antonrooseleer.ankihelper.model.Model
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface JishoApi {

    @GET("search/words")
    fun wordLookup(@Query("keyword") keyword : String?) : Observable<Model.DictionaryHits>
}