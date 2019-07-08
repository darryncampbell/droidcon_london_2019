using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Support.V4.Content;
using Android.Views;
using Android.Widget;

namespace Joke_Consumer_Xamarin
{
    [BroadcastReceiver]
    public class JokeConsumerXamarinBroadcastReceiver : BroadcastReceiver
    {
        public override void OnReceive(Context context, Intent intent)
        {
            Intent responseIntent = new Intent();
            responseIntent.SetAction("LOCAL_ACTION");
            responseIntent.PutExtra("joke", intent.GetStringExtra("joke"));
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.GetInstance(context);
            localBroadcastManager.SendBroadcast(responseIntent);
        }
    }
}