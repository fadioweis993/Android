package edu.psut.reviconf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
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
    private String imgUrl = "https://scontent-fra.xx.fbcdn.net/hphotos-xfp1/v/t1.0-9/1385863_10205100205797356_2423613737676426155_n.jpg?oh=166a670103eb9ce756b155de6cded5d0&oe=559E3933";
    private static final String LOGIN_URL = "http://newfaceapps.site90.com/personalData.php";
    private static final String TAG_MESSAGE = "message";
    private TextView fname;
    private TextView E_mail;
    private TextView person_title;
    private TextView address;
    private TextView Date_registered;
    private TextView educational_degree;
    private TextView age;
    TextView textToView;
    String PersonalData[];
    LinearLayout personalData;
    int size;
    int count = 0;
     Intent i;
    private String username ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        new getPersonInfo().execute();

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

                /*fname = (TextView) findViewById(R.id.fname);
                E_mail = (TextView) findViewById(R.id.E_mail);
                person_title = (TextView) findViewById(R.id.person_title);
                address = (TextView) findViewById(R.id.Address);
                Date_registered = (TextView) findViewById(R.id.date_registered);
                educational_degree = (TextView) findViewById(R.id.Educational_Degree);
                age = (TextView) findViewById(R.id.age);*/
                i = getIntent();
                username = i.getStringExtra("username");
                List<NameValuePair> personalInfo = new ArrayList<NameValuePair>();
                personalInfo.add(new BasicNameValuePair("Email",username));

                JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", personalInfo);
                JSONArray jsonArray = json.getJSONArray("personalData");
                size = jsonArray.length();
                PersonalData = new String[size];
                for (int c=0;c<=jsonArray.length();c++){
                    JSONObject jsonObject = jsonArray.getJSONObject(c);

                    textToView = new TextView(MyProfile.this);
                    textToView.setText("Name: " + jsonObject.getString("FirstName") + jsonObject.getString("LastName")+ "\n"
                            + "\n" + "E-mail: " + jsonObject.getString("Email") + "\n"
                            + "\n" + "Scientific Degree" + jsonObject.getString("scientific_degrees")+ "\n"
                            + "\n" + "Date of registration: " + jsonObject.getString("date_registered")+ "\n"
                            + "\n" + "City: " + jsonObject.getString("city")+ "\n"
                            + "\n" + "Age: " + jsonObject.getString("Age")
                    );
                    textToView.setTextSize(20);
                    run();
                }

                Log.d("JSON_I_GOT",json.toString());

                /*final String Title = json.getString("Title");
                final String Age = json.getString("Age");
                final String Address = json.getString("Address");
                final String Email = json.getString("E-mail");
                final String Educational_Degree = json.getString("Educational_Degree");
                final  String date_registered = json.getString("date_registered");
                final  String f_name = json.getString("f_name");
                final  String l_name = json.getString("l_name");
*/
               /* MyProfile.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        fname.setText(f_name + " " + l_name);
                        E_mail.setText(Email);
                        person_title.setText(Title);
                        address.setText(Address);
                        Date_registered.setText(date_registered);
                        educational_degree.setText(Educational_Degree);
                        age.setText(Age);
                    }
                });
*/




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
    public void run() {
        MyProfile.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                personalData = (LinearLayout) findViewById(R.id.Profile);
                personalData.addView(textToView);
            }

        });
    }
}