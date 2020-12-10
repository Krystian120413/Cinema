import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Cinema {

    private ImageIcon imageForLabel;

    private void display(){

        JFrame frame = new JFrame("KINO");
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
        JButton seanceButton = new JButton("Seanse");
        JButton loginButton = new JButton("Zaloguj");

        seanceButton.setFocusable(false);
        loginButton.setFocusable(false);

        bottomPanel.add(seanceButton);
        bottomPanel.add(loginButton);

        basePanel.add(topPanel, BorderLayout.CENTER);
        basePanel.add(bottomPanel, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);


        seanceButton.addActionListener(e -> {
            Seances seances = new Seances();
            try {
                seances.seancesListMain();
                frame.dispose();
            } catch (SQLException | ClassNotFoundException throwables) {
                JOptionPane.showMessageDialog(null, "Błąd połączenia");
                //throwables.printStackTrace();
            }
        });


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

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> new Cinema().display());
    }
}
