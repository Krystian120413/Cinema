import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class ShowTickets {
    public void ticketsList(String userMail) throws SQLException, ClassNotFoundException {
        DBAcess con = new DBAcess();
        String[][] data = con.getClientTickets(userMail);
        String[] columnNames = {"Film", "Reżyser", "Gatunek", "Czas trwania", "dzień", "godzina rozpoczęcia", "numer sali", "miejsce", "aktywny"};
        final JTable[] table = {new JTable(data, columnNames)};

        table[0].setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < table[0].getColumnCount(); i++)
            table[0].getColumnModel().getColumn(i).setPreferredWidth(130);

        table[0].setPreferredScrollableViewportSize(new Dimension(table[0].getPreferredSize().width, 600));

        final JScrollPane[] scrollPane = {new JScrollPane(table[0])};
        JButton backButton = new JButton("Powrót");
        JFrame frame = new JFrame("Twoje bilety");
        JPanel panel = new JPanel();

        scrollPane[0].getVerticalScrollBar().setPreferredSize(new Dimension(20, Integer.MAX_VALUE));

        backButton.setFocusable(false);

        panel.add(backButton);
        panel.add(scrollPane[0]);

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

        frame.add(panel);
        frame.setMinimumSize(new Dimension(1250, 500));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
