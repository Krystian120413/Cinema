import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class AdminPanel {
    private ImageIcon imageForLabel;


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

        JLabel adminLabel = new JLabel(("Panel Administratora"), JLabel.CENTER);
        adminLabel.setForeground(Color.WHITE);
        adminLabel.setFont(new Font("Arial", Font.BOLD, 16));

        JButton seanceButton = new JButton(new ImageIcon("img/buttons/seancesButton.png"));
        JButton logOutButton = new JButton(new ImageIcon("img/buttons/logoutButton.png"));
        seanceButton.setContentAreaFilled(false);
        logOutButton.setContentAreaFilled(false);
        seanceButton.setFocusable(false);
        logOutButton.setFocusable(false);

        topPanel.add(adminLabel);
        bottomPanel.add(seanceButton);
        bPanel.add(logOutButton);

        basePanel.add(topPanel, BorderLayout.NORTH);
        basePanel.add(bottomPanel, BorderLayout.CENTER);
        basePanel.add(bPanel, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);

        seanceButton.addActionListener(e -> {
            Seances seances = new Seances();
            try {
                seances.seancesListAdmin();
                frame.dispose();
            } catch (SQLException | ClassNotFoundException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
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
