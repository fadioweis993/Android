package edu.psut.reviconf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyProfile extends Activity {
    private ImageView profilePic;
    private Bitmap bitmap;
    private String imgUrl = "http://icons.iconarchive.com/icons/crountch/one-piece-jolly-roger/72/Luffys-flag-2-icon.png";
    private static final String LOGIN_URL = "http://192.168.1.2/webservice/personalData.php";
    private static final String TAG_MESSAGE = "message";
    private TextView fname;
    private TextView E_mail;
    private TextView person_title;
    private TextView address;
    private TextView Date_registered;
    private TextView educational_degree;
    private TextView age;
     Intent i;
    private String username ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        new getPersonInfo().execute();
        //      getActionBar().setDisplayHomeAsUpEnabled(true);
        profilePic = (ImageView) findViewById(R.id.ivImage);
        bitmap = getBitmapFromURL(imgUrl);
        profilePic.setImageBitmap(bitmap);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    public class getPersonInfo extends AsyncTask<String, String, Boolean> {

        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(MyProfile.this);
            pdialog.setMessage("Getting your personal data .... ");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(true);
            pdialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {

                fname = (TextView) findViewById(R.id.fname);
                E_mail = (TextView) findViewById(R.id.E_mail);
                person_title = (TextView) findViewById(R.id.person_title);
                address = (TextView) findViewById(R.id.Address);
                Date_registered = (TextView) findViewById(R.id.date_registered);
                educational_degree = (TextView) findViewById(R.id.Educational_Degree);
                age = (TextView) findViewById(R.id.age);
                i = getIntent();
                username = i.getStringExtra("username");
                List<NameValuePair> personalInfo = new ArrayList<NameValuePair>();
                personalInfo.add(new BasicNameValuePair("Email",username));

                JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", personalInfo);

                Log.d("JSON_I_GOT",json.toString());

                String Title = json.getString("Title");
                String Age = json.getString("Age");
                String Address = json.getString("Address");
                String Email = json.getString("E-mail");
                String Educational_Degree = json.getString("Educational_Degree");
                String date_registered = json.getString("date_registered");
                String f_name = json.getString("f_name");
                String l_name = json.getString("l_name");

                fname.setText(f_name + " " + l_name);
                E_mail.setText(Email);
                person_title.setText(Title);
                address.setText(Address);
                Date_registered.setText(date_registered);
                educational_degree.setText(Educational_Degree);
                age.setText(Age);

                return true;


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            pdialog.dismiss();



        }
    }

}

