package screenshoter;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScreenshoterScreen {

    private final JFrame jWindow;
    private Thread screenshoterThread;
    private boolean screenShotFullWindow = false;
    
    public ScreenshoterScreen() {
        jWindow = new JFrame();
        jWindow.setBackground(new Color(0, 0, 0, 0));
        jWindow.setContentPane(new Pane());
        jWindow.pack();
        jWindow.setVisible(true);
        jWindow.setLocationRelativeTo(null);
        jWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    class Pane extends JPanel {
        public Pane() {
            JPanel borderPainel = new JPanel();
            borderPainel.setLayout(new BorderLayout());
            JPanel gridPainel = new JPanel();
            gridPainel.setLayout(new GridLayout(10, 1));

            JButton stopButton = new JButton("Parar");
            JCheckBox fotoTelaInteiraCheckBox = new JCheckBox("Tirar da tela inteira");
            JButton takeShotButton = new JButton("Capturar agora");
            stopButton.setEnabled(false);
            
            takeShotButton.addActionListener((ActionEvent ae) -> {
                takeShotButton.setEnabled(false);
                stopButton.setEnabled(true);
                fotoTelaInteiraCheckBox.setEnabled(false);
                jWindow.setState(Frame.ICONIFIED);
                executarScreenshoter();
            });
            
            fotoTelaInteiraCheckBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    screenShotFullWindow = fotoTelaInteiraCheckBox.isSelected();
                }
            });
            
            stopButton.addActionListener((ActionEvent ae) -> {
                takeShotButton.setEnabled(true);
                stopButton.setEnabled(false);
                fotoTelaInteiraCheckBox.setEnabled(true);
                pararScreenshoter();
            });
            
            gridPainel.add(takeShotButton);
            gridPainel.add(stopButton);
            gridPainel.add(fotoTelaInteiraCheckBox);

            borderPainel.add(gridPainel, BorderLayout.SOUTH);
            add(borderPainel);

            gridPainel.setOpaque(false);
            borderPainel.setOpaque(false);
            setOpaque(false);
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
       JFrame.setDefaultLookAndFeelDecorated(true);
       SwingUtilities.invokeLater(() ->  {
            new ScreenshoterScreen();
       });
    }
    
    private void executarScreenshoter() {
        screenshoterThread = new Thread() {
            @Override
            public void run() {
                try {
                    Screenshoter screenshoter = obterScreenshoter();
                    
                    screenshoter.continuosCapture();
                } catch (InterruptedException interruptedException) {
                    System.out.println(interruptedException.getMessage());
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
            }
         };
         screenshoterThread.start();
    }
    
    private void pararScreenshoter() {
        screenshoterThread.interrupt();
    }
    
    private Screenshoter obterScreenshoter() throws AWTException {
        if (!screenShotFullWindow) {
            Point point = obterPosicao();
            Dimension dimension = obterDimensao();

            return new Screenshoter(1, point, dimension);
        }
        
        return new Screenshoter(1);
    }
    
    private Point obterPosicao() {
        int x = jWindow.getX();
        int y = jWindow.getY();
        return new Point(x, y);
    }
    
    private Dimension obterDimensao() {
        int width = jWindow.getWidth();
        int height = jWindow.getHeight();
        return new Dimension(width, height);
    }
}