package com.example.antonrooseleer.ankihelper.fragment

import android.app.Fragment
import android.os.Bundle
import com.example.antonrooseleer.ankihelper.R
import android.graphics.PorterDuff
import kotlinx.android.synthetic.main.ocr_fragment.*
import android.view.*
import com.example.antonrooseleer.ankihelper.event.OcrTextResult
import com.example.antonrooseleer.ankihelper.util.CloudVision
import com.example.antonrooseleer.ankihelper.util.PermissionUtil
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import io.fotoapparat.Fotoapparat
import io.fotoapparat.log.logcat
import io.fotoapparat.log.loggers
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.selector.back


/**
 * Created by a_176 on 28/04/2018.
 */
class OcrFragment : Fragment() {

    var isLoading = false
    val REQUEST_CODE = 200
    lateinit var fotoapparat : Fotoapparat

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

    private fun setupCameraPreview() {
        val vision = CloudVision(context)

        fotoapparat =  Fotoapparat(
                context = context,
                view = camera_view,                   // view which will draw the camera preview
                scaleType = ScaleType.CenterCrop,    // (optional) we want the preview to fill the view
                lensPosition = back() ,              // (optional) we want back camera
                logger = loggers(                    // (optional) we want to log camera events in 2 places at once
                        logcat()         // ... and to file
                ),
                cameraErrorCallback = { error -> error.printStackTrace()}   // (optional) log fatal errors
        )

        camera_view.setOnClickListener {
            if (!isLoading) {
                ocrPreview.text = ""
                isLoading = true
                loading_progress.visibility = View.VISIBLE
                val photoResult =  fotoapparat.takePicture()
                photoResult
                        .toBitmap()
                        .whenAvailable { bitmapPhoto -> run {
                            if(bitmapPhoto != null){
                                vision.uploadImage(bitmapPhoto.bitmap)
                            }
                        }}
            }
        }
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

    override fun onPause() {
        fotoapparat.stop()
        super.onPause()
    }

    override fun onResume() {
        fotoapparat.start()
        super.onResume()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onWordResult(result: OcrTextResult) {
        loading_progress.visibility = View.INVISIBLE
        setPreviewText(result.text)
        isLoading = false

    }
}
