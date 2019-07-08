package com.darryncampbell.jokegenerator

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.darryncampbell.jokegenerator.Jokes.Companion.jokes
import android.support.v4.app.NotificationCompat
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationManagerCompat
import android.widget.Button
import android.widget.Toast


class MainActivity : AppCompatActivity() {

    private val LogTag = "JokeGen"
    private val CHANNEL_ID = "100"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        val testButton = findViewById(R.id.btnTest) as Button
        testButton.setOnClickListener {
            showJoke(giveJoke())
        }
        Log.d(LogTag, "On Create")
        if (intent.hasExtra("JokeRequest"))
        {
            showJoke(giveJoke())
            finish()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        Log.d(LogTag, "On New Intent")
    }

    fun giveJoke() : String
    {
        val randomNumber = (Math.random () * jokes.size).toInt()
        return jokes.get(randomNumber)
    }

    fun showJoke(joke: String)
    {
        //  Log the joke
        Log.d(LogTag, "Hilarious Joke: " + joke)

        //  Show the joke in a Toast
        Toast.makeText(this, joke, Toast.LENGTH_LONG).show()

        //  Set the joke as the result for StartActivityForResult
        val jokeIntent = Intent()
        jokeIntent.putExtra("theJoke", joke)
        setResult(RESULT_OK, jokeIntent)

        //  Show the joke in a notification
        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Hilarious Joke")
            .setContentText(joke)
            .setStyle(NotificationCompat.BigTextStyle().bigText(joke))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1001, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Joke Notifications"
            val descriptionText = "Jokes"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
