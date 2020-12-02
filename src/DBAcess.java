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
        String pass = "";
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
        String user = "";
        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                user += result.getString(i + 1) + " ";
            }
        }

        con.close();

        return user;
    }
}
