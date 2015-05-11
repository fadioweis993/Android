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
import android.widget.LinearLayout;
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

    private static  String CONFERENCE_INFO;
    private static  String JOIN_CONFERENCE;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static int VISIBILITY = 0;
    int Visible = 0;
    private Button joinBtn;
    private Button viewCommitte;
    TextView textToView;
    LinearLayout ConfeInfo;
    TextView tvName;
    TextView tvDate;
    TextView place;
    TextView introduction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conference_info);
        CONFERENCE_INFO = this.getString(R.string.server_name) + "conferenceInfo.php";
        JOIN_CONFERENCE = this.getString(R.string.server_name) + "joinConf.php";
        getActionBar().setDisplayHomeAsUpEnabled(true);
        ii = getIntent();
        UserID = ii.getStringExtra("UserID");
        joinBtn = (Button) findViewById(R.id.joinConf);

        if (ii.getIntExtra("Visibility",Visible) == 1){
            VISIBILITY = View.VISIBLE;
        }else{
            VISIBILITY = View.INVISIBLE;
        }

        joinBtn.setVisibility(VISIBILITY);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new joinConf().execute();
            }
        });
        new getConfInfo().execute();
        viewCommitte = (Button) findViewById(R.id.committe);

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
//
            i = getIntent ();
            confID = i.getStringExtra("confID");
            final List<NameValuePair> confInfo = new ArrayList<NameValuePair>();
            confInfo.add(new BasicNameValuePair("confID",confID));
            viewCommitte.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ConferenceInfo.this,ConfCommitte.class).putExtra("confID",confID);
                    startActivity(intent);
                }
            });
            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(CONFERENCE_INFO, "POST", confInfo);

            try {
                final  String Name = json.getString("confName");
                final  String Date = json.getString("confDate");
                final String SubmitEnd = json.getString("confSubmitEnd");
                final String ReviewEnd = json.getString("confReviewEnd");
                final String Introduction =  json.getString("introduction");
                final String Creator = json.getString("confCreator");
                final String Location = json.getString("Location");

                ConferenceInfo.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        textToView = new TextView(ConferenceInfo.this);
                        textToView.setText(

                                "\n" + "Submit Paper End-Date: " + SubmitEnd + "\n"
                                        + "\n" + "Review End-Date: " + ReviewEnd + "\n"
                                        + "\n" + "Creator " + Creator + "\n"
                                        + "\n" + "Introduction: " + Introduction
                        );
                        textToView.setTextSize(20);
                        //ConfeInfo = (LinearLayout) findViewById(R.id.confInfo);
                        //ConfeInfo.addView(textToView);
                        tvName = (TextView) findViewById(R.id.tv_title);
                        tvDate = (TextView) findViewById(R.id.tv_time);
                        place = (TextView) findViewById(R.id.tv_place_name);
                        introduction = (TextView) findViewById(R.id.tv_introduction);
                        tvName.setText(Name);
                        tvDate.setText(Date);
                        place.setText(Location);
                        introduction.setText(Introduction);
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
