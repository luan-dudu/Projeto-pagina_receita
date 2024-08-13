#import javax.swing.*;
#import java.awt.*;
#import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PlanoDeVooApp {

    private static JTextField aeronaveField;
    private static JTextField prefixoField;
    private static JTextField tipoDeVooField;
    private static JTextField rotaField;
    private static JTextField horarioPrevistoField;
    private static JTextField nivelDeVooField;
    private static JTextField velocidadeDeVooField;
    private static JTextField pilotoComandanteField;
    private static JTextField copilotoField;
    private static JTextArea observacoesField;

    public static void main(String[] args) {
        criarBanco();
        SwingUtilities.invokeLater(() -> criarJanelaInicial());
    }

    private static void criarBanco() {
        String url = "jdbc:mysql://localhost:3306/exemplobd";
        String user = "root";
        String password = "root";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement dropStmt = conn.prepareStatement("DROP TABLE IF EXISTS plano_voo");
             PreparedStatement createStmt = conn.prepareStatement(
                     "CREATE TABLE plano_voo (" +
                     "id INT PRIMARY KEY AUTO_INCREMENT, " +
                     "Aeronave VARCHAR(255), " +
                     "Prefixo VARCHAR(255), " +
                     "Tipo_de_Voo VARCHAR(255), " +
                     "Rota VARCHAR(255), " +
                     "Horario_Previsto VARCHAR(255), " +
                     "Nivel_de_Voo VARCHAR(255), " +
                     "Velocidade_de_Voo VARCHAR(255), " +
                     "Piloto_Comandante VARCHAR(255), " +
                     "Copiloto VARCHAR(255), " +
                     "Observacoes TEXT)"
             )) {
            dropStmt.execute();
            createStmt.execute();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao criar banco de dados: " + e.getMessage());
        }
    }

    private static void criarJanelaInicial() {
        JFrame frame = new JFrame("Envio de Plano de Voo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Envio de Plano de Voo", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 24));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(10, 2, 10, 10));

        aeronaveField = criarCampoFormulario(formPanel, "Aeronave:");
        prefixoField = criarCampoFormulario(formPanel, "Prefixo:");
        tipoDeVooField = criarCampoFormulario(formPanel, "Tipo de Voo (VFR/IFR):");
        rotaField = criarCampoFormulario(formPanel, "Rota:");
        horarioPrevistoField = criarCampoFormulario(formPanel, "Horário Previsto:");
        nivelDeVooField = criarCampoFormulario(formPanel, "Nível de Voo:");
        velocidadeDeVooField = criarCampoFormulario(formPanel, "Velocidade de Voo:");
        pilotoComandanteField = criarCampoFormulario(formPanel, "Piloto Comandante:");
        copilotoField = criarCampoFormulario(formPanel, "Copiloto:");
        observacoesField = new JTextArea();
        observacoesField.setFont(new Font("Verdana", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(observacoesField);
        formPanel.add(new JLabel("Observações:", SwingConstants.RIGHT));
        formPanel.add(scrollPane);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));

        JButton enviarButton = new JButton("Enviar");
        enviarButton.setFont(new Font("Verdana", Font.BOLD, 14));
        enviarButton.setBackground(new Color(34, 139, 34));
        enviarButton.setForeground(Color.WHITE);
        enviarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enviarPlano();
            }
        });
        buttonPanel.add(enviarButton);

        JButton cancelarButton = new JButton("Cancelar");
        cancelarButton.setFont(new Font("Verdana", Font.BOLD, 14));
        cancelarButton.setBackground(new Color(178, 34, 34));
        cancelarButton.setForeground(Color.WHITE);
        cancelarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });
        buttonPanel.add(cancelarButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    private static JTextField criarCampoFormulario(JPanel painel, String texto) {
        JLabel label = new JLabel(texto, SwingConstants.RIGHT);
        label.setFont(new Font("Verdana", Font.PLAIN, 14));
        painel.add(label);

        JTextField textField = new JTextField();
        textField.setFont(new Font("Verdana", Font.PLAIN, 14));
        painel.add(textField);

        return textField;
    }

    private static void enviarPlano() {
        String sql = "INSERT INTO plano_voo (Aeronave, Prefixo, Tipo_de_Voo, Rota, Horario_Previsto, Nivel_de_Voo, Velocidade_de_Voo, Piloto_Comandante, Copiloto, Observacoes) VALUES (?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exemplobd", "root", "root");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aeronaveField.getText());
            pstmt.setString(2, prefixoField.getText());
            pstmt.setString(3, tipoDeVooField.getText());
            pstmt.setString(4, rotaField.getText());
            pstmt.setString(5, horarioPrevistoField.getText());
            pstmt.setString(6, nivelDeVooField.getText());
            pstmt.setString(7, velocidadeDeVooField.getText());
            pstmt.setString(8, pilotoComandanteField.getText());
            pstmt.setString(9, copilotoField.getText());
            pstmt.setString(10, observacoesField.getText());

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Plano de voo enviado com sucesso!");

            limparCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao enviar plano de voo: " + e.getMessage());
        }
    }

    private static void limparCampos() {
        aeronaveField.setText("");
        prefixoField.setText("");
        tipoDeVooField.setText("");
        rotaField.setText("");
        horarioPrevistoField.setText("");
        nivelDeVooField.setText("");
        velocidadeDeVooField.setText("");
        pilotoComandanteField.setText("");
        copilotoField.setText("");
        observacoesField.setText("");
    }
}