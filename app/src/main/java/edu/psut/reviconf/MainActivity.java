package edu.psut.reviconf;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    JSONParser jsonParser = new JSONParser();
    private static String LOGIN_URL;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private EditText tv;
    private EditText tv2;
    TextView wrongID;
    int success = 0;
    ConnectionDetector cd;
    AlertDialogManager alert = new AlertDialogManager();
    GoogleCloudMessaging gcmObj;
    String regId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
/**/

        /**/
        LOGIN_URL = this.getString(R.string.server_name) + "login.php";
        tv = (EditText) findViewById(R.id.editText);
        tv2 = (EditText) findViewById(R.id.editText2);
        wrongID = (TextView) findViewById(R.id.wrongID);
        wrongID.setVisibility(View.INVISIBLE);
        Button btn = (Button) findViewById(R.id.button);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uname = tv.getText().toString();
                String Pass = tv2.getText().toString();
                cd = new ConnectionDetector(getApplicationContext());

                // Check if Internet present
                if (!cd.isConnectingToInternet()) {
                    // Internet Connection is not present
                    alert.showAlertDialog(MainActivity.this,
                            "Internet Connection Error",
                            "Please connect to working Internet connection", false);
                    // stop executing code by return
                    return;
                }else if (Uname.equals("") || Pass.equals("")){
                    Toast.makeText(getApplicationContext(), "You can't leave empty", Toast.LENGTH_LONG).show();
                }else {
                    new AttemptLogin().execute();
                }
            }
        });




    }

            @Override
            public boolean onCreateOptionsMenu(Menu menu) {

                getMenuInflater().inflate(R.menu.menu_main, menu);
                return true;
            }

            @Override
            public boolean onOptionsItemSelected(MenuItem item) {

                return super.onOptionsItemSelected(item);
            }

            class AttemptLogin extends AsyncTask<String, String, String> {

                ProgressDialog pdialog;

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    pdialog = new ProgressDialog(MainActivity.this);
                    pdialog.setMessage("Attempting Login .... ");
                    pdialog.setIndeterminate(false);
                    pdialog.setCancelable(true);
                    pdialog.show();
                }

                @Override
                protected String doInBackground(String... args) {

                    String username = tv.getText().toString();
                    String password = tv2.getText().toString();

                    try {
                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("username", username));
                        params.add(new BasicNameValuePair("password", password));


                        JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);
                        success = json.getInt(TAG_SUCCESS);

                        if (success == 1) {
                            String FirstName = json.getString("FirstName");
                            String userID = json.getString("UserID");
                            Log.d("Login Successful!", json.toString());
                            Intent i = new Intent(MainActivity.this, MainActivity2.class).putExtra("UserName", username);
                            i.putExtra("FirstName", FirstName);
                            i.putExtra("UserID", userID);
                            SaveSharedPreference.setUserName(getApplicationContext(), String.valueOf(userID));
                            startActivity(i);
                            return json.getString(TAG_MESSAGE);
                        } else {
                            return json.getString(TAG_MESSAGE);

                        }


                    } catch (JSONException e) {
                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(i);
                        e.printStackTrace();
                    }
                    return null;

                }

                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    pdialog.dismiss();
                    if (s != null && s.equals("Invalid Credentials!")) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                        wrongID.setText(s);
                        wrongID.setTextColor(Color.RED);
                        wrongID.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                    }

                }
            }


        }
