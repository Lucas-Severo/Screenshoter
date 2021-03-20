package screenshoter;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ScreenshoterScreen {

    private final JFrame jWindow;
    private final Screenshoter screenshoter;
    private Thread screenshoterThread;
    
    public ScreenshoterScreen() throws AWTException {
        screenshoter = new Screenshoter(1);
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
            stopButton.setEnabled(false);
            
            JButton takeShotButton = new JButton("Capturar agora");
            takeShotButton.addActionListener((ActionEvent ae) -> {
                takeShotButton.setEnabled(false);
                stopButton.setEnabled(true);
                jWindow.setState(Frame.ICONIFIED);
                executarScreenshoter();
            });
            
            stopButton.addActionListener((ActionEvent ae) -> {
                takeShotButton.setEnabled(true);
                stopButton.setEnabled(false);
                pararScreenshoter();
            });
            
            gridPainel.add(takeShotButton);
            gridPainel.add(stopButton);

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
           try {
               new ScreenshoterScreen();
           } catch (AWTException ex) {
               Logger.getLogger(ScreenshoterScreen.class.getName()).log(Level.SEVERE, null, ex);
           }
       });
    }
    
    private void executarScreenshoter() {
        screenshoterThread = new Thread() {
            @Override
            public void run() {
                try {
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
}