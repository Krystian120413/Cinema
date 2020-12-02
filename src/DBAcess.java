import java.sql.*;

public class DBAcess {
    Statement stmt;
    ResultSet result;
    ResultSetMetaData meta;

    public boolean login(Object email, Object password) throws SQLException, ClassNotFoundException {
        String url = "jdbc:sqlite:baza_kino.db3";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();



        String query = "select haslo from KLIENT where email = '" + email + "'";
        result = stmt.executeQuery(query);
        String pass = null;
        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                pass = result.getString(i + 1);
            }
        }
        con.close();
        return password.equals(pass);
    }


    public String client(String email) throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlite:baza_kino.db3";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();

        String query = "select imie, nazwisko from KLIENT where email = '" + email + "'";
        result = stmt.executeQuery(query);
        StringBuilder user = new StringBuilder();
        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                user.append(result.getString(i + 1)).append(" ");
            }
        }
        con.close();
        return user.toString();
    }


    public String[][] getSeances() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlite:baza_kino.db3";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select FIlM.tytul, FILM.rezyser, FILM.gatunek, FILM.czas_trwania, SEANS.dzien, SEANS.godzina_rozpoczecia, SEANS.id_sali, SALA.liczba_miejsc " +
                "from FILM " +
                "inner join SEANS " +
                "on FILM.id_filmu=SEANS.id_filmu " +
                "inner join SALA " +
                "on SEANS.id_sali=SALA.id_sali ");
        meta = result.getMetaData();
        String[][] data = new String[100][meta.getColumnCount()];
        int j = 0;
        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                data[j][i] = result.getString(i+1);
            }
            j++;
        }
        con.close();
        return data;
    }
}
