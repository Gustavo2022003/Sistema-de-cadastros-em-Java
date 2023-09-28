import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TelaCadastroContato extends JFrame {

    private JTextField nomeField;
    private JTextField telefoneField;
    private JTextField emailField;
    private JRadioButton masculinoRadioButton;
    private JRadioButton femininoRadioButton;
    private JButton cadastrarButton;

    public TelaCadastroContato() {
        setTitle("Cadastro de Contato");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("Nome:"));
        nomeField = new JTextField(20);
        panel.add(nomeField);
        panel.add(new JLabel("Telefone:"));
        telefoneField = new JTextField(15);
        panel.add(telefoneField);
        panel.add(new JLabel("Email:"));
        emailField = new JTextField(30);
        panel.add(emailField);
        panel.add(new JLabel("Sexo:"));
        masculinoRadioButton = new JRadioButton("Masculino");
        femininoRadioButton = new JRadioButton("Feminino");
        ButtonGroup sexoGroup = new ButtonGroup();
        sexoGroup.add(masculinoRadioButton);
        sexoGroup.add(femininoRadioButton);
        JPanel sexoPanel = new JPanel();
        sexoPanel.add(masculinoRadioButton);
        sexoPanel.add(femininoRadioButton);
        panel.add(sexoPanel);

        cadastrarButton = new JButton("Cadastrar");
        panel.add(cadastrarButton);

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarContato();
            }
        });

        add(panel);
    }

    private void cadastrarContato() {
        String nome = nomeField.getText();
        String telefone = telefoneField.getText();
        String email = emailField.getText();
        String sexo = masculinoRadioButton.isSelected() ? "M" : "F";

        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "O nome é obrigatório.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/contatos", "root", "root");
            String sql = "INSERT INTO contatos (nome, telefone, email, sexo) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.setString(2, telefone);
            stmt.setString(3, email);
            stmt.setString(4, sexo);
            int rowsAffected = stmt.executeUpdate();
            conn.close();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Contato cadastrado com sucesso!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao cadastrar o contato.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new TelaCadastroContato().setVisible(true);
            }
        });
    }
}
