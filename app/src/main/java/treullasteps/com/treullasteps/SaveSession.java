package treullasteps.com.treullasteps;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;

public class SaveSession extends Activity implements View.OnClickListener {
    // il database
    DataBaseHelper myDb;

    // variabili che contengono i valori (in attesa di essere sostituiti dal gps)
    String name;
    String date;
    int time; // secondi
    float distance; // metri
    float speedTotal; // m/s
    float speedXMeters; //speed ultimi x metri
    float rythmTotal; // passi al minuto
    float rythmXMeters;//passi ultimi x metri
    ArrayList timeArray = new ArrayList();
    ArrayList distanceArray = new ArrayList();

    String toSafeJSONString;
    EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_session);

        Button buttonSaveSession = (Button) findViewById(R.id.buttonSaveSession);
        Button buttonCancelSession = (Button) findViewById(R.id.buttonCancelSession);
        nameEditText = (EditText) findViewById(R.id.InsertNameEditText);

        buttonSaveSession.setOnClickListener(this);
        buttonCancelSession.setOnClickListener(this);

        // se arrivo da un file esterno, salvo la stringa
        if (getCallingActivity() == null) {
            Intent intent = getIntent();
            toSafeJSONString = getDataFile(intent.getData().getPath());
            JSONObject trainingJSON;

            try {
                trainingJSON = new JSONObject(toSafeJSONString);
                name = trainingJSON.getString("nameSession");
                nameEditText.setHint(name + " - non è possibile modificare il nome!");
                nameEditText.setFocusable(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            // salva la sessione
            case R.id.buttonSaveSession:
                // se arrivo dal contapassi, salvo i valori calcolati
                if (getCallingActivity() != null) {
                    getData();
                    name = nameEditText.getText().toString();

                    try {
                        toSafeJSONString = createJSONString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                boolean isInserted = false;

                // il controllo avviene sul nome assegnato all'allenamento
                if (name.equals("")) {
                    Toast.makeText(SaveSession.this, "Insert a session name!", Toast.LENGTH_LONG).show();
                } else {
                    myDb = new DataBaseHelper(this);
                    isInserted = false;
                    // insertStringDB inserisce una stringa nel database
                    isInserted = myDb.insertStringDB(toSafeJSONString);

                    if (isInserted) {
                        Toast.makeText(SaveSession.this, "Session saved", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(SaveSession.this, "OOOPS, session NOT saved", Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(SaveSession.this, SessionList.class);
                    startActivity(intent);
                }
                break;
            // ignora la sessione
            case R.id.buttonCancelSession:
                Toast.makeText(SaveSession.this, "Session ignorata!", Toast.LENGTH_LONG).show();
                Intent intent2 = new Intent(SaveSession.this, Contapassi.class);
                startActivity(intent2);
                break;
        }
    }

    // funzione che inizializza i dati
    public void getData() {
        Calendar c = Calendar.getInstance();

        SharedPreferences prefs = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        distance = (prefs.getFloat("TOTAL_DISTANCE", 0));
        speedTotal = (prefs.getFloat("MIDLE_SPEED_MS", 0));
        rythmTotal = (prefs.getFloat("MIDLE_SPEED_STEPS", 0));
        speedXMeters = (prefs.getFloat("SPEED_XMF", 0));
        rythmXMeters = (prefs.getFloat("MIDLE_SPEED_STEPS_XM", 0));
        date = (c.getTime().toString());
        time = (int) ((prefs.getLong("FINAL_TIME0", 0) / 1000));
        //float  steps = 10;
        /*
        distance = 100;
        speedTotal = 10;
        speedXMeters = 10;
        rythmTotal = 300;
        rythmXMeters = 300;*/
        for (int i = 0; i < prefs.getInt("TIME0", 0); i++) {
            timeArray.add(prefs.getInt("TEMPI" + (i+1), 0));
            distanceArray.add(10);
        }
    }

    public String getDataFile(String path) {
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
        return text.toString();
    }

    // funzione che crea la stringa JSON con i dati
    public String createJSONString() throws JSONException {
        JSONObject trainingJSON = new JSONObject();

        trainingJSON.put("nameSession", name);
        trainingJSON.put("dateSession", date);
        trainingJSON.put("timeSession", time);
        //trainingJSON.put("stepsSession", steps);
        trainingJSON.put("distanceSession", distance);
        trainingJSON.put("speedTotalSession", speedTotal);
        trainingJSON.put("speedXMetersSession", speedXMeters);
        trainingJSON.put("rythmTotalSession", rythmTotal);
        trainingJSON.put("rythmXMetersSession", rythmXMeters);
        for (int i = 1; i <= timeArray.size(); i++) {
            trainingJSON.put("timeArray" + i, timeArray.get(i));
            trainingJSON.put("distanceArray" + i, distanceArray.get(i));
        }


        return trainingJSON.toString();
    }
}
