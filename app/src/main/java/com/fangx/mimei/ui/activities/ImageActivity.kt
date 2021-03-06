package com.fangx.mimei.ui.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewCompat
import android.view.MenuItem
import android.view.View
import com.fangx.mimei.R
import com.fangx.mimei.ui.base.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*
import org.jetbrains.anko.displayMetrics
import org.jetbrains.anko.doAsyncResult
import org.jetbrains.anko.info

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ImageActivity : BaseActivity() {
    private val mHideHandler = Handler()
    private val mHidePart2Runnable = Runnable {
        // Delayed removal of status and navigation bar

        // Note that some of these constants are new as of API 16 (Jelly Bean)
        // and API 19 (KitKat). It is safe to use them, as they are inlined
        // at compile-time and do nothing on earlier devices.
        fullscreen_content.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LOW_PROFILE or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }
//    private val mShowPart2Runnable = Runnable {
//        // Delayed display of UI elements
//        supportActionBar?.show()
//        fullscreen_content_controls.visibility = View.VISIBLE
//    }
    private var mVisible: Boolean = false
//    private val mHideRunnable = Runnable { hide() }
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private val mDelayHideTouchListener = View.OnTouchListener { _, _ ->
        if (AUTO_HIDE) {
//            delayedHide(AUTO_HIDE_DELAY_MILLIS)
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_image)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        // Set up the user interaction to manually show or hide the system UI.
//        fullscreen_content.setOnClickListener { toggle() }

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
//        dummy_button.setOnTouchListener(mDelayHideTouchListener)

        ViewCompat.setTransitionName(fullscreen_content, DetailActivity.KEY_SHARE_ID)


        val url = intent.getStringExtra(KEY_IMG_URL)
        info {
            "url = $url"
        }
        display(url)
    }

    private fun display(imageUrl: String?) {
        val size = doAsyncResult {
            var bitmap: Bitmap?
            bitmap = try {
                Picasso.get()
                        .load(imageUrl)
                        .get()
            } catch (e: Exception) {
                e.printStackTrace()
                Picasso.get()
                        .load(R.drawable.img_place_holder)
                        .get()
            }
            listOf(bitmap.width, bitmap.height)
        }.get()
        info {
            "width=${size[0]} , height=${size[1]}"
        }
//        val padding = (displayMetrics.density * 4).toInt()
//        val margin = (displayMetrics.density * 12).toInt()
        val imageTargetWidth = displayMetrics.widthPixels
        val scale: Float = imageTargetWidth / size[0].toFloat()
        val imageTargetHeight = (size[1] * scale).toInt()
        info {
            "scale=$scale , imageTargetWidth=$imageTargetWidth , imageTargetHeight=$imageTargetHeight"
        }
        //        fullscreen_content.layoutParams = ViewGroup.LayoutParams(imageTargetHeight, imageTargetHeight)
        Picasso.get()
                .load(imageUrl)
                .error(R.drawable.img_place_holder)
                .resize(imageTargetWidth, imageTargetHeight)
                .centerCrop()
                .into(fullscreen_content)

        fullscreen_content.setOnClickListener{
            onBackPressed()
        }


    }

//    override fun onPostCreate(savedInstanceState: Bundle?) {
//        super.onPostCreate(savedInstanceState)
//
//        // Trigger the initial hide() shortly after the activity has been
//        // created, to briefly hint to the user that UI controls
//        // are available.
//        delayedHide(100)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

//    private fun toggle() {
//        if (mVisible) {
//            hide()
//        } else {
//            show()
//        }
//    }
//
//    private fun hide() {
//        // Hide UI first
//        supportActionBar?.hide()
//        fullscreen_content_controls.visibility = View.GONE
//        mVisible = false
//
//        // Schedule a runnable to remove the status and navigation bar after a delay
//        mHideHandler.removeCallbacks(mShowPart2Runnable)
//        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY.toLong())
//    }
//
//    private fun show() {
//        // Show the system bar
//        fullscreen_content.systemUiVisibility =
//                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//        mVisible = true
//
//        // Schedule a runnable to display UI elements after a delay
//        mHideHandler.removeCallbacks(mHidePart2Runnable)
//        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY.toLong())
//    }
//
//    /**
//     * Schedules a call to hide() in [delayMillis], canceling any
//     * previously scheduled calls.
//     */
//    private fun delayedHide(delayMillis: Int) {
//        mHideHandler.removeCallbacks(mHideRunnable)
//        mHideHandler.postDelayed(mHideRunnable, delayMillis.toLong())
//    }


    companion object {
        /**
         * Whether or not the system UI should be auto-hidden after
         * [AUTO_HIDE_DELAY_MILLIS] milliseconds.
         */
        private val AUTO_HIDE = true

        /**
         * If [AUTO_HIDE] is set, the number of milliseconds to wait after
         * user interaction before hiding the system UI.
         */
        private val AUTO_HIDE_DELAY_MILLIS = 3000

        /**
         * Some older devices needs a small delay between UI widget updates
         * and a change of the status and navigation bar.
         */
        private val UI_ANIMATION_DELAY = 300

        const val KEY_IMG_URL = "key_img_url"
    }
}
