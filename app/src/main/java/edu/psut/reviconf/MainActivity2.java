package edu.psut.reviconf;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity2 extends Activity {



    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    private TaskStackBuilder stackBuilder;
    private static String[] Options = {"My Profile","My Conferences","Search a conference and join"};
    Intent intent ;
    private String username;
    ListView OptionsToSelect;
    ArrayAdapter<String> adapter;
    ImageView img;
    private Bitmap B;
   private static String IMAGE_URL = "https://scontent-cdg.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/1385863_10205100205797356_2423613737676426155_n.jpg?oh=a585ac6faac5506d418ea299ee949123&oe=55C5C633";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        //startNotification();


        Parse.initialize(getApplicationContext(), "Xx6QvGexra9Iumj5bTJoF7eshTPcvdgDpUpKzKUL", "Kf2h8zxdWD3r1SDU2Wf6Qv8WZxISFED33mwNMKW9");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("edu.psut.reviconf", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("edu.psut.reviconf", "failed to subscribe for push", e);
                }
            }
        });
        intent = getIntent();
        username = intent.getStringExtra("UserName");
       final String UserID = intent.getStringExtra("UserID");
        OptionsToSelect = (ListView) findViewById(R.id.MainOptions);


        img = (ImageView) findViewById(R.id.img);
        new GetImgFromUrl().execute();

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1,Options);
        OptionsToSelect.setAdapter(adapter);
        OptionsToSelect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0://My profile
                        Intent intent = new Intent(MainActivity2.this,MyProfile.class).putExtra("username",username);
                        startActivity(intent);
                        break;
                    case 1://my conferences
                        Intent intent1 = new Intent(MainActivity2.this,MyConferences.class).putExtra("UserID",UserID);
                        startActivity(intent1);
                        break;
                    case 2://search and join
                        Intent intent2 = new Intent(MainActivity2.this,conferences.class).putExtra("UserID",UserID);
                        startActivity(intent2);
                        break;


                }
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

                return super.onOptionsItemSelected(item);


    }





//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    protected void startNotification() {
//        //Creating Notification Builder
//        notification = new NotificationCompat.Builder(MainActivity2.this);
//        //Title for Notification
//        notification.setContentTitle("RevConf");
//        //Message in the Notification
//        notification.setContentText("Welcome " + getIntent().getStringExtra("FirstName"));
//        //Alert shown when Notification is received
//        notification.setTicker("welcome " + getIntent().getStringExtra("FirstName"));
//        //Icon to be set on Notification
//        notification.setSmallIcon(R.drawable.ic_action_done);
//        //Creating new Stack Builder
//        stackBuilder = TaskStackBuilder.create(MainActivity2.this);
//        stackBuilder.addParentStack(MainActivity.class);
//        //Intent which is opened when notification is clicked
//        resultIntent = new Intent(MainActivity2.this, MainActivity.class);
//        stackBuilder.addNextIntent(resultIntent);
//        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//        notification.setContentIntent(pIntent);
//        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.notify(0, notification.build());
//    }



class GetImgFromUrl extends AsyncTask<Void,Void,Void>{
    private Bitmap bmp;
    @Override
    protected Void doInBackground(Void... params) {
        try {
            InputStream in = new URL(IMAGE_URL).openStream();
            bmp = BitmapFactory.decodeStream(in);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void result) {
        if (bmp != null){
            img.setImageBitmap(bmp);
            img.getLayoutParams().width = 350;
            img.getLayoutParams().height = 350;

        }
    }

}

}

