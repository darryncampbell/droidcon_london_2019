package com.darryncampbell.jokegenerator

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class JokeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        // This method is called when the BroadcastReceiver is receiving an Intent broadcast.
        //  Extract the Pending Intent
        if (intent.hasExtra("callbackPI"))
        {
            val piResponse = intent.getParcelableExtra<PendingIntent>("callbackPI")
            val randomNumber = (Math.random () * Jokes.jokes.size).toInt()
            val jokeString = Jokes.jokes.get(randomNumber)
            val jokeIntent = Intent()
            jokeIntent.putExtra("joke", jokeString)
            piResponse.send(context, 0, jokeIntent, null, null)
        }
    }
}
