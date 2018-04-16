package com.example.antonrooseleer.ankihelper.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.adapter.WordListAdapter
import com.example.antonrooseleer.ankihelper.event.SearchResultEvent
import com.example.antonrooseleer.ankihelper.model.Model
import com.example.antonrooseleer.ankihelper.util.SimpleDividerItemDecorator
import kotlinx.android.synthetic.main.word_lookup_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class WordLookupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.word_lookup_activity)
        loading.visibility = View.VISIBLE
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(SimpleDividerItemDecorator(this))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWordResult(result : SearchResultEvent){
        showResults(result.data.data)
    }

    private fun showResults(wordList : ArrayList<Model.Word>){
        loading.visibility = View.INVISIBLE
        recycler.adapter = WordListAdapter(wordList)
    }
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}