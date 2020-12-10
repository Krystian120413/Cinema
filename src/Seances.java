import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

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

        loginButton.setFocusable(false);
        panel.add(loginButton);
        panel.add(scrollPane[0]);

        loginButton.addActionListener(e -> {
            LogIn login = new LogIn();
            login.displayGUI();
            frame.dispose();
        });

        frame.add(panel);
        frame.setMinimumSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void seancesListClient(String userMail) throws SQLException, ClassNotFoundException {
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
        JButton backButton = new JButton("Powrót");
        JFrame frame = new JFrame("Baza Seanse");
        JPanel panel = new JPanel();

        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        buyTicketButton.setFocusable(false);
        backButton.setFocusable(false);

        panel.add(buyTicketButton);
        panel.add(backButton);
        panel.add(scrollPane[0]);

        buyTicketButton.addActionListener(e -> {
            int row = 0;
            if (table[0].getSelectedRow() >= 0) row = table[0].getSelectedRow();

            int seats = Integer.parseInt(table[0].getValueAt(row, 7).toString());
            String title = table[0].getValueAt(row, 0).toString();



            try {
                JLabel titleLabel = new JLabel(title);
                JLabel dayLabel = new JLabel("Dzień: " + table[0].getValueAt(row, 4).toString());
                JLabel startLabel = new JLabel("Godznia rozpoczęcia: " + table[0].getValueAt(row, 5).toString());
                JLabel hallLabel = new JLabel("Numer sali: " + table[0].getValueAt(row, 6).toString());
                String [] seatsTable = con.getSeats();
                JComboBox<String> selectSeats = new JComboBox<>(seatsTable);
                JPanel queryWindow = new JPanel(new GridLayout(0, 1));
                queryWindow.add(titleLabel);
                queryWindow.add(dayLabel);
                queryWindow.add(startLabel);
                queryWindow.add(hallLabel);
                queryWindow.add(selectSeats);

                int result = JOptionPane.showConfirmDialog(null, queryWindow, ("Zakup biletu na film: " + title), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    con.newOrder(userMail, Integer.parseInt(data[row][8]), Integer.parseInt((String) Objects.requireNonNull(selectSeats.getSelectedItem())));
                    JOptionPane.showMessageDialog(null, "Zakupiono bilet.");
                } else {
                    System.out.println("Cancelled");
                }
            } catch (ClassNotFoundException | SQLException classNotFoundException) {
                classNotFoundException.printStackTrace();
            }

        });

        backButton.addActionListener(e -> {
            ClientPanel clientPanel = new ClientPanel(userMail);
            try {
                clientPanel.display();
                frame.dispose();
            } catch (SQLException | ClassNotFoundException throwables) {
                //throwables.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
            }

        });

        frame.add(panel);
        frame.setMinimumSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
