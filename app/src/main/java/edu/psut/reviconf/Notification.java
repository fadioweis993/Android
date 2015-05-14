package edu.psut.reviconf;

import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.SaveCallback;

/**
 * Created by Oweis on 1/5/2015.
 */
public class Notification extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(getApplicationContext(), "Xx6QvGexra9Iumj5bTJoF7eshTPcvdgDpUpKzKUL", "Kf2h8zxdWD3r1SDU2Wf6Qv8WZxISFED33mwNMKW9");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("edu.psut.reviconf", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("edu.psut.reviconf", "failed to subscribe for push", e);
                }
            }
        });

    }
}
