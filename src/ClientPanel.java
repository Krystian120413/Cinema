import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ClientPanel {
    private final String email;
    private ImageIcon imageForLabel;

    public ClientPanel(String email){
        this.email = email;
    }



    public void display() throws SQLException, ClassNotFoundException {

        DBAcess con = new DBAcess();

        JFrame frame = new JFrame("Panel Klienta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            imageForLabel = new ImageIcon(ImageIO.read(new File("img/tickets.jpg")));
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

        JPanel bPanel = new JPanel();
        bPanel.setOpaque(false);

        String user = con.client(email);
        
        if(user.contains("Administrator")){
            AdminPanel adminPanel = new AdminPanel();
            adminPanel.display();
            frame.dispose();
        }
        else {
            JLabel userLabel = new JLabel(("Witaj " + user), JLabel.CENTER);
            userLabel.setForeground(Color.WHITE);
            userLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JButton boughtTicketButton = new JButton(new ImageIcon("img/buttons/yourTicketsButton.png"));
            JButton seanceButton = new JButton(new ImageIcon("img/buttons/seancesButton.png"));
            JButton changeData = new JButton(new ImageIcon("img/buttons/changeDataButton.png"));
            JButton deleteAccountButton = new JButton(new ImageIcon("img/buttons/deleteAccountButton.png"));
            JButton logOutButton = new JButton(new ImageIcon("img/buttons/logoutButton.png"));
            boughtTicketButton.setContentAreaFilled(false);
            seanceButton.setContentAreaFilled(false);
            changeData.setContentAreaFilled(false);
            deleteAccountButton.setContentAreaFilled(false);
            logOutButton.setContentAreaFilled(false);
            boughtTicketButton.setFocusable(false);
            seanceButton.setFocusable(false);
            changeData.setFocusable(false);
            deleteAccountButton.setFocusable(false);
            logOutButton.setFocusable(false);

            topPanel.add(userLabel);
            bottomPanel.add(boughtTicketButton);
            bottomPanel.add(seanceButton);
            bottomPanel.add(changeData);
            bottomPanel.add(deleteAccountButton);
            bPanel.add(logOutButton);

            basePanel.add(topPanel, BorderLayout.NORTH);
            basePanel.add(bottomPanel, BorderLayout.CENTER);
            basePanel.add(bPanel, BorderLayout.PAGE_END);

            imageLabel.setLayout(new GridBagLayout());
            imageLabel.add(basePanel);

            contentPane.add(imageLabel, BorderLayout.CENTER);


            boughtTicketButton.addActionListener(e -> {
                ShowTickets showTickets = new ShowTickets();
                try {
                    showTickets.ticketsList(email);
                    frame.dispose();
                } catch (SQLException | ClassNotFoundException throwables) {
                    //throwables.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Błąd połączenia");
                }

            });

            seanceButton.addActionListener(e -> {
                Seances seances = new Seances();
                try {
                    seances.seancesListClient(email);
                    frame.dispose();
                } catch (SQLException | ClassNotFoundException throwables) {
                    JOptionPane.showMessageDialog(null, "Błąd połączenia");
                }
            });

            changeData.addActionListener(e -> {
                try {
                    String[] userDate = con.getClientData(email);

                    JTextField nameField = new JTextField(userDate[0]);
                    JTextField surnameField = new JTextField(userDate[1]);
                    JPasswordField passwordField = new JPasswordField();
                    JPanel queryWindow = new JPanel(new GridLayout(0, 1));

                    queryWindow.add(new JLabel("Imię: "));
                    queryWindow.add(nameField);
                    queryWindow.add(new JLabel("Nazwisko: "));
                    queryWindow.add(surnameField);
                    queryWindow.add(new JLabel("Wprowadź hasło aby zatwierdzić dane"));
                    queryWindow.add(passwordField);


                    int result = JOptionPane.showConfirmDialog(null, queryWindow, ("Zmiana danych: "), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                    if (result == JOptionPane.OK_OPTION && userDate[3].equals(String.valueOf(passwordField.getPassword()))) {
                        con.updateUserData(nameField.getText(), surnameField.getText(), userDate[2]);
                        JOptionPane.showMessageDialog(null, "Dane zaktualizowane pomyślnie");
                    }
                } catch (ClassNotFoundException | SQLException classNotFoundException) {
                    //classNotFoundException.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Błąd połączenia");
                }
            });

            deleteAccountButton.addActionListener(e -> {
                JLabel emailLabel = new JLabel("Wprowadź swój email: ");
                JTextField emailField = new JTextField(12);
                JLabel passwordLabel = new JLabel("Hasło: ");
                JPasswordField passwordField = new JPasswordField(12);
                JLabel repeatedPasswordLabel = new JLabel("Powtórz hasło: ");
                JPasswordField repeatedPasswordField = new JPasswordField(12);

                JPanel queryWindow = new JPanel(new GridLayout(0, 1));
                queryWindow.add(emailLabel);
                queryWindow.add(emailField);
                queryWindow.add(passwordLabel);
                queryWindow.add(passwordField);
                queryWindow.add(repeatedPasswordLabel);
                queryWindow.add(repeatedPasswordField);

                int result = JOptionPane.showConfirmDialog(null, queryWindow, ("USUWANIE KONTA"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    if (String.valueOf(passwordField.getPassword()).equals(String.valueOf(repeatedPasswordField.getPassword()))) {
                        try {
                            con.deleteUser(emailField.getText(), String.valueOf(passwordField.getPassword()));
                        } catch (ClassNotFoundException | SQLException classNotFoundException) {
                            classNotFoundException.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "Konto usunięte.");
                        LogIn login = new LogIn();
                        login.displayGUI();
                        frame.dispose();
                    } else JOptionPane.showMessageDialog(null, "Hasła nie są identyczne");
                } else {
                    System.out.println("Cancelled");
                }
            });

            logOutButton.addActionListener(e -> {
                LogOut logout = new LogOut();
                logout.display();
                frame.dispose();
            });


            frame.setContentPane(contentPane);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
        }
    }
}
