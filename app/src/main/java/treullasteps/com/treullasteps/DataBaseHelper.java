package treullasteps.com.treullasteps;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


//The class for creating the database
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "DataBase.db";
    public static final String TABLE_JSON_FILE = "JSONTable";
    private final Context mContext;

    //Constructor
    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating the table for saving the format of the json string with an id autoincrement
        db.execSQL("CREATE TABLE IF NOT EXISTS  " + TABLE_JSON_FILE + "  (ID INTEGER PRIMARY KEY AUTOINCREMENT, JASON_FILE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JSON_FILE);
        onCreate(db);
    }

    //Saving the format json file in the database
    //USE THIS METHOD TO INSERT A JSON_OBJECT (STRING)
    public boolean insertStringDB(String js) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("JASON_FILE", js);

        long result = db.insert(TABLE_JSON_FILE, null, contentValues);
        //making sure that the informations are saved
        if (result == -1)
            return false;
        else
            return true;
    }

    // restituisce la lista intera del database
    public ArrayList<Session> getJSONList() {
        Cursor res = getAllJSON();
        ArrayList<Session> sessions = new ArrayList();
        if (res.getCount() == 0) {
            //show message
            showMessage("Error", "Nothing found");
            return sessions;
        }
        while (res.moveToNext()) {
            Session session = new Session();
            session.setId(Integer.parseInt(res.getString(0)));
            session.setJSON(res.getString(1));
            sessions.add(session);
        }
        return sessions;
    }

    // restituisce la stringa JSON in base alla posizione
    public String getJSONString(int position) {
        Cursor res = getAllJSON();
        ArrayList<Session> sessions = new ArrayList();
        if (res.getCount() == 0) {
            //show message
            showMessage("Error", "Nothing found");
            return "prova";
        }
        while (res.moveToNext()) {
            Session session = new Session();
            session.setId(Integer.parseInt(res.getString(0)));
            session.setJSON(res.getString(1));
            sessions.add(session);
        }
        return sessions.get(position).getJSONString();
    }

    //Extracting the sessionsArrayList from the database
    public Cursor getAllJSON() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_JSON_FILE, null);
        return res;
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
