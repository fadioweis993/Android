package edu.psut.reviconf;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity2 extends Activity {


    NotificationCompat.Builder notification;
    PendingIntent pIntent;
    NotificationManager manager;
    Intent resultIntent;
    private TaskStackBuilder stackBuilder;
    private static String[] Options = {"My Profile","My Conferences","Search a conference and join"};
    Intent intent ;
    private String username;
    ListView OptionsToSelect;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        startNotification();

        intent = getIntent();
        username = intent.getStringExtra("UserName");
       final String UserID = intent.getStringExtra("UserID");
        OptionsToSelect = (ListView) findViewById(R.id.MainOptions);



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



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    protected void startNotification() {
        //Creating Notification Builder
        notification = new NotificationCompat.Builder(MainActivity2.this);
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


}
