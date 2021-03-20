package screenshoter;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDateTime;
import javax.imageio.ImageIO;

public class Screenshoter {

    private final int time;
    private final Rectangle screenRect;
    private boolean stop = false;
    
    public Screenshoter(int time) throws AWTException {
        this.time = time * 1000;
        screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
    }
    
    public void capture() throws Exception {
        String prefix = LocalDateTime.now().toString();
        File imageFile = new File("single-screen" + prefix + ".png");
        BufferedImage screenCapturer = new Robot().createScreenCapture(screenRect);
        ImageIO.write(screenCapturer, "png", imageFile);
    }
    
    public void continuosCapture() throws InterruptedException, Exception {
        while (!stop) {
            this.capture();
            Thread.sleep(this.time);
        }
    }
    
    public static void main(String[] args) throws Exception {
        Screenshoter screenshoter = new Screenshoter(1);
        
        screenshoter.continuosCapture();
    }
    
}
