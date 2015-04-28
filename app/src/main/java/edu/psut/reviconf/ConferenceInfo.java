package edu.psut.reviconf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ConferenceInfo extends Activity {
    Intent ii;
    Intent i;
    String confID;
    String UserID;
    private TextView confName;
    private TextView confDate;
    private TextView confSubmitEnd;
    private TextView confReviewEnd;
    private TextView introduction;
    private TextView confCreator;
    private static final String CONFERENCE_INFO = "http://newfaceapps.site90.com/conferenceInfo.php";
    private static final String JOIN_CONFERENCE = "http://newfaceapps.site90.com/joinConf.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private Button joinBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_info);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ii = getIntent();
        UserID = ii.getStringExtra("UserID");
        joinBtn = (Button) findViewById(R.id.joinConf);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new joinConf().execute();
            }
        });
        new getConfInfo().execute();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conference_info, menu);
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

    private class getConfInfo extends AsyncTask <String, String, String>{


        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(ConferenceInfo.this);
            pdialog.setMessage("Processing your request .... ");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(true);
            pdialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            confName = (TextView) findViewById(R.id.confName);
            confDate = (TextView) findViewById(R.id.confDate);
            confSubmitEnd = (TextView) findViewById(R.id.confSubmitEnd);
            confReviewEnd = (TextView) findViewById(R.id.confReviewEnd);
            introduction = (TextView) findViewById(R.id.introduction);
            confCreator = (TextView) findViewById(R.id.confCreator);
            i = getIntent ();
            confID = i.getStringExtra("confID");
            List<NameValuePair> confInfo = new ArrayList<NameValuePair>();
            confInfo.add(new BasicNameValuePair("confID",confID));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(CONFERENCE_INFO, "POST", confInfo);

            try {
              final  String Name = json.getString("confName");
                final  String Date = json.getString("confDate");
                final String SubmitEnd = json.getString("confSubmitEnd");
                final String ReviewEnd = json.getString("confReviewEnd");
                final String Introduction =  json.getString("introduction");
                final String Creator = json.getString("confCreator");

                ConferenceInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        confName.setText(Name);
                        confDate.setText(Date);
                        confSubmitEnd.setText(SubmitEnd);
                        confReviewEnd.setText(ReviewEnd);
                        introduction.setText(Introduction);
                        confCreator.setText(Creator);
                    }
                });


            } catch (JSONException e) {
                e.printStackTrace();
            }




            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdialog.dismiss();
        }
    }

    private class joinConf extends AsyncTask <String, String, String>{
        ProgressDialog pdialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(ConferenceInfo.this);
            pdialog.setMessage("Joining your Conference .... ");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(true);
            pdialog.show();
        }
        @Override
        protected String doInBackground(String... params) {

            List<NameValuePair> joinConf = new ArrayList<NameValuePair>();
            joinConf.add(new BasicNameValuePair("confID",confID));
            joinConf.add(new BasicNameValuePair("UserID",UserID));
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(JOIN_CONFERENCE, "POST", joinConf);

            int success = 0;
            try {
                success = json.getInt(TAG_SUCCESS);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (success == 1) {
                Log.d("Joined Successful!", json.toString());
                try {
                   return json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    return json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                try {
                    Log.d("Login Failure!", json.getString(TAG_MESSAGE));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    return json.getString(TAG_MESSAGE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(),s ,Toast.LENGTH_LONG).show();
        }
    }
}
