import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;
import javax.imageio.ImageIO;

public class LogIn {
    private ImageIcon imageForLabel;

    public void displayGUI(){

        DBAcess con = new DBAcess();

        JFrame frame = new JFrame("Logowanie");
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
        JLabel userLabel = new JLabel("Email: ", JLabel.CENTER);
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JTextField userField = new JTextField(12);
        JLabel passLabel = new JLabel("Hasło: ", JLabel.CENTER);
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JPasswordField passField = new JPasswordField(12);

        topPanel.add(userLabel);
        topPanel.add(userField);
        topPanel.add(passLabel);
        topPanel.add(passField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton loginButton = new JButton(new ImageIcon("img/buttons/loginButton.png"));
        loginButton.setHorizontalTextPosition(AbstractButton.CENTER);
        loginButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        loginButton.setFocusable(false);
        loginButton.setContentAreaFilled(false);
        JButton registerButton = new JButton(new ImageIcon("img/buttons/registerButton.png"));
        registerButton.setHorizontalTextPosition(AbstractButton.CENTER);
        registerButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        registerButton.setFocusable(false);
        registerButton.setContentAreaFilled(false);

        bottomPanel.add(registerButton);
        bottomPanel.add(loginButton);

        basePanel.add(topPanel, BorderLayout.CENTER);
        basePanel.add(bottomPanel, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);


        loginButton.addActionListener(e -> {
                String checkUser = userField.getText();
                String checkPassword = String.valueOf(passField.getPassword());
                boolean check = false;
            try {
                check = con.login(checkUser, checkPassword);
            } catch (SQLException | ClassNotFoundException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
            }
            if(check){
                System.out.println("Zalogowano");
                ClientPanel clientPanel = new ClientPanel(checkUser);
                try {
                    clientPanel.display();
                    frame.dispose();
                } catch (SQLException | ClassNotFoundException throwables) {
                    JOptionPane.showMessageDialog(null, "Błąd połączenia");
                }

            }
            else JOptionPane.showMessageDialog(null, "Błędne dane logowania");
        });

        registerButton.addActionListener(e -> {
            JLabel nameLabel = new JLabel("Imię: ");
            JTextField nameField = new JTextField(12);
            JLabel surnameLabel = new JLabel("Nazwisko: ");
            JTextField surnameField = new JTextField(12);
            JLabel emailLabel = new JLabel("Adres email");
            JTextField emailField = new JTextField(12);
            JLabel passwordLabel = new JLabel("Hasło: ");
            JPasswordField passwordField = new JPasswordField(12);
            JLabel repeatedPasswordLabel = new JLabel("Powtórz hasło: ");
            JPasswordField repeatedPasswordField = new JPasswordField(12);


            JPanel queryWindow = new JPanel(new GridLayout(0, 1));
            queryWindow.add(nameLabel);
            queryWindow.add(nameField);
            queryWindow.add(surnameLabel);
            queryWindow.add(surnameField);
            queryWindow.add(emailLabel);
            queryWindow.add(emailField);
            queryWindow.add(passwordLabel);
            queryWindow.add(passwordField);
            queryWindow.add(repeatedPasswordLabel);
            queryWindow.add(repeatedPasswordField);

            int result = JOptionPane.showConfirmDialog(null, queryWindow, ("Zakładanie konta"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                if (String.valueOf(passwordField.getPassword()).equals(String.valueOf(repeatedPasswordField.getPassword()))){
                    try {
                        con.addUser(nameField.getText(), surnameField.getText(), emailField.getText(), String.valueOf(passwordField.getPassword()));
                    } catch (ClassNotFoundException | SQLException classNotFoundException) {
                        //classNotFoundException.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Błąd połączenia");
                    }
                    JOptionPane.showMessageDialog(null, "Założono konto. Możesz się teraz zalogować");
                }
                else JOptionPane.showMessageDialog(null, "Hasła nie są identyczne");
            } else {
                System.out.println("Cancelled");
            }
        });

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
