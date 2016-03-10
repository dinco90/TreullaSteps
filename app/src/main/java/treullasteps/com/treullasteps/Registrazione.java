package treullasteps.com.treullasteps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registrazione extends Activity {


    PreferenceHelperDemo prefs;
    Context context = this;

    static DataBaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registrazione);
        Button bnt = (Button) findViewById(R.id.buttonSave);

//Create the sessionsArrayList base in this activity, like this the database will be created only one time
        myDb = new DataBaseHelper(this);

        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefs = new PreferenceHelperDemo(context);

                final EditText edit_name = (EditText) findViewById(R.id.name);
                final EditText edit_age = (EditText) findViewById(R.id.age);
                final EditText edit_peso = (EditText) findViewById(R.id.peso);
                final EditText edit_altezza = (EditText) findViewById(R.id.altezza);
                final EditText edit_sesso = (EditText) findViewById(R.id.sex);
                final EditText edit_step = (EditText) findViewById(R.id.step);
                final EditText edit_mail = (EditText) findViewById(R.id.mail);

//Getting the inserted values
                String name = edit_name.getText().toString();
                String age = edit_age.getText().toString();
                String peso = edit_peso.getText().toString();
                String altezza = edit_altezza.getText().toString();
                String sesso = edit_sesso.getText().toString();
                String step = edit_step.getText().toString();
                String mail = edit_mail.getText().toString();

//Save them on shared preferences

                prefs.setKey("Name", name);
                prefs.setKey("Age", age);
                prefs.setKey("Email", mail);
                prefs.setKey("Peso", peso);
                prefs.setKey("Altezza", altezza);
                prefs.setKey("Sesso", sesso);
                prefs.setKey("Step", step);
//a boolean value for testing if the registration is done or not
                prefs.setBool("First", false);

                Toast.makeText(Registrazione.this, "Your registration is done " + prefs.getKey("Name"), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(Registrazione.this, Contapassi.class);
                startActivity(intent);

            }


        });

    }
}
