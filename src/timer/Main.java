package timer;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                ColorChangingApp app = new ColorChangingApp();
                String[] options = {"Settings", "Close"};

                int x = JOptionPane.showOptionDialog(null, "Choose option:", "Color changing options", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                if (x == 0) {
                    app.setSize(new Dimension(600,600));
                    app.setVisible(true);
                } else {
                    if (app.isVisible()) {
                        app.dispose();
                    }
                }
            }
        });

    }

}
