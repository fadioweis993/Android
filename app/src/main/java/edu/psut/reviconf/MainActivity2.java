package edu.psut.reviconf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;



public class MainActivity2 extends Activity {
    int backButtonCount;
    private static String[] Options = {"My Profile","My Conferences","Search a conference and join"};
    Intent intent ;
    private String username;
    ListView OptionsToSelect;
    ArrayAdapter<String> adapter;
    ImageView img;
    private Bitmap B;
    private String UserID;
    private NotificationManager mNotificationManager;
    private int notificationID = 100;

    public static String GET_IMG_URL_FROM_DB = "http://newfaceapps.site90.com/getImg.php";
   private static String IMAGE_URL = "https://scontent-cdg.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/1385863_10205100205797356_2423613737676426155_n.jpg?oh=a585ac6faac5506d418ea299ee949123&oe=55C5C633";//IMAGE_URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        //startNotification();


        notificationForMe();

        
        intent = getIntent();
        username = intent.getStringExtra("UserName");
        UserID = intent.getStringExtra("UserID");
        OptionsToSelect = (ListView) findViewById(R.id.MainOptions);
        if(SaveSharedPreference.getUserName(MainActivity2.this).equals(null))
        {
            Intent i = new Intent(MainActivity2.this,MainActivity.class);
            startActivity(i);
        }



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
                        intent.putExtra("userID",UserID);
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
    public void onBackPressed() {

        if(backButtonCount == 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount = 0;
            backButtonCount++;
        }
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

   /* @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void startNotification() {
       // Creating Notification Builder
        notification = new NotificationCompat(MainActivity2.this);
        //Title for Notification
        notification.setContentTitle("RevConf");
        //Message in the Notification
        notification.setContentText("Welcome " + getIntent().getStringExtra("FirstName"));
        //Alert shown when Notification is received
        notification.setTicker("welcome " + getIntent().getStringExtra("FirstName"));
        //Icon to be set on Notification
        notification.setSmallIcon(R.drawable.ic_action_done);
        //Creating new Stack Builder
       stackBuilder = TaskStackBuilder.create(MainActivity2.this);
        stackBuilder.addParentStack(MainActivity.class);
        //Intent which is opened when notification is clicked
        resultIntent = new Intent(MainActivity2.this, MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        pIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pIntent);
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }
*/

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
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void notificationForMe (){
        notificationID++;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.revconf);
        mBuilder.setContentTitle("Welcome");
        mBuilder.setContentText(getIntent().getStringExtra("FirstName"));
        String username = SaveSharedPreference.getUserName(getApplicationContext());
        Intent resultIntent = new Intent(MainActivity2.this, MainActivity2.class).putExtra("UserID", username);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
      mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        mBuilder.setVibrate(new long[]{1000, 1000});
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// notificationID allows you to update the notification later on.
        mNotificationManager.notify(notificationID, mBuilder.build());
    }


}

