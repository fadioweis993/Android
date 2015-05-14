package edu.psut.reviconf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyProfile extends ActionBarActivity {
    private ImageView profilePic;
    private Bitmap bitmap;
    public static String GET_IMG_URL_FROM_DB;
    private static String IMAGE_URL;
    private static String LOGIN_URL;
    private JSONArray jsonArray;
    String FirstName;
    String LastName;
    String Email;
    String scientific_degree;
    String date_registered;
    String city;
    String Age;
    TextView textToView;
    TextView firstName;
    LinearLayout personalData;
    int size;
    int FLAG;
     Intent i;
    private String userID ;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_profile);
        LOGIN_URL = this.getString(R.string.server_name) + "personalData.php";
        GET_IMG_URL_FROM_DB = this.getString(R.string.server_name) + "getImg.php";
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        Log.d("FLAG",String.valueOf(FLAG));
        Intent intent = getIntent();
        userName = intent.getStringExtra("username");

        if (FLAG != 1){
            FLAG++;
            new getPersonInfo().execute();
        }else{
            setData();
        }

        profilePic = (ImageView) findViewById(R.id.iv_speaker_photo);
       new GetImgFromUrl().execute();
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

                i = getIntent();
                userID = i.getStringExtra("userID");
                List<NameValuePair> personalInfo = new ArrayList<NameValuePair>();
                personalInfo.add(new BasicNameValuePair("userID",userID));

                List<NameValuePair> imgFromDB = new ArrayList<NameValuePair>();
                imgFromDB.add(new BasicNameValuePair("userID",userID));

                JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", personalInfo);

                JSONParser jsonParser1 = new JSONParser();
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1 = jsonParser1.makeHttpRequest(GET_IMG_URL_FROM_DB, "POST", imgFromDB);
               jsonArray = json.getJSONArray("personalData");

                IMAGE_URL = jsonObject1.getString("URL");
                IMAGE_URL.replace('\\', '/');
                Log.d("STRING",IMAGE_URL);
                SaveSharedPreference.setJsonArr(getApplicationContext(),jsonArray);
                size = jsonArray.length();
                textToView = (TextView) findViewById(R.id.tv_description);
                for (int c=0;c<jsonArray.length();c++){
                    JSONObject jsonObject = jsonArray.getJSONObject(c);
                    FirstName = jsonObject.getString("FirstName");
                    LastName = jsonObject.getString("LastName");
                    Email = jsonObject.getString("Email");
                    scientific_degree = jsonObject.getString("scientific_degree");
                    date_registered = jsonObject.getString("date_registered");
                    city = jsonObject.getString("city");
                    Age = jsonObject.getString("Age");

                    textToView = new TextView(MyProfile.this);
                    textToView.setText(
                            "\n" + "E-mail: " + Email + "\n"
                                    + "\n" + "Scientific Degree: " + scientific_degree + "\n"
                                    + "\n" + "Date of registration: " + date_registered + "\n"
                                    + "\n" + "City: " + city + "\n"
                                    + "\n" + "Age: " + Age + "\n"


                    );

                    run();
                }

                Log.d("JSON_I_GOT",json.toString());

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
                textToView.setHintTextColor(getResources().getColor(R.color.conf_gray));
                firstName = (TextView) findViewById(R.id.tv_speaker_name);
                firstName.setText(FirstName +" "+ LastName);
            }

        });
    }

    public void setData (){
        for (int c=0;c<=jsonArray.length();c++){
            textToView = new TextView(MyProfile.this);
            textToView.setText("Name: " + FirstName +" "+ LastName+ "\n"
                            + "\n" + "E-mail: " + Email + "\n"
                            + "\n" + "Scientific Degree: " + scientific_degree+ "\n"
                            + "\n" + "Date of registration: " + date_registered + "\n"
                            + "\n" + "City: " + city+ "\n"
                            + "\n" + "Age: " + Age
            );
            textToView.setTextSize(20);
            run();
        }
    }

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
                profilePic.setImageBitmap(bmp);


            }
        }

    }
}