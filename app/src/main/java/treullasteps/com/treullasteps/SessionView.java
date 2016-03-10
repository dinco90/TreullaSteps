package treullasteps.com.treullasteps;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;*/

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class SessionView extends Activity implements View.OnClickListener {
    int position;

    DataBaseHelper myDb;
    String stringJSON = "DEFAULT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_view);

        Button buttonSendEmail = (Button) findViewById(R.id.buttonSendEmail);
        buttonSendEmail.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        position = extras.getInt(SessionList.POSITION);

        Toast.makeText(getApplicationContext(), Integer.toString(position), Toast.LENGTH_LONG).show();


        myDb = new DataBaseHelper(this);
        stringJSON = myDb.getJSONString(position);

        // anzichè mostrare tutta la stringa JSON, bisogna creare un oggetto JSON e mostrare i singoli valori
        //TextView viewJSONString = (TextView) findViewById(R.id.ViewJSONString);
        //viewJSONString.setText(stringJSON);

        // mostra tutti i dati e il grafico
        try {
            JSONObject trainingJSON = new JSONObject(stringJSON);
            viewTraining(trainingJSON);
            //viewChart(trainingJSON);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonSendEmail:
                // invia email
                try {
                    if (!sendEmail(stringJSON)) {
                        Toast.makeText(this, "Errore: email non inviata!", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void viewTraining(JSONObject trainingJSON) throws JSONException {
        TextView viewTrainingSteps = (TextView) findViewById(R.id.ViewTrainingSteps);
        TextView viewTrainingDistanceTotal = (TextView) findViewById(R.id.ViewTrainingDistanceTotal);
        TextView viewTrainingTimeTotal = (TextView) findViewById(R.id.ViewTrainingTimeTotal);
        TextView viewTrainingSpeedAverageTotal = (TextView) findViewById(R.id.ViewTrainingSpeedAverageTotal);

        String nameSession = "Nome: " + trainingJSON.getString("nameSession");
        String dateSession = "Data: " + trainingJSON.getString("dateSession");
        String timeSession = "Tempo Totale: " + trainingJSON.getString("timeSession");
        //String stepsSession = "Passi: " + trainingJSON.getString("stepsSession");

        viewTrainingSteps.setText(nameSession);
        viewTrainingDistanceTotal.setText(dateSession);
        viewTrainingTimeTotal.setText(timeSession);
       // viewTrainingSpeedAverageTotal.setText(stepsSession);
    }

    // mostra il grafico
    /*sono da importare le librerie
    public void viewChart(JSONObject trainingJSON) throws JSONException {
        int i = 0;

        LineChart lineChart = (LineChart) findViewById(R.id.chart);
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<String>();

        while (trainingJSON.has("timeArray" + i)) {
            // creating list of entry
            entries.add(new Entry(trainingJSON.getInt("timeArray" + i), i));
            //create labels
            labels.add(trainingJSON.getString("distanceArray" + i) + "metri");
            i++;
        }

        LineDataSet dataset = new LineDataSet(entries, "tempo impiegato");

        LineData data = new LineData(labels, dataset);
        lineChart.setData(data); // set the data and list of lables into chart

        lineChart.setDescription("Description");  // set the description
    }*/

    public boolean sendEmail(String trainingString) throws IOException {
        // nome del allenamento da prendere dal database
        String trainingName = "TreullaSteps.json";

        String baseDir = Environment.getExternalStorageDirectory().getAbsolutePath();
        File file = new File(baseDir, trainingName);
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
            writer.write(trainingString);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        // destinatario
        //i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        // oggetto
        i.putExtra(Intent.EXTRA_SUBJECT, "Allenamento TreullaSteps");
        // testo email
        i.putExtra(Intent.EXTRA_TEXT, "TreullaSteps\nIn allegato trovi l'allenamento effettuato.");
        // allegato
        Uri attachment = Uri.parse("file:///" + baseDir + "/" + trainingName);
        i.putExtra(Intent.EXTRA_STREAM, attachment);

        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
            // se cancello il file non viene allegato all'email
            //file.delete();
            return true;
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            //file.delete();
            return false;
        }
    }
}
