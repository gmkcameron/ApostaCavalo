import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ApostaDeCavalo extends JFrame {
    private final String[] horses = {"Relâmpago", "Tempestade", "Trovão", "Furacão", "Vento Forte"};
    private final JComboBox<String> horseSelection;
    private final JTextField betAmountField;
    private final JLabel resultLabel;
    private final JLabel balanceLabel;
    private final JButton placeBetButton;
    private final RacePanel racePanel;

    private boolean raceInProgress = false;
    private double saldo = 1000.00; // Saldo inicial do jogador

    public ApostaDeCavalo() {
        setTitle("🏇 Apostas de Cavalos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2));

        topPanel.add(new JLabel("Escolha um cavalo:"));
        horseSelection = new JComboBox<>(horses);
        topPanel.add(horseSelection);

        topPanel.add(new JLabel("Digite o valor da aposta:"));
        betAmountField = new JTextField();
        topPanel.add(betAmountField);

        // Exibir saldo do jogador
        balanceLabel = new JLabel("💰 Saldo: R$" + String.format("%.2f", saldo));
        balanceLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(balanceLabel);

        add(topPanel, BorderLayout.NORTH);

        racePanel = new RacePanel(horses);
        add(racePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        placeBetButton = new JButton("Apostar e Iniciar Corrida!");
        bottomPanel.add(placeBetButton);
        
        // Ajustando a exibição do resultado
        resultLabel = new JLabel("<html><b>Resultado:</b> Aguardando aposta...</html>", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 14));
        resultLabel.setForeground(Color.BLUE);
        bottomPanel.add(resultLabel);
        
        add(bottomPanel, BorderLayout.SOUTH);

        placeBetButton.addActionListener(e -> {
            if (!raceInProgress) {
                placeBet();
            }
        });

        setVisible(true);
    }

    private void placeBet() {
        String selectedHorse = (String) horseSelection.getSelectedItem();
        String betAmountText = betAmountField.getText();
        
        try {
            double betAmount = Double.parseDouble(betAmountText);
            
            if (betAmount <= 0) {
                resultLabel.setText("<html><b>⚠️ Digite um valor de aposta válido.</b></html>");
                return;
            }
            
            if (betAmount > saldo) {
                resultLabel.setText("<html><b>❌ Saldo insuficiente para essa aposta!</b></html>");
                return;
            }

            // Descontar do saldo antes da corrida começar
            saldo -= betAmount;
            balanceLabel.setText("💰 Saldo: R$" + String.format("%.2f", saldo));

            raceInProgress = true;
            racePanel.startRace(() -> {
                raceInProgress = false;
                String winningHorse = racePanel.getWinningHorse(); // Obtém o cavalo vencedor
                
                if (selectedHorse.equals(winningHorse)) {
                    double lucroBruto = betAmount * 2; // O valor ganho antes da taxa
                    double taxaCasa = lucroBruto * 0.10; // 10% para a Casa de Apostas
                    double lucroLiquido = lucroBruto - taxaCasa; // O que o jogador recebe

                    saldo += lucroLiquido; // Atualiza saldo do jogador
                    resultLabel.setText("<html><b>🏆 O cavalo vencedor foi: " + winningHorse + 
                        "!</b><br>🎉 Você ganhou R$" + String.format("%.2f", lucroLiquido) + 
                        " (Casa ficou com: R$" + String.format("%.2f", taxaCasa) + ")</html>");
                } else {
                    resultLabel.setText("<html><b>❌ O cavalo vencedor foi: " + winningHorse + 
                        ".</b><br>Você perdeu R$" + String.format("%.2f", betAmount) + "</html>");
                }

                // Atualizar saldo na interface
                balanceLabel.setText("💰 Saldo: R$" + String.format("%.2f", saldo));

                // Verificar se o saldo zerou e desativar botão de aposta
                if (saldo <= 0) {
                    placeBetButton.setEnabled(false);
                    resultLabel.setText("<html><b>🚫 O cavalo vencedor foi: " + winningHorse + 
                        ".</b><br>Você ficou sem saldo! Reinicie o jogo.</html>");
                }
            });

        } catch (NumberFormatException ex) {
            resultLabel.setText("<html><b>⚠️ Insira um valor de aposta numérico.</b></html>");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApostaDeCavalo::new);
    }
}
