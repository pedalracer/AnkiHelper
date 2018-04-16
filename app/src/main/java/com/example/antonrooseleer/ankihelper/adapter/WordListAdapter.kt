package com.example.antonrooseleer.ankihelper.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.model.Model
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
            val list = stringifyJpReading(word.japanese)
            itemView.jp_reading.text = list.get(1)
            itemView.jp_word.text = list.get(0)
            itemView.en_def.text = stringifyEnReading(word.senses)
            itemView.setOnClickListener {

            }
        }

        private fun stringifyJpReading(japaneseWords: List<Model.Japanese>): List<String> {
            var readings = ""
            var words = ""
            if (japaneseWords.isEmpty()) {
                return listOf("", "")
            }
            for (jpword in japaneseWords) {
                if (jpword.reading != null) {
                    if (readings.equals("")) {
                        readings += jpword.reading
                    } else if (!jpword.reading.isEmpty()) {
                        readings += String.format(", %s", jpword.reading)
                    }
                }
                if (jpword.word != null) {
                    if (words.equals("")) {
                        words += jpword.word
                    } else if (!jpword.word.isEmpty()) {
                        words += String.format(", %s", jpword.word)
                    }
                }
            }
            return listOf<String>(words, readings)
        }

        private fun stringifyEnReading(englishWord: List<Model.Senses>): String {

            var result = "";
            for (translation in englishWord) {
                if (result.equals("")) {
                    result += translation.english_definitions
                } else {
                    result += String.format(", %s", translation.english_definitions)
                }
            }
            return result
        }
    }

}