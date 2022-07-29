package com.ahmety.pokedex.service

import android.app.*
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.util.DisplayMetrics
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.ahmety.pokedex.R
import com.ahmety.pokedex.ui.detail.DetailViewModel.Companion.ACTION_STOP_FOREGROUND
import com.bumptech.glide.Glide
import kotlin.math.roundToInt

class OverlayControlService : Service() {

    private var windowManager: WindowManager? = null
    private var floatingControlView: ViewGroup? = null
    private lateinit var dismissButton: Button
    private lateinit var textPokemonName: TextView
    private lateinit var imgPokemonBehind: ImageView
    private lateinit var imgPokemonFront: ImageView

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (windowManager == null) {
            windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        }
        if (floatingControlView == null) {
            val li = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
            floatingControlView = li.inflate(R.layout.layout_overlay, null) as ViewGroup?
        }
        floatingControlView?.let {
            textPokemonName = it.findViewById(R.id.txtPokemonName)
            dismissButton = it.findViewById(R.id.btnClose)
            imgPokemonBehind = it.findViewById(R.id.imgBehind)
            imgPokemonFront = it.findViewById(R.id.imgFront)
            textPokemonName.text = intent?.getStringExtra("pokemonName")
            Glide.with(imgPokemonBehind).load(intent?.getStringExtra("frontPhoto"))
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imgPokemonBehind)
            Glide.with(imgPokemonFront).load(intent?.getStringExtra("behindPhoto"))
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imgPokemonFront)

            dismissButton.setOnClickListener {
                removeFloatingControl()
            }
        }
        if (intent?.action != null && intent.action.equals(
                ACTION_STOP_FOREGROUND, ignoreCase = true
            )
        ) {
            removeFloatingControl()
            stopForeground(true)
            stopSelf()
        } else {
            addFloatingMenu()
        }
        return START_NOT_STICKY
    }

    private fun removeFloatingControl() {
        if (floatingControlView?.parent != null) {
            windowManager?.removeView(floatingControlView)
        }
    }

    private fun addFloatingMenu() {
        if (floatingControlView?.parent == null) {
            //Set layout params to display the controls over any screen.
            val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) WindowManager.LayoutParams.TYPE_PHONE else WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
            params.height = dpToPx(250)
            params.width = dpToPx(270)

            //Initial position of the floating controls
            params.gravity = Gravity.TOP or Gravity.START
            params.x = 200
            params.y = 600

            //Add the view to window manager
            windowManager?.addView(floatingControlView, params)
        }
    }

    //Method to convert dp to px
    private fun dpToPx(dp: Int): Int {
        val displayMetrics = this.resources.displayMetrics
        return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
    }

}