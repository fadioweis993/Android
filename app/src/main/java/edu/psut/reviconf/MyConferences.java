package edu.psut.reviconf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyConferences extends Activity {

    private static final String MyConferences_URL = "http://newfaceapps.site90.com/myconferences.php";
    private TextView ConferenceInfo;
    TextView confNames;
    LinearLayout linearLayout;
    String getConfName[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conferences);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        new getConferences().execute();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_conferences, menu);
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


    public class getConferences extends AsyncTask <String, String, Boolean>{
        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(MyConferences.this);
            pdialog.setMessage("Getting Conferences .... ");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(true);
            pdialog.show();
        }
        @Override
        protected Boolean doInBackground(String... params) {

            Intent i = getIntent();

            String UserID = i.getStringExtra("UserID");

            List<NameValuePair> MyConferences = new ArrayList<NameValuePair>();
            MyConferences.add(new BasicNameValuePair("UserID", UserID));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(MyConferences_URL, "POST", MyConferences);
            linearLayout = (LinearLayout) findViewById(R.id.calendar);
            try {
                JSONArray jsonArray = json.getJSONArray("confName");
                getConfName = new String[jsonArray.length()];
                for (int c=0;c<=jsonArray.length();c++){
                    JSONObject jsonObject = jsonArray.getJSONObject(c);
                    getConfName[c] = jsonObject.getString("confName");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            run();


            return null;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
            pdialog.dismiss();

        }
    }

    public void run() {
        MyConferences.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

                for (int i=0;i<getConfName.length;i++){
                  int num = i;
                    confNames = new TextView(MyConferences.this);
                    confNames.setText(++num + "- " + getConfName[i]);
                    linearLayout.addView(confNames);
                    confNames.setTextSize(20);
                }

            }
        });
    }




}

