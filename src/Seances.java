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
        table[0].getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

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
        table[0].getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
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
                JLabel startLabel = new JLabel("Godzina rozpoczęcia: " + table[0].getValueAt(row, 5).toString());
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

    public void seancesListAdmin() throws SQLException, ClassNotFoundException {
        DBAcess con = new DBAcess();
        String[][] data = con.getSeances();
        String[] columnNames = {"Tytuł", "Reżyser", "Gatunek", "Czas trwania", "dzień", "godzina rozpoczęcia", "numer sali", "liczba miejsc"};
        final JTable[] table = {new JTable(data, columnNames)};

        table[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        table[0].getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        for (int i = 0; i < table[0].getColumnCount(); i++)
            table[0].getColumnModel().getColumn(i).setPreferredWidth(130);

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 300));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        JButton addSeanceButton = new JButton(new ImageIcon("img/buttons/addSeanceButton.png"));
        JButton changeSeanceButton = new JButton(new ImageIcon("img/buttons/changeSeanceButton.png"));
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

        addSeanceButton.setFocusable(false);
        changeSeanceButton.setFocusable(false);
        backButton.setFocusable(false);

        addSeanceButton.setContentAreaFilled(false);
        changeSeanceButton.setContentAreaFilled(false);
        backButton.setContentAreaFilled(false);

        topPanel.add(addSeanceButton);
        topPanel.add(changeSeanceButton);
        topPanel.add(backButton);

        bottomPanel.add(scrollPane[0]);

        basePanel.add(topPanel, BorderLayout.CENTER);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.PAGE_END);

        addSeanceButton.addActionListener(e -> {

            JLabel titleLabel = new JLabel("Tytuł:");
            JTextField titleField = new JTextField();
            JLabel directorLabel = new JLabel("Reżyser:");
            JTextField directorField = new JTextField();
            JLabel genreLabel = new JLabel("Gatunek:");
            JTextField genreField = new JTextField();
            JLabel lengthLabel = new JLabel("Czas trwania:");
            JTextField lengthField = new JTextField();
            JLabel dayLabel = new JLabel("Dzień:");
            JTextField dayField = new JTextField();
            JLabel startLabel = new JLabel("Godzina rozpoczęcia:");
            JTextField startField = new JTextField();
            JLabel hallLabel = new JLabel("Sala:");
            JTextField hallField = new JTextField();

            JPanel queryWindow = new JPanel(new GridLayout(0, 1));
            queryWindow.add(titleLabel);
            queryWindow.add(titleField);
            queryWindow.add(directorLabel);
            queryWindow.add(directorField);
            queryWindow.add(genreLabel);
            queryWindow.add(genreField);
            queryWindow.add(lengthLabel);
            queryWindow.add(lengthField);
            queryWindow.add(dayLabel);
            queryWindow.add(dayField);
            queryWindow.add(startLabel);
            queryWindow.add(startField);
            queryWindow.add(hallLabel);
            queryWindow.add(hallField);

            int result = JOptionPane.showConfirmDialog(null, queryWindow, ("Dodawanie seansu"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            try {
                if (result == JOptionPane.OK_OPTION && con.addSeance(titleField.getText(), directorField.getText(), genreField.getText(), lengthField.getText(), dayField.getText(), startField.getText(), hallField.getText())) {
                    JOptionPane.showMessageDialog(null, "Pomyślnie dodano seans.");
                } else {
                    System.out.println("Cancelled");
                }
            } catch (ClassNotFoundException | SQLException classNotFoundException) {
                classNotFoundException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
            }

        });

        changeSeanceButton.addActionListener(e -> {
            int row = 0;
            if (table[0].getSelectedRow() >= 0) row = table[0].getSelectedRow();

            JLabel titleLabel = new JLabel("Tytuł:");
            JLabel titleField = new JLabel(table[0].getValueAt(row, 0).toString());
            JLabel directorLabel = new JLabel("Reżyser:");
            JLabel directorField = new JLabel(table[0].getValueAt(row, 1).toString());
            JLabel genreLabel = new JLabel("Gatunek:");
            JLabel genreField = new JLabel(table[0].getValueAt(row, 2).toString());
            JLabel lengthLabel = new JLabel("Czas trwania:");
            JLabel lengthField = new JLabel(table[0].getValueAt(row, 3).toString());
            JLabel dayLabel = new JLabel("Dzień:");
            JTextField dayField = new JTextField(table[0].getValueAt(row, 4).toString());
            JLabel startLabel = new JLabel("Godzina rozpoczęcia:");
            JTextField startField = new JTextField(table[0].getValueAt(row, 5).toString());
            JLabel hallLabel = new JLabel("Sala:");
            JTextField hallField = new JTextField(table[0].getValueAt(row, 6).toString());

            JPanel queryWindow = new JPanel(new GridLayout(0, 1));
            queryWindow.add(titleLabel);
            queryWindow.add(titleField);
            queryWindow.add(directorLabel);
            queryWindow.add(directorField);
            queryWindow.add(genreLabel);
            queryWindow.add(genreField);
            queryWindow.add(lengthLabel);
            queryWindow.add(lengthField);
            queryWindow.add(dayLabel);
            queryWindow.add(dayField);
            queryWindow.add(startLabel);
            queryWindow.add(startField);
            queryWindow.add(hallLabel);
            queryWindow.add(hallField);

            int result = JOptionPane.showConfirmDialog(null, queryWindow, ("Dodawanie seansu"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            try {
                if (result == JOptionPane.OK_OPTION && con.changeSeance(titleField.getText(), dayField.getText(), startField.getText(), hallField.getText(), table[0].getValueAt(row, 5).toString(), table[0].getValueAt(row, 4).toString(), table[0].getValueAt(row, 6).toString())) {
                    JOptionPane.showMessageDialog(null, "Zatwierdzono zmiany seans.");
                } else {
                    System.out.println("Cancelled");
                }
            } catch (ClassNotFoundException | SQLException classNotFoundException) {
                classNotFoundException.printStackTrace();
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
            }
        });

        backButton.addActionListener(e -> {
            AdminPanel adminPanel = new AdminPanel();
            try {
                adminPanel.display();
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
