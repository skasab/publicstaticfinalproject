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
public class LevelKey extends JPanel
{
    static JFrame frame = new JFrame("Title of JFrame");
    static LevelKey classInstance = new LevelKey();
    public static void main( ) {

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setLocationRelativeTo(null);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        frame.setFocusable(true);
        frame.toFront();

        classInstance.setFocusable(true);
        frame.getContentPane().add(classInstance);

        classInstance.getInputMap().put(KeyStroke.getKeyStroke(49,0), "select1");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(50,0), "select2");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(51,0), "select3");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(52,0), "select4");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(53,0), "select5");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(54,0), "select6");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(55,0), "select7");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(56,0), "select8");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(57,0), "select9");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(48,0), "select0");

        classInstance.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_S,0), "s");

        classInstance.getInputMap().put(KeyStroke.getKeyStroke(38,0), "up");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(40,0), "down");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(10,0), "enter");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(8,0), "back");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(37,0), "left");
        classInstance.getInputMap().put(KeyStroke.getKeyStroke(39,0), "right");

        Action up = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.up();
                }
            };
        Action down = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.down();
                }
            };
        Action enter = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.add();
                }
            };
        Action back = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.remove();
                }
            };

        Action left = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.left();
                }
            };

        Action right = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.right();
                }
            };
        Action select1 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(1);
                }
            };
        Action select2 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(2);
                }
            };
        Action select3 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(3);
                }
            };
        Action select4 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(4);
                }
            };
        Action select5 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(5);
                }
            };
        Action select6 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(6);
                }
            };
        Action select7 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(7);
                }
            };
        Action select8 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(8);
                }
            };
        Action select9 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(9);
                }
            };
        Action select0 = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.select(0);
                }
            };

        Action save = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.save();
                }
            };

        Action reflect = new AbstractAction(){
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    MapEditor.reflect();
                }
            };

        classInstance.getActionMap().put("up", up);
        classInstance.getActionMap().put("down", down);
        classInstance.getActionMap().put("enter", enter);
        classInstance.getActionMap().put("back", back);
        classInstance.getActionMap().put("left", left);
        classInstance.getActionMap().put("right", right);

        classInstance.getActionMap().put("s", save);
        classInstance.getActionMap().put("r", reflect);

        classInstance.getActionMap().put("select0", select0);
        classInstance.getActionMap().put("select1", select1);
        classInstance.getActionMap().put("select2", select2);
        classInstance.getActionMap().put("select3", select3);
        classInstance.getActionMap().put("select4", select4);
        classInstance.getActionMap().put("select5", select5);
        classInstance.getActionMap().put("select6", select6);
        classInstance.getActionMap().put("select7", select7);
        classInstance.getActionMap().put("select8", select8);
        classInstance.getActionMap().put("select9", select9);
        //classInstance.getActionMap().put("select-", select-);

        frame.requestFocusInWindow();
        classInstance.requestFocusInWindow();

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
        classInstance.setFocusable(true);
        classInstance.setLayout(new BorderLayout());
        classInstance.requestFocusInWindow();
        classInstance.requestFocus();
        frame.toFront();
        //classInstance.toFront();        
    }

    public static void closeFrame() {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        //frame.dispose();
    }
}
