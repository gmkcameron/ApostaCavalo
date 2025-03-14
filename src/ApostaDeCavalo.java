import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ApostaDeCavalo extends JFrame {
    private final String[] horses = {"Relâmpago", "Tempestade", "Trovão", "Furacão", "Vento Forte"};
    private final JComboBox<String> horseSelection;
    private final JTextField betAmountField;
    private final JLabel resultLabel;
    private final JLabel balanceLabel;
    private final JButton placeBetButton;
    private final RacePanel racePanel;
    private final DefaultListModel<String> historyModel;
    private final JList<String> historyList;

    private boolean raceInProgress = false;
    private double saldo = 1000.00; // Saldo inicial do jogador
    private final Map<String, Integer> horseWins = new HashMap<>(); // Contador de vitórias por cavalo

    public ApostaDeCavalo() {
        setTitle("🏇 Apostas de Cavalos");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Inicializa contadores de vitórias para cada cavalo
        for (String horse : horses) {
            horseWins.put(horse, 0);
        }

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

        // Histórico de Vitórias dos Cavalos
        historyModel = new DefaultListModel<>();
        historyList = new JList<>(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyList);
        scrollPane.setBorder(BorderFactory.createTitledBorder("🏆 Ranking de Vitórias"));
        scrollPane.setPreferredSize(new Dimension(200, 400));
        add(scrollPane, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        placeBetButton = new JButton("Apostar e Iniciar Corrida!");
        bottomPanel.add(placeBetButton);

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
                
                // Atualiza o número de vitórias do cavalo vencedor
                horseWins.put(winningHorse, horseWins.get(winningHorse) + 1);
                updateHistory(); // Atualiza o ranking na interface

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

    private void updateHistory() {
        historyModel.clear();

        // Ordena os cavalos por número de vitórias em ordem decrescente
        horseWins.entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue())) // Ordena pelo número de vitórias
                .forEach(entry -> historyModel.addElement("🏇 " + entry.getKey() + " - " + entry.getValue() + " vitórias"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ApostaDeCavalo::new);
    }
}
