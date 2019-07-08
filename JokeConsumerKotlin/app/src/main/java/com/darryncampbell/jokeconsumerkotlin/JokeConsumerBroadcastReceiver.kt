package com.darryncampbell.jokeconsumerkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager

class JokeConsumerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //  Send the joke to the Main Activity
        val responseIntent = Intent()
        responseIntent.action = "LOCAL_ACTION"
        responseIntent.putExtra("joke", intent.getStringExtra("joke"))
        val localBroadcastManager = LocalBroadcastManager.getInstance(context)
        localBroadcastManager.sendBroadcast(responseIntent)

    }
}
