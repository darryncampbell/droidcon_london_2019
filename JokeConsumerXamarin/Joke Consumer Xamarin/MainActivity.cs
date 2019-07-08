using System;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Support.V4.Content;
using Android.Support.V7.App;
using Android.Views;
using Android.Widget;

namespace Joke_Consumer_Xamarin
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        BroadcastResponseReceiver broadcastResponseReceiver;
        public TextView output;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.activity_main);
            output = FindViewById<TextView>(Resource.Id.output);

            Android.Support.V7.Widget.Toolbar toolbar = FindViewById<Android.Support.V7.Widget.Toolbar>(Resource.Id.toolbar);
            SetSupportActionBar(toolbar);

            Button btnStartActivity = FindViewById<Button>(Resource.Id.btnStartActivity);
            btnStartActivity.Click += StartActivityClicked;

            Button btnStartActivityForResult = FindViewById<Button>(Resource.Id.btnStartActivityForResult);
            btnStartActivityForResult.Click += StartActivityForResultClicked;

            Button btnSendBroadcast = FindViewById<Button>(Resource.Id.btnSendBroadcast);
            btnSendBroadcast.Click += SendBroadcastClicked;

        }

        public void StartActivityClicked(object sender, EventArgs args)
        {
            Intent requestJokeIntent = new Intent();
            requestJokeIntent.SetClassName(
                "com.darryncampbell.jokegenerator", 
                "com.darryncampbell.jokegenerator.MainActivity");
            requestJokeIntent.PutExtra("JokeRequest", "");
            StartActivity(requestJokeIntent);
        }

        public void StartActivityForResultClicked(object sender, EventArgs args)
        {
            Intent requestJokeIntent = new Intent();
            requestJokeIntent.SetClassName(
                "com.darryncampbell.jokegenerator", 
                "com.darryncampbell.jokegenerator.MainActivity");
            requestJokeIntent.PutExtra("JokeRequest", "");
            StartActivityForResult(requestJokeIntent, 100);
        }
        private void SendBroadcastClicked(object sender, EventArgs e)
        {
            Intent requestJokeIntent = new Intent();
            requestJokeIntent.SetClassName(
                "com.darryncampbell.jokegenerator", 
                "com.darryncampbell.jokegenerator.JokeReceiver");
            Intent responseIntent = new Intent(this, 
                Java.Lang.Class.FromType(typeof(JokeConsumerXamarinBroadcastReceiver)));
            PendingIntent piResponse = 
                PendingIntent.GetBroadcast(Application.Context, 0, responseIntent, 0);
            requestJokeIntent.PutExtra("callbackPI", piResponse);
            SendBroadcast(requestJokeIntent);
        }


        protected override void OnActivityResult(int requestCode, Result resultCode, Intent data)
        {
            base.OnActivityResult(requestCode, resultCode, data);

            switch (resultCode)
            {
                case Result.Ok:
                    {
                        string theJoke = data.GetStringExtra("theJoke");
                        output.Text = "From StartActivityForResult: " + theJoke;
                        break;
                    }
            }

        }

        protected override void OnResume()
        {
            base.OnResume();
            IntentFilter broadcastFilter = new IntentFilter("LOCAL_ACTION");
            broadcastResponseReceiver = new BroadcastResponseReceiver(this);
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.GetInstance(this);
            localBroadcastManager.RegisterReceiver(broadcastResponseReceiver, broadcastFilter);
        }

        protected override void OnPause()
        {
            base.OnPause();
            LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.GetInstance(this);
            localBroadcastManager.UnregisterReceiver(broadcastResponseReceiver);
        }

        public override bool OnCreateOptionsMenu(IMenu menu)
        {
            MenuInflater.Inflate(Resource.Menu.menu_main, menu);
            return true;
        }

        public override bool OnOptionsItemSelected(IMenuItem item)
        {
            int id = item.ItemId;
            if (id == Resource.Id.action_settings)
            {
                return true;
            }

            return base.OnOptionsItemSelected(item);
        }

        class BroadcastResponseReceiver : BroadcastReceiver
        {
            MainActivity _mainActivity;

            public BroadcastResponseReceiver(MainActivity activity) : base()
            {
                _mainActivity = activity;
            }

            public override void OnReceive(Context context, Intent intent)
            {
                if (intent.HasExtra("joke"))
                {
                   
                    string joke = intent.GetStringExtra("joke");
                    _mainActivity.output.Text = "From Broadcast Receiver: " + joke;
                }
            }
        }

    }
}

