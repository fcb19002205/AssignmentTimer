package timer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ColorChangingApp extends JFrame {

    private final JTextField timeField;
    private final JTextField timeField2;
    private final JRadioButton onTimeRadioButton;
    private final JRadioButton countdownRadioButton;
    private final JButton chooseColorButton;
    private final JLabel colorLabel;
    private final JComboBox<Integer> speedComboBox;
    private JButton startCountdownButton;
    private JButton stopButton;
    private Timer countdownTimer;
    private ColorChangingWindow colorChangingWindow;

    public ColorChangingApp() {

        timeField = new JTextField(10);
        timeField2 = new JTextField(10);
        onTimeRadioButton = new JRadioButton("On time", true);
        countdownRadioButton = new JRadioButton("Countdown", false);
        chooseColorButton = new JButton("Choose color:");
        colorLabel = new JLabel();
        speedComboBox = new JComboBox<>(new Integer[]{1000, 2000, 3000, 4000, 5000});
        startCountdownButton = new JButton("Start countdown");
        stopButton = new JButton("Stop");
        colorChangingWindow = new ColorChangingWindow((int) speedComboBox.getSelectedItem(), colorLabel.getForeground());

        setTitle("Color changing app");
        getContentPane().setBackground(Color.lightGray);
        getContentPane().setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        setLocation(dim.width / 2 - this.getWidth() / 2, dim.height / 2 - this.getHeight() - 2);

        onTimeRadioButton.setBounds(10, 10, 290, 30);
        countdownRadioButton.setBounds(10, 50, 290, 30);
        timeField.setBounds(300, 10, 270, 30);
        timeField2.setBounds(300, 50, 270, 30);
        ButtonGroup buttongrp = new ButtonGroup();
        buttongrp.add(onTimeRadioButton);
        buttongrp.add(countdownRadioButton);

        add(onTimeRadioButton);
        add(countdownRadioButton);
        add(timeField);
        add(timeField2);

        chooseColorButton.setBounds(150, 160, 250, 50);
        colorLabel.setBounds(420, 160, 250, 50);
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        ActionListener timerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date date = new Date();
                String time = timeFormat.format(date);
                colorLabel.setText(time);
            }
        };
        Timer timer = new Timer(1000, timerListener);
        timer.setInitialDelay(0);
        timer.start();
        colorLabel.setFont(new Font("Verdana", Font.PLAIN, 18));

        chooseColorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(ColorChangingApp.this, "Choose color", null);
                if (selectedColor != null) {
                    colorLabel.setForeground(selectedColor);
                }
            }
        });

        add(chooseColorButton);
        add(colorLabel);

        JLabel selectSpeed = new JLabel("Speed: ");
        selectSpeed.setBounds(180, 300, 50, 30);

        speedComboBox.setBounds(250, 300, 80, 30);
        speedComboBox.setSelectedIndex(2);


        add(speedComboBox);
        add(selectSpeed);

        startCountdownButton = new JButton("Start countdown");
        startCountdownButton.setBounds(100, 450, 250, 50);

        stopButton = new JButton("Stop");
        stopButton.setBounds(400, 450, 100, 50);

        add(startCountdownButton);
        add(stopButton);

        startCountdownButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startCountdown();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopCountdown();
            }
        });

        pack();
        setLocationRelativeTo(null);

    }


    private void startCountdown() {
        timeField.setEnabled(false);
        timeField2.setEnabled(false);
        onTimeRadioButton.setEnabled(false);
        countdownRadioButton.setEnabled(false);
        chooseColorButton.setEnabled(false);
        speedComboBox.setEnabled(false);
        startCountdownButton.setEnabled(false);
        stopButton.setEnabled(true);

        if (onTimeRadioButton.isSelected()) {
            startOnTimeCountdown();
        } else if (countdownRadioButton.isSelected()) {
            int countdownDuration = Integer.parseInt(timeField2.getText());

            countdownTimer = new Timer(countdownDuration * 1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showColorChangingWindow();
                }
            });
            countdownTimer.setRepeats(false);
            countdownTimer.start();
        }
    }

    private void startOnTimeCountdown() {
        String targetTime = timeField.getText();
        long delay = calculateDelayFromTime(targetTime);

        if (delay > 0) {
            colorChangingWindow = new ColorChangingWindow((int) speedComboBox.getSelectedItem(), colorLabel.getForeground());
            countdownTimer = new Timer(0, null);
            countdownTimer.setRepeats(false);
            countdownTimer.setInitialDelay((int) delay);
            countdownTimer.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showColorChangingWindow();
                }
            });
            countdownTimer.start();
        }
    }

    private void showColorChangingWindow() {
        int speed = (int) speedComboBox.getSelectedItem();
        Color selectedColor = colorLabel.getForeground();
        colorChangingWindow = new ColorChangingWindow(speed, selectedColor);
        colorChangingWindow.setVisible(true);
        colorChangingWindow.setSize(600, 600);
        int windowWidth = colorChangingWindow.getWidth();
        int windowHeight = colorChangingWindow.getHeight();
        int colorChangingWindowX = getX() + getWidth(); // X-koordinata prozora ColorChangingApp + širina prozora ColorChangingApp
        int colorChangingWindowY = getY(); // Y-koordinata prozora ColorChangingApp
        colorChangingWindow.setBounds(colorChangingWindowX, colorChangingWindowY, windowWidth, windowHeight);
        colorChangingWindow.setLayout(new FlowLayout());
        colorChangingWindow.setSelectedColor(colorLabel.getForeground());
        colorChangingWindow.startTimer();


    }

    private long calculateDelayFromTime(String timeString) {
        long delay = 0;
        try {
            String[] parts = timeString.split(":");
            if (parts.length == 3) {
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);
                int seconds = Integer.parseInt(parts[2]);
                long currentTime = System.currentTimeMillis();
                Calendar targetCalendar = Calendar.getInstance();
                targetCalendar.set(Calendar.HOUR_OF_DAY, hours);
                targetCalendar.set(Calendar.MINUTE, minutes);
                targetCalendar.set(Calendar.SECOND, seconds);
                long targetTime = targetCalendar.getTimeInMillis();
                if (targetTime < currentTime) {
                    targetCalendar.add(Calendar.DAY_OF_MONTH, 1);
                    targetTime = targetCalendar.getTimeInMillis();
                }
                delay = targetTime - currentTime;
            } else {
                throw new IllegalArgumentException("Invalid time format.");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return delay;
    }

    private void stopCountdown() {
        timeField.setEnabled(true);
        timeField2.setEnabled(true);
        onTimeRadioButton.setEnabled(true);
        countdownRadioButton.setEnabled(true);
        chooseColorButton.setEnabled(true);
        speedComboBox.setEnabled(true);
        startCountdownButton.setEnabled(true);
        stopButton.setEnabled(false);

        if (countdownTimer != null && countdownTimer.isRunning()) {
            countdownTimer.stop();
        }

        if (colorChangingWindow != null) {
            colorChangingWindow.dispose();
        }
    }

    private static class ColorChangingWindow extends JFrame {
        private final Timer timer;
        private boolean isColorSelected = false;

        public void setSelectedColor(Color selectedColor) {
            isColorSelected = true;
            getContentPane().setBackground(selectedColor);
            repaint();
        }

        public ColorChangingWindow(int speed, Color selectedColor) {
            setTitle("Color changing window");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setResizable(false);

            timer = new Timer(speed, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (isColorSelected) {
                        getContentPane().setBackground(Color.WHITE);
                        isColorSelected = false;
                    } else {
                        getContentPane().setBackground(selectedColor);
                        isColorSelected = true;
                    }
                    repaint();
                }
            });
            timer.setRepeats(true);
            pack();

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            int screenWidth = screenSize.width;
            int screenHeight = screenSize.height;
            int appWidth = getWidth();
            int appHeight = getHeight();
            int x = (screenWidth - appWidth) / 2;
            int y = (screenHeight - appHeight) / 2;
            setLocationRelativeTo(null);
            setLocation(x, y);
        }

        public void startTimer() {
            timer.start();
        }

    }


}
