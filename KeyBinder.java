import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
/**
 * Write a description of class mainclass here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class KeyBinder extends JPanel
{
    static JFrame frame = new JFrame("Title of JFrame");
    static KeyBinder gameBind = new KeyBinder();
    // static KeyBinder gameBind = new KeyBinder();
    public static void main( ) {
        //         frame.addWindowListener( new WindowAdapter() 
        //             {
        //                 public void windowOpened( WindowEvent e ){
        //                     gameBind.requestFocusInWindow();
        //                     frame.requestFocusInWindow();
        //                 }
        //             });

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.toFront();

        gameBind.setFocusable(true);
        frame.getContentPane().add(gameBind);
        //getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)

        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A,0), "p1Left");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0), "p1Down");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D,0), "p1Right");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_W,0), "p1Up");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(51,0), "p1Shoot");

        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_L,0), "p2Left");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(59,0), "p2Down");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(222,0), "p2Right");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_P,0), "p2Up");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(45,0), "p2Shoot");

        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_G,0), "p3Left");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_H,0), "p3Down");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_J,0), "p3Right");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_Y,0), "p3Up");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(55,0), "p3Shoot");

        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(97,0), "p4Left");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(98,0), "p4Down");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(99,0), "p4Right");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(101,0), "p4Up");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(104,0), "p4Shoot");

        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(38,0), "menuUp");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(40,0), "menuDown");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(10,0), "menuEnter");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(37,0), "replayLeft");
        gameBind.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(39,0), "replayRight");

        Action menuUp = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    try{
                        GameController.menuController(38);
                    }
                    catch(InterruptedException e) {
                    }	
                    //System.out.print("u");
                }
            };
        Action menuDown = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    try{
                        GameController.menuController(40);
                    }
                    catch(InterruptedException e) {
                    }	
                    //System.out.print("l");
                }
            };
        Action menuEnter = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    try{
                        GameController.menuEnter();
                    }
                    catch(InterruptedException e) {
                    }	
                    catch(IOException e) {
                    }
                    //System.out.print("d");
                }
            };

        Action replayLeft = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ReplayManager.slower();
                    //System.out.print("l");
                }
            };
        Action replayRight = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    ReplayManager.faster();
                    //System.out.print("d");
                }
            };

        Action p1Up = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(0).up();
                    //System.out.print("u");
                }
            };
        Action p1Left = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(0).left();
                    //System.out.print("l");
                }
            };
        Action p1Down = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(0).down();
                    //System.out.print("d");
                }
            };
        Action p1Right = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(0).right();
                    //System.out.print("r");
                }
            };
        Action p1Shoot = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(0).shoot();
                    //System.out.print("s");
                }
            };

        Action p2Up = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(1).up();
                    //System.out.print("u");
                }
            };
        Action p2Left = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(1).left();
                    //System.out.print("l");
                }
            };
        Action p2Down = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(1).down();
                    //System.out.print("d");
                }
            };
        Action p2Right = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(1).right();
                    //System.out.print("r");
                }
            };
        Action p2Shoot = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(1).shoot();
                    //System.out.print("s");
                }
            };

        Action p3Up = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(2).up();
                    //System.out.print("u");
                }
            };
        Action p3Left = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(2).left();
                    //System.out.print("l");
                }
            };
        Action p3Down = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(2).down();
                    //System.out.print("d");
                }
            };
        Action p3Right = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(2).right();
                    //System.out.print("r");
                }
            };
        Action p3Shoot = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(2).shoot();
                    //System.out.print("s");
                }
            };

        Action p4Up = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(3).up();
                    //System.out.print("u");
                }
            };
        Action p4Left = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(3).left();
                    //System.out.print("l");
                }
            };
        Action p4Down = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(3).down();
                    //System.out.print("d");
                }
            };
        Action p4Right = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(3).right();
                    //System.out.print("r");
                }
            };
        Action p4Shoot = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    Map.getPlayer(3).shoot();
                    //System.out.print("s");
                }
            };

        gameBind.getActionMap().put("p1Up", p1Up);
        gameBind.getActionMap().put("p1Down", p1Down);
        gameBind.getActionMap().put("p1Left", p1Left);
        gameBind.getActionMap().put("p1Right", p1Right);
        gameBind.getActionMap().put("p1Shoot", p1Shoot);

        gameBind.getActionMap().put("p2Up", p2Up);
        gameBind.getActionMap().put("p2Down", p2Down);
        gameBind.getActionMap().put("p2Left", p2Left);
        gameBind.getActionMap().put("p2Right", p2Right);
        gameBind.getActionMap().put("p2Shoot", p2Shoot);

        gameBind.getActionMap().put("p3Up", p3Up);
        gameBind.getActionMap().put("p3Down", p3Down);
        gameBind.getActionMap().put("p3Left", p3Left);
        gameBind.getActionMap().put("p3Right", p3Right);
        gameBind.getActionMap().put("p3Shoot", p3Shoot);

        gameBind.getActionMap().put("p4Up", p4Up);
        gameBind.getActionMap().put("p4Down", p4Down);
        gameBind.getActionMap().put("p4Left", p4Left);
        gameBind.getActionMap().put("p4Right", p4Right);
        gameBind.getActionMap().put("p4Shoot", p4Shoot);

        gameBind.getActionMap().put("menuUp", menuUp);
        gameBind.getActionMap().put("menuDown", menuDown);
        gameBind.getActionMap().put("menuEnter", menuEnter);
        gameBind.getActionMap().put("replayLeft", replayLeft);
        gameBind.getActionMap().put("replayRight", replayRight);

        frame.requestFocusInWindow();
        gameBind.requestFocusInWindow();

    }

    public static void getFocus() throws AWTException {
        Robot bot = new Robot();
        int mask = InputEvent.BUTTON1_DOWN_MASK;
        bot.mouseMove(2, 2);           
        bot.mousePress(mask);     
        bot.mouseRelease(mask);
    }

    public static void bringToFront() {
        frame.setAlwaysOnTop(true);
        frame.setFocusable(true);
        frame.requestFocus();
        frame.requestFocusInWindow();
        gameBind.setFocusable(true);
        gameBind.setLayout(new BorderLayout());
        gameBind.requestFocusInWindow();
        gameBind.requestFocus();
        frame.toFront();
        //gameBind.toFront();        
    }

    public static void closeFrame() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        frame.getContentPane().remove(gameBind);
        //frame.dispose();
    }

}
