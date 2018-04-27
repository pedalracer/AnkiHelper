package com.example.antonrooseleer.ankihelper.activity

import android.app.Activity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import com.example.antonrooseleer.ankihelper.job.WordLookUpJob


class ProcessTextActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this,"Looking up word",Toast.LENGTH_LONG).show()
        showWordLookUpActivity()
        val selectedText : CharSequence? = intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT)
        handleText(selectedText?.toString())
        finish()
    }

    private fun showWordLookUpActivity(){
        val intent : Intent = Intent(this, WordLookupActivity::class.java)
        startActivity(intent)
    }

    private fun handleText(selectedText : String?){
        WordLookUpJob().lookup(selectedText)
    }
}
