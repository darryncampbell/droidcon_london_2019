package com.darryncampbell.jokeconsumerkotlin

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.Button

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    val request_id = 100;
    internal lateinit var broadcastResponseReceiver: BroadcastResponseReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val startActivityButton = findViewById(R.id.btnViaStartActivity) as Button
        startActivityButton.setOnClickListener {
            val requestJokeIntent = Intent()
            requestJokeIntent.setClassName("com.darryncampbell.jokegenerator",
                "com.darryncampbell.jokegenerator.MainActivity")
            requestJokeIntent.putExtra("JokeRequest", "")
            startActivity(requestJokeIntent)
        }

        val startActivityForResultButton = findViewById(R.id.btnViaStartActivityForResult) as Button
        startActivityForResultButton.setOnClickListener {
            val requestJokeIntent = Intent()
            requestJokeIntent.setClassName(
                "com.darryncampbell.jokegenerator",
                "com.darryncampbell.jokegenerator.MainActivity")
            requestJokeIntent.putExtra("JokeRequest", "")
            startActivityForResult(requestJokeIntent, 0)
        }

        val sendBroadcastButton = findViewById(R.id.btnViaSendBroadcast) as Button
        sendBroadcastButton.setOnClickListener {
            val requestJokeIntent = Intent()
            requestJokeIntent.setClassName(
                "com.darryncampbell.jokegenerator",
                "com.darryncampbell.jokegenerator.JokeReceiver")
            val responseIntent = Intent(this,
                JokeConsumerBroadcastReceiver::class.java)
            val piResponse = PendingIntent.getBroadcast(
                applicationContext, 0, responseIntent, 0)
            requestJokeIntent.putExtra("callbackPI", piResponse)
            sendBroadcast(requestJokeIntent)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        if (requestCode == request_id)
        {
            if (resultCode == Activity.RESULT_OK)
            {
                //  Process the received Joke
                val theJoke = data?.getStringExtra("theJoke");
                output.setText("From StartActivityForResult: " + theJoke)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        //  Register the broadcast receiver used to receive UI updates from the IntentService
        val broadcastFilter = IntentFilter("LOCAL_ACTION")
        broadcastResponseReceiver = BroadcastResponseReceiver()
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.registerReceiver(broadcastResponseReceiver, broadcastFilter)
    }

    override fun onPause() {
        super.onPause()
        //  Unregister the broadcast receiver used to receive UI updates from the IntentService
        val localBroadcastManager = LocalBroadcastManager.getInstance(this)
        localBroadcastManager.unregisterReceiver(broadcastResponseReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    inner class BroadcastResponseReceiver : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            var joke = ""
            if (intent.hasExtra("joke"))
                joke = intent.getStringExtra("joke")
            output.setText("From Broadcast Receiver: " + joke)
        }
    }
}
