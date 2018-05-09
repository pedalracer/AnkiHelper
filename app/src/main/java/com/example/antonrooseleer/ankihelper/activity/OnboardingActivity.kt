package com.example.antonrooseleer.ankihelper.activity

import android.app.Fragment
import android.app.FragmentTransaction
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.fragment.OcrFragment
import com.example.antonrooseleer.ankihelper.util.PermissionUtil


class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity)
        if(PermissionUtil.checkAnkiPermission(this)){
            showOcrFragment()
        }
        setupDrawerLayout()
    }

    private fun setupDrawerLayout () {

        var mDrawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navigationView: NavigationView = findViewById(R.id.nav_view)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            mDrawerLayout.closeDrawers()
            true
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var i = 0;
        for (permission in permissions) {
            when (permission) {
                "com.ichi2.anki.permission.READ_WRITE_DATABASE" -> {
                    if(grantResults[i] !=  -1) {
                        showOcrFragment()
                    }
                }
            }
            i++
        }
    }

    private fun showFailureFragment () {
        //show failure
    }

    fun showOcrFragment() {
        loadFragment(OcrFragment())
    }

    fun loadFragment (fragment: Fragment) {
        val manager = fragmentManager
        val transaction: FragmentTransaction = manager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    private fun showTutorial() {

    }
}