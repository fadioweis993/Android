package edu.psut.reviconf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;


public class ConfCommitte extends Activity {
    GridView grid;
    String[] web = {} ;
    int[] imageId = {
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred,
            R.drawable.bg_blurred

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conf_committe);

        CustomGrid adapter = new CustomGrid(ConfCommitte.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
               Intent i = new Intent (ConfCommitte.this,MyProfile.class);
                startActivity(i);

            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_conf_committe, menu);
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
}
