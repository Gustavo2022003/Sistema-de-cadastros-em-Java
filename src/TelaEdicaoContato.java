import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TelaEdicaoContato extends JFrame {

    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JRadioButton masculinoRadioButton;
    private JRadioButton femininoRadioButton;
    private int idContato;

    public TelaEdicaoContato(int id) {
        this.idContato = id;

        setTitle("Edição de Contato");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        // Inicialize os campos de texto
        nomeField = new JTextField(20);
        telefoneField = new JTextField(15);
        emailField = new JTextField(30);
        masculinoRadioButton = new JRadioButton("Masculino");
        femininoRadioButton = new JRadioButton("Feminino");

        ButtonGroup sexoGroup = new ButtonGroup();
        sexoGroup.add(masculinoRadioButton);
        sexoGroup.add(femininoRadioButton);

        JButton salvarButton = new JButton("Salvar");

        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarEdicaoContato();
            }
        });

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Telefone:"));
        panel.add(telefoneField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Sexo:"));
        panel.add(masculinoRadioButton);
        panel.add(new JLabel(""));
        panel.add(femininoRadioButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(salvarButton);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Carregue os dados do contato com base no ID
        carregarDadosContato(panel, buttonPanel, masculinoRadioButton, femininoRadioButton);
    }

    private void carregarDadosContato(JPanel panel, JPanel buttonPanel, JRadioButton masculinoRadioButton, JRadioButton femininoRadioButton) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/contatos", "root", "root");
            String sql = "SELECT nome, telefone, email, sexo FROM contatos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, idContato);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String sexo = rs.getString("sexo");

                nomeField.setText(nome);
                telefoneField.setText(telefone);
                emailField.setText(email);

                if ("M".equals(sexo)) {
                    panel.setBackground(Color.GREEN);
                    buttonPanel.setBackground(Color.GREEN);
                    masculinoRadioButton.setBackground(Color.GREEN);
                    femininoRadioButton.setBackground(Color.GREEN);
                } else if ("F".equals(sexo)) {
                    panel.setBackground(Color.YELLOW);
                    buttonPanel.setBackground(Color.YELLOW);
                    masculinoRadioButton.setBackground(Color.YELLOW);
                    femininoRadioButton.setBackground(Color.YELLOW);
                }
            }

            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
        }
    }

    private void salvarEdicaoContato() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String sexo = masculinoRadioButton.isSelected() ? "M" : "F";

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/contatos", "root", "root");
            String sql = "UPDATE contatos SET nome=?, telefone=?, email=?, sexo=? WHERE id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, email);
            stmt.setString(4, sexo);
            stmt.setInt(5, idContato);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Contato atualizado com sucesso!");
                dispose(); // Feche a tela de edição após a atualização bem-sucedida
            } else {
                JOptionPane.showMessageDialog(this, "Ocorreu um erro ao atualizar o contato.");
            }

            conn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaEdicaoContato(1).setVisible(true);
            }
        });
    }
}
