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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Oweis on 15/3/2015.
 */
public class conferences extends Activity {
    private MenuItem item;
    private static final String LOGIN_URL = "http://192.168.1.2/webservice/conferences.php";
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private static final String TAG_TITLE = "title";
    protected TextView tv;
    protected EditText ConfSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conferences);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        Button bttn;
        bttn = (Button) findViewById(R.id.testBtn);
        bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new getConf().execute();
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

    public class getConf extends AsyncTask<String, String, String> {

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

            try {
                ConfSearch = (EditText) findViewById(R.id.ConfToSearch);

                String conf = ConfSearch.getText().toString();
                List<NameValuePair> confs = new ArrayList<NameValuePair>();
                confs.add(new BasicNameValuePair("confName",conf));

                JSONParser jsonParser = new JSONParser();
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", confs);
                String result = json.getString("introduction").toString();
                Log.d("JSON_I_GOT", result);
                return result;


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pdialog.dismiss();
            tv = (TextView) findViewById(R.id.ConfToView);
            if (s.equals(null)){
                Toast.makeText(getApplicationContext(),"Conference not found",Toast.LENGTH_LONG).show();
            }
            tv.setText(s);

        }
    }
}