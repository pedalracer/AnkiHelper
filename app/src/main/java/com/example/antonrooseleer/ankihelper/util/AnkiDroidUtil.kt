package com.example.antonrooseleer.ankihelper.util

import android.app.Activity
import android.content.Context
import com.example.antonrooseleer.ankihelper.model.Model
import android.support.v4.app.ShareCompat
import com.ichi2.anki.api.AddContentApi
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.ichi2.anki.api.AddContentApi.READ_WRITE_PERMISSION








class AnkiDroidUtil {

    companion object {
        val deckName = "AnkiHelper";
        private val MODEL_REF_DB = "com.ichi2.anki.api.models"
        val modelName = "com.example.antonrooseleer.ankihelper"

        fun addWord(context : Context, word : Model.Word){
            if (AddContentApi.getAnkiDroidPackageName(context) != null) {
                // API available: Add deck and model if required, then add your note
                val api = AddContentApi(context)
                val deckId = if(getDeckId(api, deckName) == -1L)  api.addNewDeck("My app name") else getDeckId(api, deckName)
                val modelId = if(findModel(api, modelName) != -1L) findModel(api, modelName) else api.addNewBasicModel(modelName)
                api.addNote(modelId, deckId, arrayOf(word.japanese[0].word, word.senses[0].english_definitions.toString()), null)
            } else {
                // Fallback on ACTION_SEND Share Intent if the API is unavailable
                val shareIntent = ShareCompat.IntentBuilder.from(context as Activity)
                        .setType("text/plain")
                        .setSubject(word.japanese[0].word)
                        .setText(word.senses[0].english_definitions.toString())
                        .intent
                if (shareIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(shareIntent)
                }
            }
        }

        fun findModel(api: AddContentApi, modelName: String) : Long{
            for(model in api.modelList){
                if(model.value.equals(modelName)){
                    return model.key
                }
            }
            return -1L;
        }
        /**
         * Whether or not we should request full access to the AnkiDroid API
         */
        fun shouldRequestPermission(context: Context): Boolean {
            return ContextCompat.checkSelfPermission(context, READ_WRITE_PERMISSION) != PackageManager.PERMISSION_GRANTED
        }

        /**
         * Request permission from the user to access the AnkiDroid API (for SDK 23+)
         * @param callbackActivity An Activity which implements onRequestPermissionsResult()
         * @param callbackCode The callback code to be used in onRequestPermissionsResult()
         */
        fun requestPermission(callbackActivity: Activity, callbackCode: Int) {
            ActivityCompat.requestPermissions(callbackActivity, arrayOf(READ_WRITE_PERMISSION), callbackCode)
        }

        /**
         * Get the ID of the deck which matches the name
         * @param deckName Exact name of deck (note: deck names are unique in Anki)
         * @return the ID of the deck that has given name, or null if no deck was found
         */
        private fun getDeckId(mApi : AddContentApi , deckName: String): Long {
            val deckList = mApi.getDeckList()
            for (entry in deckList.entries) {
                if (entry.value.equals(deckName, ignoreCase = true)) {
                    return entry.key
                }
            }
            return -1L
        }

        fun findModelIdByName(mApi : AddContentApi, mContext: Context, modelName: String, numFields: Int): Long? {
            val modelsDb = mContext.getSharedPreferences(MODEL_REF_DB, Context.MODE_PRIVATE)
            val prefsModelId = modelsDb.getLong(modelName, -1L)
            // if we have a reference saved to modelName and it exists and has at least numFields then return it
            if (prefsModelId != -1L && mApi.getModelName(prefsModelId) != null
                    && mApi.getFieldList(prefsModelId).size >= numFields) { // could potentially have been renamed
                return prefsModelId
            }
            val modelList = mApi.getModelList(numFields)
            for (entry in modelList.entries) {
                if (entry.value == modelName) {
                    return entry.key // first model wins
                }
            }
            // model no longer exists (by name nor old id) or the number of fields was reduced
            return null
        }
    }
}