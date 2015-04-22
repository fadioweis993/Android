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
                        Intent intent2 = new Intent(MainActivity2.this,conferences.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        break;

                    //my papers

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

        switch (id) {
//            case R.id.action_call:
//                Intent dialer = new Intent(Intent.ACTION_CALL);
//                startActivity(dialer);
//                return true;
//            case R.id.action_speech:
//                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//                startActivityForResult(intent, 1234);
//
//                return true;
//            case R.id.action_done:
//
//                Bundle args = new Bundle();
//                args.putString("Menu", "You pressed done button.");
//

//                FragmentManager fragmentManager = getFragmentManager();

//
//                return true;
            case R.id.action_contacts:
                Toast.makeText(getApplicationContext(), "Contacts Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_settings:
                Toast.makeText(getApplicationContext(), "Settings Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_status:
                Toast.makeText(getApplicationContext(), "Status Clicked", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //EditText et = (EditText) findViewById(R.id.editText3);
//        if (requestCode == 1234 && resultCode == RESULT_OK) {
//            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
//            Toast.makeText(getApplicationContext(), voice_text, Toast.LENGTH_LONG).show();
//         //   et.setText(voice_text);
//
//        }
//    }

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
