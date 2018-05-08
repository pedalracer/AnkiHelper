package com.example.antonrooseleer.ankihelper.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.antonrooseleer.ankihelper.R
import kotlinx.android.synthetic.main.onboarding_fragment.*

/**
 * Created by a_176 on 29/04/2018.
 */
class OnboardingFragment : Fragment() {

    companion object {
        fun newInstance () : Fragment? {
            return OnboardingFragment()
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.onboarding_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}