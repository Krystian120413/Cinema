import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


public class ShowTickets {
    private ImageIcon imageForLabel;
    public void ticketsList(String userMail) throws SQLException, ClassNotFoundException {

        DBAcess con = new DBAcess();
        String[][] data = con.getClientTickets(userMail);
        String[] columnNames = {"Film", "Reżyser", "Gatunek", "Czas trwania", "dzień", "godzina rozpoczęcia", "numer sali", "miejsce", "aktywny"};
        final JTable[] table = {new JTable(data, columnNames)};



        table[0].getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));

        for (int i = 0; i < table[0].getColumnCount(); i++) {
            table[0].getColumnModel().getColumn(i).setPreferredWidth(130);
        }

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 300));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        JButton backButton = new JButton(new ImageIcon("img/buttons/backButton.png"));
        backButton.setContentAreaFilled(false);
        backButton.setFocusable(false);
        JFrame frame = new JFrame("Twoje bilety");
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout(5, 5));
        try {
            imageForLabel = new ImageIcon(ImageIO.read(new File("img/filmB.jpg")));
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
        topPanel.add(backButton);
        bottomPanel.add(scrollPane[0]);
        basePanel.add(topPanel, BorderLayout.CENTER);
        imageLabel.setLayout(new GridBagLayout());
        imageLabel.add(basePanel);
        contentPane.add(imageLabel, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.PAGE_END);


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
        frame.add(panel);
        frame.setMinimumSize(new Dimension(1000, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
