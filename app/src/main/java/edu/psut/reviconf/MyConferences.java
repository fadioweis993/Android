package edu.psut.reviconf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyConferences extends ActionBarActivity {

    private static String MyConferences_URL;
    LinearLayout linearLayout;
    String getConfName[] = {};
    String confID[] = {};
    ArrayAdapter<String> adapter;
    ListView theLayout2;
    String UserID;
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_conferences);
        MyConferences_URL = this.getString(R.string.server_name) + "myconferences.php";
        //getActionBar().setDisplayHomeAsUpEnabled(true);
       getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MyConferences.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }else{
            new getConferences().execute();
        }

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

            UserID = i.getStringExtra("UserID");

            List<NameValuePair> MyConferences = new ArrayList<NameValuePair>();
            MyConferences.add(new BasicNameValuePair("UserID", UserID));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(MyConferences_URL, "POST", MyConferences);
            linearLayout = (LinearLayout) findViewById(R.id.calendar);
            try {
                JSONArray jsonArray = json.getJSONArray("confName");
                getConfName = new String[jsonArray.length()];
                confID = new String[jsonArray.length()];
                if (jsonArray.length() == 0){
                    MyConferences.this.runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            TextView tv = new TextView(getApplicationContext());
                            tv.setText("You don't have any conferences yet ...");
                            tv.setTextColor(getResources().getColor(R.color.conf_purple));
                            tv.setTextSize(20);
                            linearLayout.addView(tv);
                        }
                    });

                }
                for (int c=0;c<jsonArray.length();c++){
                    JSONObject jsonObject = jsonArray.getJSONObject(c);
                    getConfName[c] = jsonObject.getString("confName");
                    confID[c] = jsonObject.getString("ID");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
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
                theLayout2 = (ListView) findViewById(R.id.listView2);
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1,getConfName);
                theLayout2.setAdapter(adapter);
              theLayout2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                      String userChoose = String.valueOf(confID[position]);
                      Intent intent = new Intent(MyConferences.this,ConferenceInfo.class).putExtra("confID",userChoose);
                      intent.putExtra("UserID",UserID);
                      intent.putExtra("Visibility",0);
                      startActivity(intent);

                  }
              });


            }
        });
    }




}

