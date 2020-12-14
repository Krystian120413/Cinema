import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Seances {
    private ImageIcon imageForLabel;

    public void seancesListMain() throws SQLException, ClassNotFoundException, IOException {
        DBAcess con = new DBAcess();
        String[][] data = con.getSeances();
        String[] columnNames = {"Tytuł", "Reżyser", "Gatunek", "Czas trwania", "dzień", "godzina rozpoczęcia", "numer sali", "liczba miejsc"};
        final JTable[] table = {new JTable(data, columnNames)};

        table[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table[0].getColumnCount(); i++)
            table[0].getColumnModel().getColumn(i).setPreferredWidth(130);

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 300));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        JFrame frame = new JFrame("KINO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            imageForLabel = new ImageIcon(ImageIO.read(new File("img/film.jpg")));
        } catch(IOException mue) {
            JOptionPane.showMessageDialog(null, "Błąd połączenia");
        }
        JLabel imageLabel = new JLabel(imageForLabel);
        JPanel basePanel = new JPanel();
        basePanel.setOpaque(false);
        basePanel.setLayout(new BorderLayout(5, 5));
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new GridLayout(2, 2, 5, 5));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);


        JButton loginButton = new JButton(new ImageIcon("img/buttons/secondLoginButton.png"));
        loginButton.setContentAreaFilled(false);

        loginButton.setFocusable(false);

        topPanel.add(loginButton);
        bottomPanel.add(scrollPane[0]);

        basePanel.add(topPanel, BorderLayout.CENTER);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.PAGE_END);

        loginButton.addActionListener(e -> {
            LogIn login = new LogIn();
            login.displayGUI();
            frame.dispose();
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
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

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 300));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        JButton buyTicketButton = new JButton(new ImageIcon("img/buttons/buyTicketButton.png"));
        JButton backButton = new JButton(new ImageIcon("img/buttons/backButton.png"));
        JFrame frame = new JFrame("KINO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            imageForLabel = new ImageIcon(ImageIO.read(new File("img/film.jpg")));
        } catch(IOException mue) {
            JOptionPane.showMessageDialog(null, "Błąd połączenia");
        }
        JLabel imageLabel = new JLabel(imageForLabel);
        JPanel basePanel = new JPanel();
        basePanel.setOpaque(false);
        basePanel.setLayout(new BorderLayout(5, 5));
        JPanel topPanel = new JPanel();
        topPanel.setOpaque(false);
        topPanel.setLayout(new GridLayout(2, 2, 5, 5));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        buyTicketButton.setFocusable(false);
        backButton.setFocusable(false);

        buyTicketButton.setContentAreaFilled(false);
        backButton.setContentAreaFilled(false);

        topPanel.add(buyTicketButton);
        topPanel.add(backButton);

        bottomPanel.add(scrollPane[0]);

        basePanel.add(topPanel, BorderLayout.CENTER);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.PAGE_END);

        buyTicketButton.addActionListener(e -> {
            int row = 0;
            if (table[0].getSelectedRow() >= 0) row = table[0].getSelectedRow();

            String title = table[0].getValueAt(row, 0).toString();

            try {
                JLabel titleLabel = new JLabel(title);
                JLabel dayLabel = new JLabel("Dzień: " + table[0].getValueAt(row, 4).toString());
                JLabel startLabel = new JLabel("Godznia rozpoczęcia: " + table[0].getValueAt(row, 5).toString());
                JLabel hallLabel = new JLabel("Numer sali: " + table[0].getValueAt(row, 6).toString());
                JLabel seatsLabel = new JLabel("Wybierz miejsce: ");
                String [] seatsTable = con.getSeats(Integer.parseInt(data[row][8]));
                JComboBox<String> selectSeats = new JComboBox<>(seatsTable);
                JPanel queryWindow = new JPanel(new GridLayout(0, 1));
                queryWindow.add(titleLabel);
                queryWindow.add(dayLabel);
                queryWindow.add(startLabel);
                queryWindow.add(hallLabel);
                queryWindow.add(seatsLabel);
                queryWindow.add(selectSeats);

                int result = JOptionPane.showConfirmDialog(null, queryWindow, ("Zakup biletu na film: " + title), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    con.newOrder(userMail, Integer.parseInt(data[row][8]), Integer.parseInt((String) Objects.requireNonNull(selectSeats.getSelectedItem())));
                    JOptionPane.showMessageDialog(null, "Zakupiono bilet.");
                } else {
                    System.out.println("Cancelled");
                }
            } catch (ClassNotFoundException | SQLException classNotFoundException) {
                //classNotFoundException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
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

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setMinimumSize(new Dimension(1100, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
