import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class ApostaDeCavalo extends JFrame {
    private final String[] horses = {"Relâmpago", "Tempestade", "Trovão", "Furacão", "Vento Forte"};
    private final JComboBox<String> horseSelection;
    private final JTextField betAmountField;
    private final JLabel resultLabel;
    private final JButton placeBetButton;
    private final RacePanel racePanel;

    private boolean raceInProgress = false;

    public ApostaDeCavalo() {
        setTitle("🏇 Apostas de Cavalos");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(2, 2));

        topPanel.add(new JLabel("Escolha um cavalo:"));
        horseSelection = new JComboBox<>(horses);
        topPanel.add(horseSelection);

        topPanel.add(new JLabel("Digite o valor da aposta:"));
        betAmountField = new JTextField();
        topPanel.add(betAmountField);

        add(topPanel, BorderLayout.NORTH);

        racePanel = new RacePanel(horses);
        add(racePanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        placeBetButton = new JButton("Apostar e Iniciar Corrida!");
        bottomPanel.add(placeBetButton);
        resultLabel = new JLabel("Resultado: ");
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
                resultLabel.setText("⚠️ Digite um valor de aposta válido.");
                return;
            }

            raceInProgress = true;
            racePanel.startRace(() -> {
                raceInProgress = false;
                String winningHorse = racePanel.getWinningHorse();
                
                if (selectedHorse.equals(winningHorse)) {
                    double prize = betAmount * 2;
                    resultLabel.setText("🎉 Parabéns! " + winningHorse + " venceu! Você ganhou R$" + prize);
                } else {
                    resultLabel.setText("❌ O cavalo vencedor foi " + winningHorse + ". Você perdeu R$" + betAmount);
                }
            });

        } catch (NumberFormatException ex) {
            resultLabel.setText("⚠️ Insira um valor de aposta numérico.");
        }
    }
}
