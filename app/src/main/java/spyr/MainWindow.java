package spyr;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.*;

public class MainWindow {
    private JMenuBar menuBar;
    private JPanel panelMain;
    private JTextArea songInfoGoesHereTextArea;
    private JTextField songField;
    private JButton addSongButton;
    private JSlider slider1;
    private JList list1;
    private JButton pauseButton;
    private JTable recentSongTable;
    private JTree tree1;
    DefaultListModel listModel = new DefaultListModel();
    SongManager songManager;
    public void setPlaying() {
        pauseButton.setText("Pause");
    }

    public MainWindow() {
        songManager = new SongManager(AudioQuality.HIGH);
        menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        $$$setupUI$$$();
        addSongButton.putClientProperty("JButton.buttonType", "square");
        menuBar.add(fileMenu);
        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SettingsWindow settingsWindow = new SettingsWindow();
                settingsWindow.setup();
            }
        });
        fileMenu.add(settingsMenuItem);
        // actionlisteners n' timers
        addSongButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String songQuery = songField.getText();
                if (SongManager.isYoutubeURL(songQuery)) {
                    songManager.addSongFromURL(songField.getText());
                    songField.setText("");
                    listModel.add(listModel.size(), songManager.songTitleList.get(songManager.songTitleList.size() - 1));
                } else {
                    JOptionPane.showMessageDialog(panelMain, "Song must be a YouTube URL!");
                }

            }
        });
        list1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (!songManager.songTitleList.isEmpty()) {
                    App.audioPlayer.start(songManager.getSongUrl(list1.getSelectedIndex()));
                }
            }
        });
        // can these two be merged?
        slider1.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                App.audioPlayer.setPosition((float) slider1.getValue() / 100);
            }
        });
        slider1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                App.audioPlayer.setPosition((float) slider1.getValue() / 100);
            }
        });
        Timer sliderTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO: do this from AudioPlayer rather than polling it every 200ms
                slider1.setValue(App.audioPlayer.getPercentage());
            }
        });
        sliderTimer.start();

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (App.audioPlayer.isPlaying()) {
                    App.audioPlayer.pause();
                    pauseButton.setText("Play");
                } else if (pauseButton.getText().equals("Play")) {
                    App.audioPlayer.play();
                    pauseButton.setText("Pause");
                } else {
                    if (!songManager.songTitleList.isEmpty()) {
                        try {
                            App.audioPlayer.start(songManager.songURLList.get(list1.getSelectedIndex()));
                        } catch (IndexOutOfBoundsException ignored) {
                            App.audioPlayer.start(songManager.songURLList.get(0));
                        }
                        pauseButton.setText("Pause");
                    }
                }
            }
        });
        recentSongTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                songManager.addExistingSong(songManager.configManager.getYoutubeId(recentSongTable.getSelectedRow()));
                listModel.add(listModel.size(), songManager.songTitleList.get(listModel.size()));
            }
        });
    }

    public void setTime(int percentage) {
        slider1.setValue(percentage);
    }

    public String getNextSongUrl() {
        list1.setSelectedIndex(songManager.playingIndex + 1);
        return songManager.getNextSongUrl();
    }

    public void setup() {
        JFrame frame = new JFrame("Spyr");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                App.writeOutJson();
                App.audioPlayer.exit();
                System.exit(0);
//
            }
        });
        frame.setContentPane(panelMain);
        frame.setPreferredSize(new Dimension(600, 600));
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(4, 12, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(0, 0, 2, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setViewportView(list1);
        songField = new JTextField();
        songField.setText("");
        panelMain.add(songField, new GridConstraints(2, 0, 1, 10, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        pauseButton = new JButton();
        pauseButton.setText("Start");
        panelMain.add(pauseButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        recentSongTable.setEnabled(true);
        recentSongTable.setFillsViewportHeight(false);
        panelMain.add(recentSongTable, new GridConstraints(1, 6, 1, 6, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(150, 50), null, 0, false));
        addSongButton = new JButton();
        addSongButton.setText("Add song!");
        panelMain.add(addSongButton, new GridConstraints(2, 10, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Recently Listened");
        panelMain.add(label1, new GridConstraints(0, 6, 1, 3, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        slider1 = new JSlider();
        slider1.setValue(0);
        panelMain.add(slider1, new GridConstraints(3, 1, 1, 11, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

    private void createUIComponents() {
        list1 = new JList(listModel);
        recentSongTable = new JTable(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return songManager.configManager.getNumSongs();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                if (columnIndex == 0) {
                    return songManager.configManager.getTitle(rowIndex);
                } else {
                    return songManager.configManager.getTimesListenedTo(rowIndex);
                }
            }
        });
        recentSongTable.getColumnModel().getColumn(1).setMaxWidth(10);
    }
}