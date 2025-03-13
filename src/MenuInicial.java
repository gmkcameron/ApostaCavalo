import javax.swing.*;
import java.awt.*;

public class MenuInicial extends JFrame {
    public MenuInicial() {
        setTitle("🏇 Apostas de Cavalos - Menu Inicial");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel do título
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(30, 30, 30));
        JLabel titleLabel = new JLabel("🏆 Corrida de Cavalos 🏆", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Painel central com botões
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 1, 10, 10));
        centerPanel.setBackground(new Color(50, 50, 50));

        JButton startButton = createStyledButton("🎮 Iniciar Jogo");
        JButton rulesButton = createStyledButton("📜 Regras");
        JButton exitButton = createStyledButton("❌ Sair");

        centerPanel.add(startButton);
        centerPanel.add(rulesButton);
        centerPanel.add(exitButton);

        add(centerPanel, BorderLayout.CENTER);

        // Ação do botão "Iniciar Jogo"
        startButton.addActionListener(e -> {
            dispose(); // Fecha o menu
            SwingUtilities.invokeLater(ApostaDeCavalo::new); // Abre a tela do jogo de apostas
        });

        rulesButton.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "📜 Regras do Jogo:\n" +
                        "- Escolha um cavalo e aposte um valor.\n" +
                        "- O cavalo vencedor é sorteado aleatoriamente.\n" +
                        "- Se seu cavalo vencer, você recebe o dobro do valor apostado!\n",
                "Regras do Jogo", JOptionPane.INFORMATION_MESSAGE));

        exitButton.addActionListener(e -> System.exit(0));

        setVisible(true);
    }

    // Método para criar botões estilizados
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(0, 120, 215));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createRaisedBevelBorder());

        // Efeito ao passar o mouse
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 150, 250));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 120, 215));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MenuInicial::new);
    }
}
