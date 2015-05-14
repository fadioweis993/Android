package edu.psut.reviconf;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class MainActivity2 extends ActionBarActivity {
    int backButtonCount;
    private static String[] Options = {"My Profile","My Conferences","Search a conference and join"};
    private int[] imageId = {R.drawable.menu_speakers_normal,R.drawable.menu_agenda_normal,R.drawable.menu_about_normal};
    Intent intent ;
    private String username;
    GridView OptionsToSelect;
    private String UserID;
    private NotificationManager mNotificationManager;
    private int notificationID = 100;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        //startNotification();

        intent = getIntent();
        username = intent.getStringExtra("UserName");
        UserID = intent.getStringExtra("UserID");

        if(SaveSharedPreference.getUserName(MainActivity2.this).equals(null))
        {
            Intent i = new Intent(MainActivity2.this,MainActivity.class);
            startActivity(i);
        }



       //adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.grid_single,Options);
       CustomGrid adapter = new CustomGrid(MainActivity2.this, Options, imageId);
            OptionsToSelect = (GridView) findViewById(R.id.grid);
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
        getMenuInflater().inflate(R.menu.menu_main_activity2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout){
            SaveSharedPreference.setUserName(getApplicationContext(),"");
            finish();
        }
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

