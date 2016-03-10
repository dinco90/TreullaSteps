package treullasteps.com.treullasteps;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class Contapassi extends Activity implements View.OnClickListener {

    Button buttonStartTraining;
    Button buttonPauseTraining;
    Button buttonStopTraining;

    private TextView tvLatitudine;
    private TextView tvLongitudine;
    private TextView tvSpeed;
    private TextView tvDistance;
    private TextView tvMidleSpeed;
    private TextView tvMidleSpeedX;
    private TextView tvMidleSpeedStep;
    private TextView tvMidleSpeedStepXM;
    private LocationManager locationManager;
    private LocationListener locationListener;

    float passo = (float) 0.5;
    float xMetri = 10;
    float distanceUpdate = 10; //La sensibilità del GPS nel migliore dei casi è 10m, una sensibilità molto  bassa non ha senso.
    long firstTime = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final ArrayList timeArray = new ArrayList();

        SharedPreferences prefs = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("DISTANZA_X", 0);
        editor.putFloat("TOTAL_DISTANCE", 0);
        editor.putBoolean("FIRST", true);
        editor.putBoolean("STATO", false);


        //salvo l'istante d'avvio
        firstTime = prefs.getLong("FIRST_TIME", 0);


        editor.commit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.contapassi);
        tvLatitudine = (TextView) this.findViewById(R.id.tvLatitudine);
        tvLongitudine = (TextView) this.findViewById(R.id.tvLongitudine);
        tvSpeed = (TextView) this.findViewById(R.id.tvSpeed);
        tvDistance = (TextView) this.findViewById(R.id.TotalDistance);
        tvMidleSpeed = (TextView) this.findViewById(R.id.midleSpeed);
        tvMidleSpeedStep = (TextView) this.findViewById(R.id.passoMedioS);
        tvMidleSpeedX = (TextView) this.findViewById(R.id.speedMedioXM);
        tvMidleSpeedStepXM = (TextView) this.findViewById(R.id.stepMedioXM);


        buttonStartTraining = (Button) findViewById(R.id.buttonStartTraining);
        buttonPauseTraining = (Button) findViewById(R.id.buttonPauseTraining);
        buttonStopTraining = (Button) findViewById(R.id.buttonStopTraining);

        buttonStartTraining.setOnClickListener(this);
        buttonPauseTraining.setOnClickListener(this);
        buttonStopTraining.setOnClickListener(this);

        buttonPauseTraining.setVisibility(View.INVISIBLE);
        buttonStopTraining.setVisibility(View.INVISIBLE);


        // Otteniamo il riferimento al LocationManager
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
        // Verifica se il GPS e' abilitato altrimenti avvisa l'utente
        if (!locationManager.isProviderEnabled("gps")) {
            Toast.makeText(this,
                    "GPS e' attualmente disabilitato. E' possibile abilitarlo dal menu impostazioni.",
                    Toast.LENGTH_LONG).show();
        }

        // Implementazione dell'interfaccia LocationListener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences prefs = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
                boolean stato = prefs.getBoolean("STATO", false);
                if (stato) {
                    Calendar adesso = Calendar.getInstance();
                    long lastlTime = (adesso.getTimeInMillis()) / 1000;
                    long time = (firstTime - lastlTime) / 1000;
                    //salvo i tempi nelle shared preference
                    timeArray.add(time);
                    int dim = timeArray.size();
                    prefs.edit().putLong("TEMPI" + Integer.toString(0)//nella prima posizione salvo la dimensione dell'array
                            , dim);
                    prefs.edit().putLong("TEMPI" + Integer.toString(dim)
                            , time);

                    float x = location.getSpeed();//calcolo la velocità
                    String a = String.valueOf(x);
                    tvLatitudine.setText(format(location.getLatitude()));
                    tvLongitudine.setText(format(location.getLongitude()));
                    tvSpeed.setText(a);
                    tvDistance.setText(distanceC(distanceUpdate));
                    float totalDistance = (prefs.getFloat("TOTAL_DISTANCE", 0));
                    //velocità media totale
                    float midleSpeedV = midleSpeed(totalDistance);
                    String midleSpeedMS = String.valueOf(midleSpeedV);
                    tvMidleSpeed.setText(midleSpeedMS);
                    //passo medio totale
                    float passoMedio = midleSpeedStep(midleSpeedV, passo);
                    String passoMedioS = String.valueOf(passoMedio);
                    tvMidleSpeedStep.setText(passoMedioS);
                    //velocità media su X metri
                    float speedXM = midleXSpeed((int) distanceUpdate, (int) xMetri);
                    String speedMedioXM = String.valueOf(speedXM);
                    tvMidleSpeedX.setText(speedMedioXM);
                    //passo medio XM
                    float XMpassoMedio = midleSpeedStep(speedXM, passo);
                    String XMpassoMedioS = String.valueOf(XMpassoMedio);
                    tvMidleSpeedStepXM.setText(XMpassoMedioS);
                }


            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(Contapassi.this, "onProviderDisabled " + provider,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(Contapassi.this, "onProviderEnabled " + provider,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                Toast.makeText(Contapassi.this,
                        "onStatusChanged " + provider + " status: " + status,
                        Toast.LENGTH_SHORT).show();
            }
        };
        // Registriamo il LocationListener al provider GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, distanceUpdate, locationListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Rimozione del listener
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        onStop();
    }

    public void onClick(View v) {
        SharedPreferences prefs = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("STATO", false);
        switch (v.getId()) {
            // inizia allenamento (al momento non fa niente)
            case R.id.buttonStartTraining:
                Toast.makeText(Contapassi.this, "Start", Toast.LENGTH_LONG).show();
                //salvo l'istante d'avvio
                Calendar adesso = Calendar.getInstance();
                long firstTime0 = (adesso.getTimeInMillis());
                editor.putLong("FIRST_TIME", firstTime0);
                editor.putLong("FIRST_XTIME", (adesso.getTimeInMillis()));
                buttonPauseTraining.setVisibility(View.VISIBLE);
                buttonStopTraining.setVisibility(View.INVISIBLE);
                editor.putBoolean("STATO", true);
                editor.commit();
                break;
            // mette in pausa l'allenamento (NON FUNZIONANTE)
            case R.id.buttonPauseTraining:
                Toast.makeText(Contapassi.this, "Pause", Toast.LENGTH_LONG).show();
                buttonPauseTraining.setVisibility(View.INVISIBLE);
                buttonStopTraining.setVisibility(View.VISIBLE);
                editor.putBoolean("STATO", false);
                editor.commit();
                break;
            // ferma l'allenamento
            case R.id.buttonStopTraining:
                Toast.makeText(Contapassi.this, "Stop", Toast.LENGTH_LONG).show();
                Calendar adessoFine = Calendar.getInstance();
                long stopTime = (adessoFine.getTimeInMillis());
                editor.putLong("STOP_TIME", stopTime);
                long finalTime0 = (stopTime - (prefs.getLong("FIRST_TIME", 0)));
                editor.putLong("FINAL_TIME0", finalTime0);
                editor.commit();
                Intent i = new Intent(Contapassi.this, SaveSession.class);
                startActivityForResult(i, 0);
                break;
        }
    }

    public static String format(double value) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0000000");
        return decimalFormat.format(value);
    }


    public String distanceC(float dist) {
        SharedPreferences prefs = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        float distTmp = dist;
        float distTmp2 = (prefs.getFloat("TOTAL_DISTANCE", 0));
        float distanzaTotale = distTmp + distTmp2;//devo inizzializarla all'inizio dell'activity
        editor.putFloat("TOTAL_DISTANCE", distanzaTotale);
        editor.commit();
        String distanzaFinale = String.valueOf(distanzaTotale);
        return distanzaFinale;
    }

    public float midleSpeed(float finalDist) {
        SharedPreferences preferes = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferes.edit();
        Calendar adessox = Calendar.getInstance();
        long lastlTime = (adessox.getTimeInMillis()) / 1000; //sessionsArrayList corrente in secondi
        long firstTime = (preferes.getLong("FIRST_TIME", 0)) / 1000;//sessionsArrayList d'avvio in secondi
        long timeS = (lastlTime - firstTime);
        long longDist = (long) finalDist;
        float midleSpeedMS = (float) (longDist / timeS);
        editor.putFloat("MIDLE_SPEED_MS", midleSpeedMS);
        editor.commit();
        return midleSpeedMS;

    }


    public float midleSpeedStep(float midleSpeed, float step) {
        SharedPreferences preferes = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferes.edit();
        float midleSpeedSteps = midleSpeed / step;
        editor.putFloat("MIDLE_SPEED_STEPS", midleSpeedSteps);
        editor.commit();
        return midleSpeedSteps;
    }

    public float midleXSpeed(int dist, int x) { //dista = distanza per ogni aggiornamento, x = Xmetri
        SharedPreferences prefs = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int distanza = (prefs.getInt("DISTANZA_X", 0)) + dist;//va inizializzato all'inizio
        if (distanza == x) {

            Calendar adessox = Calendar.getInstance();
            long lastlTime = (adessox.getTimeInMillis()) / 1000; //sessionsArrayList corrente in secondi
            long firstXTime = (prefs.getLong("FIRST_XTIME", 0)) / 1000;//sessionsArrayList d'avvio in secondi
            long timeS = (lastlTime - firstXTime);
            long speedXM = (long) distanza / timeS;
            float speedXMF = (float) speedXM;
            editor.putInt("DISTANZA_X", 0);
            editor.putFloat("SPEED_XMF", speedXMF);
            editor.putLong("FIRST_XTIME", (adessox.getTimeInMillis()));
            editor.commit();
            return speedXMF;
        }
        float speedXMF = (prefs.getFloat("SPEED_XMF", 0));
        editor.putInt("DISTANZA_X", distanza);
        editor.commit();
        return speedXMF;

    }


    public float midleSpeedStepXM(float midleSpeedXM, float step) {
        SharedPreferences preferes = getSharedPreferences("MY_PREFERENCES", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferes.edit();
        float midleSpeedStepsXM = midleSpeedXM / step;
        editor.putFloat("MIDLE_SPEED_STEPS_XM", midleSpeedStepsXM);
        editor.commit();
        return midleSpeedStepsXM;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
                return true;
            case R.id.sessions:
                Intent intent2 = new Intent(this, SessionList.class);
                startActivity(intent2);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }


}