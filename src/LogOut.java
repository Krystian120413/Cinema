import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class LogOut {
    private ImageIcon imageForLabel;

    public void display(){

        JFrame frame = new JFrame("KINO");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            imageForLabel = new ImageIcon(ImageIO.read(new File("img/cinema.jpg")));
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

        JLabel logOutLabel = new JLabel("Pomyślnie wylogowano ", JLabel.CENTER);
        logOutLabel.setForeground(Color.WHITE);
        logOutLabel.setFont(new Font(logOutLabel.getFont().getFontName(), Font.PLAIN, 20));

        JButton closeButton = new JButton("Zamknij");

        closeButton.setFocusable(false);

        topPanel.add(logOutLabel);
        bottomPanel.add(closeButton);

        basePanel.add(topPanel, BorderLayout.CENTER);
        basePanel.add(bottomPanel, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);


        closeButton.addActionListener(e -> frame.dispose());

        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
