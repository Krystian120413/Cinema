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
        JTextField userField = new JTextField(12);
        JLabel passLabel = new JLabel("Hasło: ", JLabel.CENTER);
        passLabel.setForeground(Color.WHITE);
        JPasswordField passField = new JPasswordField(12);

        topPanel.add(userLabel);
        topPanel.add(userField);
        topPanel.add(passLabel);
        topPanel.add(passField);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton loginButton = new JButton("ZALOGUJ");
        loginButton.setHorizontalTextPosition(AbstractButton.CENTER);
        loginButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        loginButton.setFocusable(false);
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


        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
