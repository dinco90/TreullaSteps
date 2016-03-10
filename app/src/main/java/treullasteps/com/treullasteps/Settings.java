package treullasteps.com.treullasteps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class Settings extends Activity {

    PreferenceHelperDemo prefs;
    Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        Button bnt = (Button) findViewById(R.id.btnSave);

        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefs = new PreferenceHelperDemo(context);



                final EditText rythme = (EditText) findViewById(R.id.rythme);
                final EditText distance = (EditText) findViewById(R.id.distance);

                String rhytmeStr = rythme.getText().toString();
                String distanceStr = distance.getText().toString();

                prefs.setKey("Rythme", rhytmeStr);
                prefs.setKey("Distance", distanceStr);

                Intent intent = new Intent(Settings.this, Contapassi.class);
                startActivity(intent);

            }

        });
    }
}