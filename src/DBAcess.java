import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;

public class DBAcess {
    Statement stmt, stmt1, stmt2, stmt3, stmt4;
    ResultSet result, result1, result2;
    ResultSetMetaData meta;
    final String url = "jdbc:sqlite:baza_kino.db3";
    static Connection con;

    static DBAcess dbc;

    static {
        try{
            dbc = new DBAcess();
        } catch (ClassNotFoundException | SQLException e){
            e.printStackTrace();
        }
    }

    private DBAcess() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.con = DriverManager.getConnection(url);
    }

    public static DBAcess getInstance(){
        return dbc;
    }

    public boolean login(Object email, Object password) throws SQLException {

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
        return password.equals(pass);
    }


    public String client(String email) throws SQLException {
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
        return user.toString();
    }

    public String[] getClientData(String email) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();

        String query = "select imie, nazwisko, email, haslo from KLIENT where email = '" + email + "'";
        result = stmt.executeQuery(query);
        String [] user = new String[4];
        meta = result.getMetaData();

        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                user[i] = result.getString(i + 1);
            }
        }
        return user;
    }

    public void updateUserData(String name, String surname, String email) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();

        String query = "update KLIENT  set imie = '" + name + "', nazwisko = '" + surname + "' where email = '"  + email + "'";
        stmt.executeUpdate(query);
    }

    public void addUser(String name, String surname, String email, String password) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();

        String query = "insert into KLIENT  values ('" + email + "', '" + password + "', '" + name + "', '" + surname + "')";
        stmt.executeUpdate(query);
    }

    public void deleteUser(String email, String password) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();

        String query = "delete from KLIENT where email = '" + email + "' and haslo = '" + password + "' ";
        stmt.executeUpdate(query);
    }

    public String[][] getSeances() throws SQLException {
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select FIlM.tytul, FILM.rezyser, FILM.gatunek, FILM.czas_trwania, SEANS.dzien, SEANS.godzina_rozpoczecia, SEANS.id_sali, SALA.liczba_miejsc, SEANS.id_seansu " +
                "from FILM " +
                "inner join SEANS " +
                "on FILM.id_filmu=SEANS.id_filmu " +
                "inner join SALA " +
                "on SEANS.id_sali=SALA.id_sali ");
        meta = result.getMetaData();

        LocalDate currentTime = LocalDate.now();
        java.util.Date nowDate = java.util.Date.from(currentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());

        String[][] data = new String[20][meta.getColumnCount()];
        int j = 0;
        while (result.next()) {
            LocalDate date = LocalDate.parse(result.getString("dzien"));
            if(Date.valueOf(date).after(nowDate)) {
                for (int i = 0; i < meta.getColumnCount(); i++) {
                    data[j][i] = result.getString(i + 1);
                }
                j++;
            }
        }
        return data;
    }
    public boolean addSeance(String title, String director, String genre, String length, String day, String start, String hall) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();
        this.stmt1 = con.createStatement();
        this.stmt2 = con.createStatement();
        this.stmt3 = con.createStatement();
        this.stmt4 = con.createStatement();

        result = stmt.executeQuery("SELECT SEANS.id_seansu " +
                "FROM SEANS");
        int seanceId = 0;
                while(result.next()) seanceId = Integer.parseInt(result.getString("id_seansu"));
        seanceId++;

        result1 = stmt1.executeQuery("select FIlM.id_filmu " +
                "FROM FILM " +
                "WHERE FILM.tytul = '"+ title + "' AND FILM.rezyser = '" + director + "'");
        if(result1.next()) {
            String filmId = result1.getString("id_filmu");
            System.out.println(filmId);
            stmt2.executeUpdate("INSERT INTO SEANS " +
                    "VALUES (" + seanceId + ", " + hall + ", " + filmId + ", '" + day + "', '" + start + "')");
        }
        else {
            result2 = stmt2.executeQuery("SELECT FILM.id_filmu " +
                    "FROM FILM");
            int filmID = 0;
            while (result2.next()) filmID = Integer.parseInt(result2.getString("id_filmu"));
            filmID++;
            stmt3.executeUpdate("INSERT INTO FILM " +
                    "VALUES (" + filmID + ", '" + title + "', '" + director + "', '" + genre + "', '" + length + "')");
            stmt4.executeUpdate("INSERT INTO SEANS " +
                    "VALUES (" + seanceId + ", " + hall + ", " + filmID + ", '" + day + "', '" + start + "')");
        }
        return true;
    }

    public boolean changeSeance(String title, String day, String start, String hall, String oldStart, String oldDay, String oldHall) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select SEANS.id_filmu, SEANS.id_seansu " +
                "FROM SEANS " +
                "INNER JOIN FILM " +
                "ON SEANS.id_filmu = FILM.id_filmu " +
                "WHERE FILM.tytul = '" + title + "' AND SEANS.godzina_rozpoczecia = '" + oldStart + "' AND SEANS.dzien = '" + oldDay + "' AND SEANS.id_sali = " + oldHall);

         int seanceId;
        int filmId;

        if(result.next()) {
            seanceId = Integer.parseInt(result.getString(2));
            filmId = Integer.parseInt(result.getString(1));
            this.stmt1 = con.createStatement();
            stmt1.executeUpdate("UPDATE SEANS " +
                    "SET id_sali = " + hall + ", id_filmu = " + filmId + ", dzien = '" + day + "', godzina_rozpoczecia = '" + start + "' " +
                    "WHERE id_seansu = " + seanceId);

            return true;
        }
        else
            return false;
    }

    public String[][] getClientTickets(String userMail) throws SQLException {
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select FIlM.tytul, FILM.rezyser, FILM.gatunek, FILM.czas_trwania, SEANS.dzien, SEANS.godzina_rozpoczecia, SEANS.id_sali, BILET.miejsce, BILET.id_biletu " +
                "from FILM " +
                "inner join SEANS " +
                "on FILM.id_filmu=SEANS.id_filmu " +
                "inner join ZAMOWIENIA " +
                "on SEANS.id_seansu=ZAMOWIENIA.id_seansu " +
                "inner join BILET " +
                "on ZAMOWIENIA.id_zamowienia=BILET.id_zamowienia " +
                "where ZAMOWIENIA.email_klienta = '" + userMail + "'");

        meta = result.getMetaData();

        LocalDate currentTime = LocalDate.now();
        java.util.Date nowDate = java.util.Date.from(currentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
        String[][] data = new String[30][meta.getColumnCount()+1];
        int j = 0, i;
        while (result.next()) {
            i = 0;
            while(i < meta.getColumnCount()-1) {
                data[j][i] = result.getString(i+1);
                i++;
            }
            LocalDate date = LocalDate.parse(String.valueOf(data[j][4]));
            data[j][i] = (Date.valueOf(date).after(nowDate)) ? "aktywny" : "nieaktywny";
            j++;
        }
        return data;
    }

    public String[] getSeats(int seats) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();

        result = stmt.executeQuery("select BILET.miejsce, SALA.liczba_miejsc, SEANS.id_seansu " +
                "from BILET " +
                "inner join ZAMOWIENIA on BILET.id_zamowienia=ZAMOWIENIA.id_zamowienia " +
                "inner join SEANS on ZAMOWIENIA.id_seansu=SEANS.id_seansu " +
                "inner join SALA on SEANS.id_sali=SALA.id_sali " +
                "where SEANS.id_seansu = " + seats);
        meta = result.getMetaData();
        ArrayList<String> seatTable = new ArrayList<>();

        for (int w = 0; w < Integer.parseInt(result.getString(1)); w++)
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
        return seatsRes;
    }

    private int orderId;
    private int ticketId;

    public void newOrder(String userMail, int seanceID, int selectedSeat) throws ClassNotFoundException, SQLException {
        this.stmt = con.createStatement();
        this.orderId++;
        this.ticketId++;
        result = stmt.executeQuery("select ZAMOWIENIA.id_zamowienia from ZAMOWIENIA");
        meta = result.getMetaData();
        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                if(Integer.parseInt(result.getString("id_zamowienia")) > this.orderId)
                    this.orderId = Integer.parseInt(result.getString("id_zamowienia")) + 1;
            }
        }

        result = stmt.executeQuery("select BILET.id_biletu from BILET");
        meta = result.getMetaData();
        while (result.next()) {
            for (int i = 0; i < meta.getColumnCount(); i++) {
                if(Integer.parseInt(result.getString("id_biletu")) > this.ticketId)
                    this.ticketId = Integer.parseInt(result.getString("id_biletu")) + 1;
            }
        }

        System.out.println("id zamowienia: " + orderId + " id biletu: " + ticketId);

        stmt.executeUpdate("insert into ZAMOWIENIA " +
                "values (" + orderId + ", '" + userMail + "', " + seanceID + ")");
        stmt.executeUpdate("insert into BILET " +
                "values (" + ticketId + ", '" + orderId + "', " + selectedSeat + ")");
    }
}
