package edu.psut.reviconf;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Oweis on 15/3/2015.
 */
public class conferences extends Activity {
    private MenuItem item;
    private static final String LOGIN_URL = "http://newfaceapps.site90.com/conferences.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_TITLE = "title";
    protected EditText ConfSearch;
    ListView theLayout;
    String conferencesToJoin[];
   private int conferenceID[];
    TextView textToView;
    JSONArray jsonArray;
    private String UserIDD;
    Intent i;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        Button bttn;
        i = getIntent();
        UserIDD = i.getStringExtra("UserID");
        bttn = (Button) findViewById(R.id.testBtn);
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new searchConf().execute();
            }
        });


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
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        switch (id) {
//            case R.id.action_call:
//                Intent dialer = new Intent(Intent.ACTION_CALL);
//                startActivity(dialer);
//                return true;
            case R.id.action_speech:
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                startActivityForResult(intent, 1234);

                return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EditText et = (EditText) findViewById(R.id.ConfToSearch);
        if (requestCode == 1234 && resultCode == RESULT_OK) {
            String voice_text = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);
            Toast.makeText(getApplicationContext(), voice_text, Toast.LENGTH_LONG).show();
            et.setText(voice_text);

        }
    }

    public class searchConf extends AsyncTask<String, String, String> {

        ProgressDialog pdialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdialog = new ProgressDialog(conferences.this);
            pdialog.setMessage("Processing your request .... ");
            pdialog.setIndeterminate(false);
            pdialog.setCancelable(true);
            pdialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            ConfSearch = (EditText) findViewById(R.id.ConfToSearch);
            //confSearchResult = (TextView) findViewById(R.id.confResult);
            String conf = ConfSearch.getText().toString();
            if (conf.equals("")){
                //Toast.makeText(conferences.this,"You can't leave empty",Toast.LENGTH_LONG).show();
                Log.d("empty","empty string");
            }else {


            List<NameValuePair> confs = new ArrayList<NameValuePair>();
            confs.add(new BasicNameValuePair("confName",conf));

            JSONParser jsonParser = new JSONParser();
            JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", confs);

            try {

                jsonArray = json.getJSONArray("conferencesResult");

                conferencesToJoin = new String[jsonArray.length()];
                conferenceID = new int[jsonArray.length()];
                Log.d("ArraySize",String.valueOf(jsonArray.length()));
                for (int c = 0; c <= jsonArray.length(); c++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(c);
                    theLayout = (ListView) findViewById(R.id.listView);
                    conferencesToJoin[c] = jsonObject.getString("confName");
                    conferenceID[c] = jsonObject.getInt("confID");
                    textToView = new TextView(conferences.this);
                    textToView.setText(conferencesToJoin[c]);
                    textToView.setTextSize(15);
                    run();

                }
            }catch (JSONException ee){
                ee.printStackTrace();
            }


        } return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdialog.dismiss();




        }
    }

    public void run() {
        conferences.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                theLayout = (ListView) findViewById(R.id.listView);
                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple_list_item_1,conferencesToJoin);
                theLayout.setAdapter(adapter);
                theLayout.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String userChoose = String.valueOf(conferenceID[position]);
                        Intent intent = new Intent(conferences.this,ConferenceInfo.class).putExtra("confID",userChoose);
                        intent.putExtra("UserID",UserIDD);
                        intent.putExtra("Visibility",1);
                        startActivity(intent);

                    }
                });

            }
        });
    }
}

