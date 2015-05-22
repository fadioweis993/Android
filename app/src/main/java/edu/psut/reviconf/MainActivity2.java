package edu.psut.reviconf;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


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
    /*******************************************************/
    public static final String EXTRA_MESSAGE = "message";
    public static  String PROPERTY_REG_ID;
    private static  String PROPERTY_APP_VERSION;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "4055938811";
    GoogleCloudMessaging gcm;
    String regid;
    Context context;
    static final String TAG = "GCM Demo";
    AtomicInteger msgId = new AtomicInteger();
    /*******************************************************/
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2);
        //startNotification();
/***/ context = getApplicationContext();/**/

        intent = getIntent();
        username = intent.getStringExtra("UserName");
        UserID = intent.getStringExtra("UserID");

            if (checkPlayServices()) {
                gcm = GoogleCloudMessaging.getInstance(this);
                regid = getRegistrationId(context);
                if (regid.isEmpty()) {
                    Log.d("GCM","you are registering");

                    registerInBackground();
                }
            } else {
                Log.i("GCM", "No valid Google Play Services APK found.");
            }

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
                switch (position) {
                    case 0://My profile
                        Intent intent = new Intent(MainActivity2.this, MyProfile.class).putExtra("username", username);
                        intent.putExtra("userID", UserID);
                        startActivity(intent);

                        break;
                    case 1://my conferences
                        Intent intent1 = new Intent(MainActivity2.this, MyConferences.class).putExtra("UserID", UserID);
                        startActivity(intent1);
                        break;
                    case 2://search and join
                        Intent intent2 = new Intent(MainActivity2.this, conferences.class).putExtra("UserID", UserID);
                        startActivity(intent2);
                        break;


                }
            }
        });



        }






    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity2.this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

                finish();
            }
            return false;
        }
        return true;
    }

    /***********************************************************************/
    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences(context);
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }


    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     *         registration ID.
     */
    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Check device for Play Services APK.
        checkPlayServices();
    }

    /**/

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;
                    Log.d("REGID",regid);
                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("GCM",msg);
            }
        }.execute(null, null, null);
    }
    /******************************************************************************************/
    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences, but
        // how you store the regID in your app is up to you.
        return getSharedPreferences(MainActivity2.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }
    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    /***************************************************************************************/
    private void sendRegistrationIdToBackend() {
        // Your implementation here.
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                List<NameValuePair> p = new ArrayList<NameValuePair>();
                p.add(new BasicNameValuePair("regId", regid));
                p.add(new BasicNameValuePair("ID", UserID));
                JSONParser jsonParser = new JSONParser();
                jsonParser.makeHttpRequest("192.168.1.5/webservice/register.php","POST",p);

                return null;
            }
        };
    }
    /*******************************************************************************/
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

