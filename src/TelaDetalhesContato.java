import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class TelaDetalhesContato extends JFrame {

    public TelaDetalhesContato(int contatoId) {
        setTitle("Detalhes do Contato");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(4, 2));
        setContentPane(contentPane);

        JLabel nomeLabel = new JLabel("Nome:");
        JLabel telefoneLabel = new JLabel("Telefone:");
        JLabel emailLabel = new JLabel("Email:");
        JLabel sexoLabel = new JLabel("Sexo:");

        JLabel nomeField = new JLabel();
        JLabel telefoneField = new JLabel();
        JLabel emailField = new JLabel();
        JLabel sexoField = new JLabel();

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/contatos", "root", "root");
            String sql = "SELECT nome, telefone, email, sexo FROM contatos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, contatoId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String sexo = rs.getString("sexo");

                nomeField.setText(nome);
                if (telefone != null && !telefone.isEmpty()) {
                    telefoneField.setText(telefone);
                } else {
                    telefoneField.setText("O telefone não foi informado");
                }
                if (email != null && !email.isEmpty()) {
                    emailField.setText(email);
                } else {
                    emailField.setText("O email não foi informado");
                }
                sexoField.setText(sexo);

                if ("M".equals(sexo)) {
                    contentPane.setBackground(Color.GREEN);
                } else if ("F".equals(sexo)) {
                    contentPane.setBackground(Color.YELLOW);
                }
            }

            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
        }

        contentPane.add(nomeLabel);
        contentPane.add(nomeField);
        contentPane.add(telefoneLabel);
        contentPane.add(telefoneField);
        contentPane.add(emailLabel);
        contentPane.add(emailField);
        contentPane.add(sexoLabel);
        contentPane.add(sexoField);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaDetalhesContato(1).setVisible(true);
            }
        });
    }
}
