import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class TelaListagemContatos extends JFrame {

    private JTable tabelaContatos;
    private DefaultTableModel modeloTabela;
    private JButton excluirButton;
    private JButton editarButton;
    private JButton atualizarButton;
    private JButton exibirContatoButton;


    public TelaListagemContatos() {
        setTitle("Listagem de Contatos");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Telefone");
        modeloTabela.addColumn("Email");
        modeloTabela.addColumn("Sexo");

        tabelaContatos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaContatos);

        excluirButton = new JButton("Excluir");
        editarButton = new JButton("Editar");

        excluirButton.setEnabled(false);
        editarButton.setEnabled(false);

        atualizarButton = new JButton("Atualizar");

        atualizarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarContatos();
            }
        });

        exibirContatoButton = new JButton("Exibir Contato");

        exibirContatoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exibirContatoSelecionado();
            }
        });


        excluirButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelaContatos.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tabelaContatos.getValueAt(selectedRow, 0);

                    int opcao = JOptionPane.showConfirmDialog(null, "Tem certeza que deseja excluir este contato?", "Confirmação", JOptionPane.YES_NO_OPTION);
                    if (opcao == JOptionPane.YES_OPTION) {
                        if (excluirContato(id)) {
                            modeloTabela.removeRow(selectedRow);
                            JOptionPane.showMessageDialog(null, "Contato excluído com sucesso!");
                        } else {
                            JOptionPane.showMessageDialog(null, "Erro ao excluir o contato.");
                        }
                    }
                }
            }
        });


        editarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelaContatos.getSelectedRow();
                if (selectedRow >= 0) {
                    int id = (int) tabelaContatos.getValueAt(selectedRow, 0);
                    new TelaEdicaoContato(id).setVisible(true);
                }
            }
        });

        tabelaContatos.getSelectionModel().addListSelectionListener(e -> {
            boolean rowSelected = tabelaContatos.getSelectedRow() != -1;
            excluirButton.setEnabled(rowSelected);
            editarButton.setEnabled(rowSelected);
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(excluirButton);
        buttonPanel.add(editarButton);
        buttonPanel.add(atualizarButton);
        buttonPanel.add(exibirContatoButton);

        listarContatos();

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private boolean excluirContato(int id) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/contatos", "root", "root");
            String sql = "DELETE FROM contatos WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            conn.close();
            return rowsAffected > 0;
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao conectar ao banco de dados: " + ex.getMessage());
            return false;
        }
    }

    private void exibirContatoSelecionado() {
        int selectedRow = tabelaContatos.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tabelaContatos.getValueAt(selectedRow, 0);

            new TelaDetalhesContato(id).setVisible(true);
        }
    }



    private void listarContatos() {
        modeloTabela.setRowCount(0);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/contatos", "root", "root");

            String sql = "SELECT id, nome, telefone, email, sexo FROM contatos";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");
                String telefone = rs.getString("telefone");
                String email = rs.getString("email");
                String sexo = rs.getString("sexo");

                modeloTabela.addRow(new Object[]{id, nome, telefone, email, sexo});
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
                String jdbcUrl = "jdbc:mysql://localhost/contatos";
                String usuario = "root";
                String senha = "root";

                try {
                    Connection conn = DriverManager.getConnection(jdbcUrl, usuario, senha);

                    Statement stmt = conn.createStatement();
                    String createTableSQL = "CREATE TABLE IF NOT EXISTS contatos (" +
                            "id INT AUTO_INCREMENT PRIMARY KEY," +
                            "nome VARCHAR(255) NOT NULL," +
                            "telefone VARCHAR(15)," +
                            "email VARCHAR(255)," +
                            "sexo VARCHAR(10)" +
                            ")";
                    stmt.executeUpdate(createTableSQL);

                    conn.close();
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao configurar o banco de dados: " + ex.getMessage());
                }

                new TelaListagemContatos().setVisible(true);
            }
        });
    }
}
