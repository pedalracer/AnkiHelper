package com.example.antonrooseleer.ankihelper.fragment

import android.app.ActionBar
import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.antonrooseleer.ankihelper.R
import android.content.pm.PackageManager
import android.hardware.Camera
import com.example.antonrooseleer.ankihelper.model.CameraPreview
import kotlinx.android.synthetic.main.ocr_fragment.*


/**
 * Created by a_176 on 28/04/2018.
 */
class OcrFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.ocr_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Create an instance of Camera
        var mCamera = getCameraInstance()

        // Create our Preview view and set it as the content of our activity.
        var mPreview = CameraPreview(context, mCamera)
        camera_preview.addView(mPreview)
    }

    /** Check if this device has a camera  */
    private fun checkCameraHardware(context: Context): Boolean {
        return (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
    }

    /** A safe way to get an instance of the Camera object.  */
    fun getCameraInstance(): Camera? {

        var c: Camera? = null
        try {
            c = Camera.open() // attempt to get a Camera instance
        } catch (e: Exception) {
            // Camera is not available (in use or does not exist)
        }

        return c // returns null if camera is unavailable
    }
}
