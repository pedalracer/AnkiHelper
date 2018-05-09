package com.example.antonrooseleer.ankihelper.util

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.os.AsyncTask
import android.util.Log

import com.example.antonrooseleer.ankihelper.R
import com.example.antonrooseleer.ankihelper.activity.OnboardingActivity
import com.example.antonrooseleer.ankihelper.event.OcrTextResult
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.json.GoogleJsonResponseException

import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.vision.v1.Vision
import com.google.api.services.vision.v1.VisionRequest
import com.google.api.services.vision.v1.VisionRequestInitializer
import com.google.api.services.vision.v1.model.AnnotateImageRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse
import com.google.api.services.vision.v1.model.Feature
import com.google.api.services.vision.v1.model.Image

import org.greenrobot.eventbus.EventBus

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.lang.ref.WeakReference
import java.util.ArrayList

import android.content.ContentValues.TAG
import com.example.antonrooseleer.ankihelper.BuildConfig

/**
 * Created by a_176 on 6/05/2018.
 */

class CloudVision(private val context: Context) {
    private val CLOUD_VISION_API_KEY = BuildConfig.ApiKey
    private var processedBitmap: Bitmap? = null

    fun uploadImage(bitmap: Bitmap) {
        callCloudVision(scaleBitmapDown(bitmap, MAX_DIMENSION))
        resources = context.resources
    }

    @Throws(IOException::class)
    private fun prepareAnnotationRequest(bitmap: Bitmap): Vision.Images.Annotate {
        val httpTransport = AndroidHttp.newCompatibleTransport()
        val jsonFactory = GsonFactory.getDefaultInstance()
        processedBitmap = bitmap
        val requestInitializer = object : VisionRequestInitializer(CLOUD_VISION_API_KEY) {
            /**
             * We override this so we can inject important identifying fields into the HTTP
             * headers. This enables use of a restricted cloud platform API key.
             */
            @Throws(IOException::class)
            override fun initializeVisionRequest(visionRequest: VisionRequest<*>?) {
                super.initializeVisionRequest(visionRequest)

                val packageName = context.packageName
                visionRequest!!.requestHeaders.set(ANDROID_PACKAGE_HEADER, packageName)

                val sig = PackageManagerUtils.getSignature(context.packageManager, packageName)

                visionRequest.requestHeaders.set(ANDROID_CERT_HEADER, sig)
            }
        }

        val builder = Vision.Builder(httpTransport, jsonFactory, null)
        builder.setVisionRequestInitializer(requestInitializer)

        val vision = builder.build()

        val batchAnnotateImagesRequest = BatchAnnotateImagesRequest()
        batchAnnotateImagesRequest.requests = object : ArrayList<AnnotateImageRequest>() {
            init {
                val annotateImageRequest = AnnotateImageRequest()

                // Add the image
                val base64EncodedImage = Image()
                // Convert the bitmap to a JPEG
                // Just in case it's a format that Android understands but Cloud Vision
                val byteArrayOutputStream = ByteArrayOutputStream()
                processedBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()

                // Base64 encode the JPEG
                base64EncodedImage.encodeContent(imageBytes)
                annotateImageRequest.image = base64EncodedImage

                // add the features we want
                annotateImageRequest.features = object : ArrayList<Feature>() {
                    init {
                        val textDetection = Feature()
                        textDetection.type = "TEXT_DETECTION"
                        textDetection.maxResults = MAX_LABEL_RESULTS
                        add(textDetection)
                    }
                }

                // Add the list of one thing to the request
                add(annotateImageRequest)
            }
        }

        val annotateRequest = vision.images().annotate(batchAnnotateImagesRequest)
        // Due to a bug: requests to Vision API containing large images fail when GZipped.
        annotateRequest.disableGZipContent = true
        Log.d(TAG, "created Cloud Vision request object, sending request")

        return annotateRequest
    }

    private class LableDetectionTask internal constructor(activity: OnboardingActivity, private val mRequest: Vision.Images.Annotate) : AsyncTask<Any, Void, String>() {
        private val mActivityWeakReference: WeakReference<OnboardingActivity>

        init {
            mActivityWeakReference = WeakReference(activity)
        }

        override fun doInBackground(vararg params: Any): String {
            try {
                Log.d(TAG, "created Cloud Vision request object, sending request")
                val response = mRequest.execute()
                sendResponseText(response)

            } catch (e: GoogleJsonResponseException) {
                Log.d(TAG, "failed to make API request because " + e.content)
            } catch (e: IOException) {
                Log.d(TAG, "failed to make API request because of other IOException " + e.message)
            }

            return "Cloud Vision API request failed. Check logs for details."
        }

        override fun onPostExecute(result: String) {

        }
    }

    private fun callCloudVision(bitmap: Bitmap) {
        // Switch text to loading
        // Do the real work in an async task, because we need to use the network anyway
        try {
            val labelDetectionTask = LableDetectionTask(context as OnboardingActivity, prepareAnnotationRequest(bitmap))
            labelDetectionTask.execute()
        } catch (e: IOException) {
            Log.d(TAG, "failed to make API request because of other IOException " + e.message)
        }

    }

    private fun scaleBitmapDown(bitmap: Bitmap, maxDimension: Int): Bitmap {

        val originalWidth = bitmap.width
        val originalHeight = bitmap.height
        var resizedWidth = maxDimension
        var resizedHeight = maxDimension

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = (resizedHeight * originalWidth.toFloat() / originalHeight.toFloat()).toInt()
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension
            resizedHeight = (resizedWidth * originalHeight.toFloat() / originalWidth.toFloat()).toInt()
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension
            resizedWidth = maxDimension
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false)
    }

    companion object {
        private val ANDROID_CERT_HEADER = "X-Android-Cert"
        private val ANDROID_PACKAGE_HEADER = "X-Android-Package"
        private val MAX_LABEL_RESULTS = 10
        private val MAX_DIMENSION = 1200
        private var resources: Resources? = null

        private fun sendResponseText(response: BatchAnnotateImagesResponse) {
            if (response.responses[0].fullTextAnnotation != null) {
                EventBus.getDefault().post(OcrTextResult(response.responses[0].fullTextAnnotation.text))
            } else {
                EventBus.getDefault().post(OcrTextResult(resources!!.getString(R.string.no_results)))
            }
        }
    }
}

