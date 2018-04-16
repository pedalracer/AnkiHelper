package com.example.antonrooseleer.ankihelper.model

object Model {
    data class DictionaryHits (val data : ArrayList<Word>)
    data class Word (val japanese : List<Japanese> , val senses : List<Senses>)
    data class Senses (val english_definitions : List<String>)
    data class Japanese (val word: String, val reading : String)
}
