package com.example.alex.sharedlist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    Button sendButton;
    ProgressBar progressBar;
    String secretKey;
    boolean logedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ParseUser.getCurrentUser().saveInBackground();

        Intent intent = getIntent();
        String isLoged = intent.getStringExtra("logedin");
        if (isLoged != null){
            logedIn = true;
        }

        if(!logedIn) {
            Intent i = new Intent(this, LoginActivity.class);
            startActivity(i);
        }


        /*// object = table
        // feel free to enter your own test values here
        ParseObject parseObject = new ParseObject("Test");
        parseObject.put("pasta", "cannelloni");
        parseObject.put("price", 4.99);

        Log.i("StarterApplication", "Attempting to save...");
        parseObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("StarterApplication", "Success... congrats!");
                } else {
                    Log.i("StarterApplication", "Ooops... " + e);
                }
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
