package treullasteps.com.treullasteps;


import android.os.Bundle;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;


import java.lang.Runnable;

import android.os.Handler;

public class Benvenuto1 extends Activity implements Runnable {

    //variable for testing if the registration is already done or no
    boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.benvenuto1);

//A timer for going to the next activity automatically
        Handler mHandler = new Handler();
        mHandler.postDelayed(this, 2500);

//Calling the shared preferences for testing if the registration is done
        Context context = this;
        PreferenceHelperDemo prefs = new PreferenceHelperDemo(context);
        firstTime = prefs.getBool("First");
    }

    @Override
    public void run() {
        if (firstTime) {

            Intent intent = new Intent(this, Benvenuto2.class);
            startActivity(intent);
        } else {
//se la prima registrazione è stata effetuata passo direttamente al contappassi
            Intent intent = new Intent(this, Contapassi.class);
            startActivity(intent);
        }
    }
}
