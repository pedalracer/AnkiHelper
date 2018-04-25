package com.example.antonrooseleer.ankihelper.util

import com.example.antonrooseleer.ankihelper.model.Model

class StringUtils {

    companion object {

        private fun HashSet<String>.formatString() : String{
                return this.toString().replace("[", "").replace("]", "")
        }

        private fun MutableList<String>.formatString () : String{
            return this.toString().replace("[", "").replace("]", "")
        }

        fun stringifyJpReading(japaneseWords: List<Model.Japanese>): List<String> {
            val readings = HashSet<String>()
            val words = HashSet<String>()

            for (jpword in japaneseWords) {
                if(jpword.reading != null) {readings.add(jpword.reading)}
                if(jpword.word != null) {words.add(jpword.word)}
            }

            return listOf<String>(words.formatString(), readings.formatString())
        }

        fun stringifyEnReading(englishWord: List<Model.Senses>): String {
            val result = mutableListOf<String>()
            for (translation in englishWord) {
                if(translation.english_definitions != null){
                    result.addAll(translation.english_definitions)
                }
            }
            return result.formatString()
        }

        fun generateBackOfCard(reading: String,  translation : String) : String {
            return String.format("%s<br />%s",reading,translation)
        }
    }
}