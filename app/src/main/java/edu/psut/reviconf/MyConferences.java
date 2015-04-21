package edu.psut.reviconf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
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

    private static final String MyConferences_URL = "http://192.168.1.2/webservice/MyConferences.php";
    private TextView ConferenceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conferences);

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

            try {
                JSONArray jsonArray = json.getJSONArray("confName");
                for (int c=0;c<=jsonArray.length();c++){
                    JSONObject jsonObject = jsonArray.getJSONObject(c);
                    LinearLayout linearLayout = (LinearLayout) findViewById(R.id.calendar);
                    TextView confNames = new TextView(MyConferences.this);
                    confNames.setText(jsonObject.getString("confName"));
                    linearLayout.addView(confNames);
                }

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

