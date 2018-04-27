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
        icClose.setOnClickListener { finish() }
        loading.visibility = View.VISIBLE
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.addItemDecoration(SimpleDividerItemDecorator(this))
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWordResult(result: SearchResultEvent) {
        //showResults(result.data.data)
    }

    private fun showResults(wordList: ArrayList<Model.Word>) {
        var wordAdapter: WordListAdapter
        loading.visibility = View.INVISIBLE
        selectionTitle.visibility = View.VISIBLE
        if (wordList.size <= 3) {
            wordAdapter = WordListAdapter(wordList)
        } else {
            var subList = wordList.subList(0, 3)
            wordAdapter = WordListAdapter(subList)
            icMore.visibility = View.VISIBLE
            icMore.setOnClickListener {
                wordAdapter.setWordList(wordList)
            }
        }
        recycler.adapter = wordAdapter
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