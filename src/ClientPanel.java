import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ClientPanel {
    private String email;
    private ImageIcon imageForLabel;

    public ClientPanel(Object email){
        this.email = (String) email;
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

        String user = con.client(email);

        JLabel userLabel = new JLabel(("Witaj " + user), JLabel.CENTER);
        userLabel.setForeground(Color.WHITE);

        JButton ticketButton = new JButton("Kup bilet");
        JButton buyedTicketButton = new JButton("Zakupione bilety");
        JButton seanceButton = new JButton("Seanse");

        topPanel.add(userLabel);
        bottomPanel.add(ticketButton);
        bottomPanel.add(buyedTicketButton);
        bottomPanel.add(seanceButton);

        basePanel.add(topPanel, BorderLayout.CENTER);
        basePanel.add(bottomPanel, BorderLayout.PAGE_END);

        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);

        contentPane.add(imageLabel, BorderLayout.CENTER);


        frame.setContentPane(contentPane);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
    }
}
