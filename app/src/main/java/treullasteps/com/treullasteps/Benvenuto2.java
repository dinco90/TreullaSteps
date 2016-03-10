package treullasteps.com.treullasteps;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Benvenuto2 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.benvenuto2);

        Button bnt = (Button) findViewById(R.id.buttonAv);

        bnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Benvenuto2.this, Registrazione.class);
                startActivity(intent);
            }
        });
    }
}
