package treullasteps.com.treullasteps;

// classe che salva tutti i valori
public class Session {
    int id;
    String stringJSON;

    public String getJSONString() {
        return stringJSON;
    }

    public void setJSON(String stringJSON) {
        this.stringJSON = stringJSON;
    }

    public void setId(int id) {
        this.id = id;
    }


}
