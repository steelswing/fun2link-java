/*
 * Ну вы же понимаете, что код здесь только мой?
 * Well, you do understand that the code here is only mine?
 */

/**
 * File: JMEThread.java
 * Created on 2025 Nov 12, 15:28:42
 *
 * @author LWJGL2
 */
public class JMEThread implements Runnable {
    
    protected Thread realThread;

    public JMEThread() {
        realThread = new Thread(this);
    }

    @Override
    public void run() {
    }

    public void start() {
        realThread.start();
    }

    public void stop() {
        realThread.interrupt();
    }
}
