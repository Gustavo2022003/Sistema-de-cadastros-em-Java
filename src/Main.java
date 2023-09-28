import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    public Main() {
        setTitle("Sistema de cadastro de contatos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JButton cadastrarButton = new JButton("Cadastrar Contato");
        JButton listarButton = new JButton("Listar Contatos");

        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abra a tela de cadastro de contatos aqui
                new TelaCadastroContato().setVisible(true);
            }
        });

        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new TelaListagemContatos().setVisible(true);
            }
        });

        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(cadastrarButton);
        panel.add(listarButton);

        add(panel);

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main().setVisible(true);
            }
        });
    }
}
