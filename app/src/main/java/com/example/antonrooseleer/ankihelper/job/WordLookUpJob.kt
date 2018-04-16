package com.example.antonrooseleer.ankihelper.job

import com.example.antonrooseleer.ankihelper.event.SearchResultEvent
import com.example.antonrooseleer.ankihelper.api.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.greenrobot.eventbus.EventBus

class WordLookUpJob{

    fun lookup(keyword : String?){
        val jishoInstance = ApiService.createJishoService()
        jishoInstance.wordLookup(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {result -> EventBus.getDefault().post(SearchResultEvent(result))},
                    {error -> System.out.println("Anton " + error.printStackTrace())}
                )
    }

}