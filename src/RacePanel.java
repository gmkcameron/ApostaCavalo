import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RacePanel extends JPanel {
    private final String[] horses;
    private final int[] positions;
    private boolean raceFinished = false;
    private String winningHorse;
    private final Random random = new Random();

    public RacePanel(String[] horses) {
        this.horses = horses;
        this.positions = new int[horses.length];
        setPreferredSize(new Dimension(600, 200));
    }

    public void startRace(Runnable onFinish) {
        raceFinished = false;
        for (int i = 0; i < positions.length; i++) {
            positions[i] = 0; // Resetando posições
        }

        new Thread(() -> {
            while (!raceFinished) {
                for (int i = 0; i < horses.length; i++) {
                    positions[i] += random.nextInt(10); // Movimentação aleatória

                    if (positions[i] >= getWidth() - 100) { // Quando um cavalo atinge o final
                        raceFinished = true;
                        winningHorse = horses[i];
                        break;
                    }
                }
                repaint();
                try {
                    Thread.sleep(100); // Controle de velocidade
                } catch (InterruptedException ignored) {}
            }
            onFinish.run();
        }).start();
    }

    public String getWinningHorse() {
        return winningHorse;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        for (int i = 0; i < horses.length; i++) {
            g.setColor(Color.BLACK);
            g.fillOval(positions[i], 50 + i * 30, 30, 20); // Desenha os cavalos (círculos)
            g.setColor(Color.BLUE);
            g.drawString(horses[i], positions[i], 45 + i * 30);
        }
    }
}
