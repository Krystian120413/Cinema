import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class Seances {
    public void seancesListMain() throws SQLException, ClassNotFoundException {
        DBAcess con = new DBAcess();
        String[][] data = con.getSeances();
        String[] columnNames = {"Tytuł", "Reżyser", "Gatunek", "Czas trwania", "dzień", "godzina rozpoczęcia", "numer sali", "liczba miejsc"};
        final JTable[] table = {new JTable(data, columnNames)};

        table[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table[0].getColumnCount(); i++)
            table[0].getColumnModel().getColumn(i).setPreferredWidth(130);

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 600));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        JButton loginButton = new JButton("Zaloguj");
        JFrame frame = new JFrame("Baza Seanse");
        JPanel panel = new JPanel();

        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        panel.add(loginButton);
        panel.add(scrollPane[0]);

        loginButton.addActionListener(e -> {
            LogIn login = new LogIn();
            login.displayGUI();
            frame.setVisible(false);
        });

        frame.add(panel);
        frame.setMinimumSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void seancesListClient() throws SQLException, ClassNotFoundException {
        DBAcess con = new DBAcess();
        String[][] data = con.getSeances();
        String[] columnNames = {"Tytuł", "Reżyser", "Gatunek", "Czas trwania", "dzień", "godzina rozpoczęcia", "numer sali", "liczba miejsc"};
        final JTable[] table = {new JTable(data, columnNames)};

        table[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table[0].getColumnCount(); i++)
            table[0].getColumnModel().getColumn(i).setPreferredWidth(130);

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 600));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        JButton buyTicketButton = new JButton("Kup bilet");
        JButton ticketsButton = new JButton("Twoje bilety");
        JFrame frame = new JFrame("Baza Seanse");
        JPanel panel = new JPanel();

        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        panel.add(buyTicketButton);
        panel.add(ticketsButton);
        panel.add(scrollPane[0]);



        frame.add(panel);
        frame.setMinimumSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
