import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

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

        result = stmt.executeQuery("select FIlM.tytul, FILM.rezyser, FILM.gatunek, FILM.czas_trwania, SEANS.dzien, SEANS.godzina_rozpoczecia, SEANS.id_sali, SALA.liczba_miejsc, SEANS.id_seansu " +
                "from FILM " +
                "inner join SEANS " +
                "on FILM.id_filmu=SEANS.id_filmu " +
                "inner join SALA " +
                "on SEANS.id_sali=SALA.id_sali ");
        meta = result.getMetaData();
        String[][] data = new String[40][meta.getColumnCount()];
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

    public String[][] getClientTickets(String userMail) throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlite:baza_kino.db3";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();



        result = stmt.executeQuery("select FIlM.tytul, FILM.rezyser, FILM.gatunek, FILM.czas_trwania, SEANS.dzien, SEANS.godzina_rozpoczecia, SEANS.id_sali, BILET.miejsce, BILET.id_biletu " +
                "from FILM " +
                "inner join SEANS " +
                "on FILM.id_filmu=SEANS.id_filmu " +
                "inner join ZAMOWIENIA " +
                "on SEANS.id_seansu=ZAMOWIENIA.id_zamowienia " +
                "inner join BILET " +
                "on ZAMOWIENIA.id_zamowienia=BILET.id_zamowienia " +
                "where ZAMOWIENIA.email_klienta = '" + userMail + "'");

        meta = result.getMetaData();

        LocalDate currentTime = LocalDate.now();
        java.util.Date nowDate = java.util.Date.from(currentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String[][] data = new String[100][meta.getColumnCount()+1];
        int j = 0, i;
        while (result.next()) {
            i = 0;
            while(i < meta.getColumnCount()-1) {
                data[j][i] = result.getString(i+1);
                i++;
            }
            data[j][i] = (Date.valueOf(result.getDate("dzien").toLocalDate()).after(nowDate)) ? "aktywny" : "nieaktywny";
            System.out.println(nowDate);
            System.out.println(Date.valueOf(result.getDate("dzien").toLocalDate()));
            j++;
        }
        con.close();
        return data;
    }

    public String[] getSeats() throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlite:baza_kino.db3";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select BILET.miejsce, SALA.liczba_miejsc, SEANS.id_seansu " +
                "from BILET " +
                "inner join ZAMOWIENIA on BILET.id_zamowienia=ZAMOWIENIA.id_zamowienia " +
                "inner join SEANS on ZAMOWIENIA.id_seansu=SEANS.id_seansu " +
                "inner join SALA on SEANS.id_sali=SALA.id_sali " +
                "where SEANS.id_seansu = 2");
        meta = result.getMetaData();
        String[][] data = new String[40][meta.getColumnCount()];
        ArrayList<String> seatTable = new ArrayList<>();
        for (int w = 0; w < Integer.parseInt(result.getString("liczba_miejsc")); w++)
            seatTable.add(w, String.valueOf(w+1));

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                for (int g = 0; g < seatTable.size(); g++){
                    if(seatTable.get(g).equals(result.getString("miejsce")))
                        seatTable.remove(g);
                }
            }
        }
        String[] seatsRes = new String[seatTable.size()];
        for (int f = 0; f < seatsRes.length; f++)
            seatsRes[f] = seatTable.get(f);
        con.close();
        return seatsRes;
    }

    public void newOrder(String userMail, int seanceID, int selectedSeat) throws ClassNotFoundException, SQLException {
        String url = "jdbc:sqlite:baza_kino.db3";
        Class.forName("org.sqlite.JDBC");
        Connection con = DriverManager.getConnection(url);
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select ZAMOWIENIA.id_zamowienia " +
                "from ZAMOWIENIA");
        meta = result.getMetaData();
        int orderId = 0;
        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                if(Integer.parseInt(result.getString("id_zamowienia")) > orderId)
                    orderId = Integer.parseInt(result.getString("id_zamowienia")) + 1;
            }
        }

        result = stmt.executeQuery("select BILET.id_biletu " +
                "from BILET");
        meta = result.getMetaData();
        int ticketId = 0;
        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                if(Integer.parseInt(result.getString("id_biletu")) > ticketId)
                    ticketId = Integer.parseInt(result.getString("id_biletu")) + 1;
            }
        }

        stmt.executeUpdate("insert into ZAMOWIENIA " +
                "values (" + orderId + ", '" + userMail + "', " + seanceID + ")");
        stmt.executeUpdate("insert into BILET " +
                "values (" + ticketId + ", '" + orderId + "', " + selectedSeat + ")");

        con.close();
    }
}
