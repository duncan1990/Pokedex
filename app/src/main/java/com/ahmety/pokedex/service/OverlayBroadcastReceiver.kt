package com.ahmety.pokedex.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class OverlayBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // logic of the code needs to be written here
        Toast.makeText(context, "Overlay Working", Toast.LENGTH_SHORT).show()
    }
}