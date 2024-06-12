package dawbird;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import javax.swing.border.MatteBorder;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Score extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Image fondo;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    String nick = "Usuario"; // Aquí deberías pasar el nick autenticado
                    ResultSet scoreData = DBConnectionUser.getScoreByNick(nick);

                    Score frame = new Score(nick, scoreData);
                    frame.setVisible(true);

                    if (scoreData != null) {
                        scoreData.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Score(String apodo, ResultSet scoreData) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(8000, 10000, 2000, 1200);

        fondo = new ImageIcon("/home/tansorlaz/eclipse-workspace/DawBird/img/fondo_XP.png").getImage();

        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBackground(new Color(238, 238, 238));
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel.setBackground(new Color(218, 233, 255));
        panel.setBounds(576, 217, 752, 564);
        contentPane.add(panel);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBorder(new MatteBorder(2, 2, 2, 2, new Color(0, 0, 0)));
        panel_1.setBounds(0, 0, 752, 72);
        panel.add(panel_1);

        JLabel lblScore = new JLabel("SCORE");
        lblScore.setHorizontalAlignment(SwingConstants.CENTER);
        lblScore.setForeground(new Color(119, 118, 123));
        lblScore.setFont(new Font("TeX Gyre Bonum", Font.BOLD, 28));
        lblScore.setBounds(185, 0, 350, 72);
        panel_1.add(lblScore);

        JButton btnNewButton = new JButton("X");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        btnNewButton.setBackground(new Color(246, 245, 244));
        btnNewButton.setBounds(697, 12, 43, 25);
        panel_1.add(btnNewButton);

        JLabel lblNameHeader = new JLabel("Nick");
        lblNameHeader.setFont(new Font("Dialog", Font.BOLD, 22));
        lblNameHeader.setBounds(53, 93, 55, 15);
        panel.add(lblNameHeader);

        JLabel lblDateHeader = new JLabel("Date");
        lblDateHeader.setFont(new Font("Dialog", Font.BOLD, 22));
        lblDateHeader.setBounds(333, 93, 100, 15);
        panel.add(lblDateHeader);

        JLabel lblPointsHeader = new JLabel("Points");
        lblPointsHeader.setFont(new Font("Dialog", Font.BOLD, 22));
        lblPointsHeader.setBounds(624, 93, 116, 15);
        panel.add(lblPointsHeader);

        try {
            int yPosition = 120;
            while (scoreData != null && scoreData.next()) {
                String fecha = scoreData.getString("date");
                String puntos = scoreData.getString("points");

                JPanel panel_ScoreRow = new JPanel();
                panel_ScoreRow.setBounds(53, yPosition, 650, 30);
                panel.add(panel_ScoreRow);
                panel_ScoreRow.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
                panel_ScoreRow.setLayout(null);

                JLabel lblUsuario = new JLabel(apodo);
                lblUsuario.setBounds(10, 5, 200, 20);
                panel_ScoreRow.add(lblUsuario);

                JLabel lblFecha = new JLabel(fecha);
                lblFecha.setBounds(280, 5, 200, 20);
                panel_ScoreRow.add(lblFecha);

                JLabel lblPuntos = new JLabel(puntos);
                lblPuntos.setHorizontalAlignment(SwingConstants.RIGHT);
                lblPuntos.setBounds(574, 5, 65, 15);
                panel_ScoreRow.add(lblPuntos);

                yPosition += 40; // Incrementar la posición y para la próxima fila
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}