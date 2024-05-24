package com.example.contactstask

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MainBroadCastReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == "com.example.MainBroadCastReceiver") {
            Toast.makeText(context, "Broadcast received!", Toast.LENGTH_SHORT).show()
        }

    }
}