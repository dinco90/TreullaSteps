package treullasteps.com.treullasteps;

        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListAdapter;
        import android.widget.ListView;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.util.ArrayList;

public class SessionList extends Activity implements AdapterView.OnItemClickListener {

    ArrayList<Session> sessionsArrayList;
    ArrayList<String> sessionNameDataArrayList = new ArrayList<>();

    DataBaseHelper myDb;

    public static final String POSITION = "POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.session_list);

        ListView listView = (ListView) findViewById(R.id.list);

        myDb = new DataBaseHelper(this);
        sessionsArrayList = myDb.getJSONList();


        for (int i = 0; i < sessionsArrayList.size(); i++) {
            String tmpString = sessionsArrayList.get(i).getJSONString();
            try {
                JSONObject test = new JSONObject(tmpString);
                tmpString = test.getString("nameSession");
                //per vedere stringJSON e ora
                //tmpString = test.getString("nameSession") + " " + test.getString("dateSession");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            sessionNameDataArrayList.add(tmpString);
        }

        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sessionNameDataArrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SessionList.this, SessionView.class);
        intent.putExtra(POSITION, position);
        startActivity(intent);
    }
}