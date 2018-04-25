package com.example.antonrooseleer.ankihelper.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.model.Model
import com.example.antonrooseleer.ankihelper.util.AnkiDroidUtil
import com.example.antonrooseleer.ankihelper.util.StringUtils
import kotlinx.android.synthetic.main.word_option_view.view.*

class WordListAdapter(val wordList: ArrayList<Model.Word>) : RecyclerView.Adapter<WordListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordListAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.word_option_view, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: WordListAdapter.ViewHolder, position: Int) {
        holder.bindItems(wordList[position])
    }

    override fun getItemCount(): Int {
        return wordList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(word: Model.Word) {
            val list = StringUtils.stringifyJpReading(word.japanese)
            itemView.jp_reading.text = list.get(1)
            itemView.jp_word.text = list.get(0)
            itemView.en_def.text = StringUtils.stringifyEnReading(word.senses)
            itemView.setOnClickListener {
                AnkiDroidUtil.addWord(itemView.context, itemView.jp_word.text.toString(), itemView.jp_reading.text.toString(),itemView.en_def.text.toString())
            }
        }
    }
}