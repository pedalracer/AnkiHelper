package com.example.antonrooseleer.ankihelper.fragment

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import com.example.antonrooseleer.ankihelper.R
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.PorterDuff
import android.hardware.Camera
import com.example.antonrooseleer.ankihelper.model.CameraPreview
import kotlinx.android.synthetic.main.ocr_fragment.*
import android.hardware.Camera.PictureCallback
import android.view.*
import com.example.antonrooseleer.ankihelper.event.OcrTextResult
import com.example.antonrooseleer.ankihelper.util.CloudVision
import com.example.antonrooseleer.ankihelper.util.PermissionUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import android.view.ContextMenu.ContextMenuInfo
import android.widget.AdapterView




/**
 * Created by a_176 on 28/04/2018.
 */
class OcrFragment : Fragment() {

    var isLoading = false
    val REQUEST_CODE = 200


    companion object {
        val TAG = "OcrFragment"
        fun newInstance(): Fragment {
            return OcrFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.ocr_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mContext = context
        if (mContext != null) {
            if (PermissionUtil.checkOcrPermissions(mContext, this)) {
                setupCameraPreview()

            }
        }
        loading_progress.indeterminateDrawable.setColorFilter(context.getColor(R.color.white), PorterDuff.Mode.MULTIPLY)
        registerForContextMenu(ocrPreview)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var i = 0;
        for (permission in permissions) {
            when (permission) {
                "android.permission.CAMERA" -> {
                    if(grantResults[i] !=  -1) {
                        setupCameraPreview()
                    }
                }
            }
            i++
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo) {

        menu.setHeaderTitle("Context Menu")
        menu.add(0, v.id, 0, "Action 1")
        super.onCreateContextMenu(menu, v, menuInfo)
        // Create your context menu here
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        // Call your function to preform for buttons pressed in a context menu
        // can use item.getTitle() or similar to find out button pressed
        // item.getItemID() will return the v.getID() that we passed before
        return true

    }
    private fun setupCameraPreview() {

        val mCamera = getCameraInstance()
        mCamera?.setDisplayOrientation(90)
        var params = mCamera?.getParameters()
        params?.jpegQuality = 100
        params?.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)
        params?.setRotation(90)
        mCamera?.parameters = params
        // Create our Preview view and set it as the content of our activity.
        var mPreview = CameraPreview(context, mCamera)
        camera_preview.addView(mPreview)

        camera_preview.setOnClickListener {
            if (!isLoading) {
                ocrPreview.text = ""
                isLoading = true
                loading_progress.visibility = View.VISIBLE
                mCamera?.takePicture(null, null, mPicture)
            }
        }

        retry.setOnClickListener {
            if(!isLoading){
                mCamera?.startPreview()
            }
        }
    }

    /** Check if this device has a camera  */
    private fun checkCameraHardware(context: Context?): Boolean {
        if (context?.packageManager != null) {
            return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
        }
        return false
    }

    /** A safe way to get an instance of the Camera object.  */
    fun getCameraInstance(): Camera? {

        var c: Camera? = null
        try {
            c = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK) // attempt to get a Camera instance
        } catch (e: Exception) {
            e.printStackTrace()
            // Camera is not available (in use or does not exist)
        }

        return c // returns null if camera is unavailable
    }

    private val mPicture = PictureCallback { data, camera ->
        //val bitmap = BitmapUtil.toBitmap(data);
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        val vision = CloudVision(context)
        vision.uploadImage(bitmap)
        // mCamera?.startPreview()
    }

    fun setPreviewText(text: String) {
        ocrPreview.text = text
    }

    override fun onStart() {
        EventBus.getDefault().register(this)
        super.onStart()
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWordResult(result: OcrTextResult) {
        loading_progress.visibility = View.INVISIBLE
        setPreviewText(result.text)
        isLoading = false

    }
}
