import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Stack;

public class Main extends JFrame {
    private int level = 0;
    private Board board;
    private int nOfMovements;

    Main(ArrayList<StringBuilder> levelStrings, ArrayList<Integer[]> boardSizes, int inputLevelFile) {
        board = new Board(levelStrings.get(level), boardSizes.get(level));
        setSize(900, 800);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setTitle("Sokoban");


        JPanel jboard = new Canvas();
        add(jboard, "Center");
        jboard.requestFocus();
        jboard.setFocusable(true);
        jboard.setBackground(Color.BLACK);

        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setBackground(Color.LIGHT_GRAY);
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTH;
        toolBarPanel.setLayout(gbl);
        add(toolBarPanel, "East");


        //FIRST "LEVEL" TEXT

        JLabel l1 = new JLabel("       LEVEL ");
        l1.setPreferredSize(new Dimension(100, 30));
        l1.setForeground(Color.YELLOW);
        gbc.gridx = 2;
        gbc.gridy = 1;
        toolBarPanel.add(l1, gbc);

        //SECOND "LEVEL" LABEL

        JLabel l2 = new JLabel("           " + (inputLevelFile+ 1));
        l2.setPreferredSize(new Dimension(100, 30));
        l2.setOpaque(true);
        l2.setBackground(Color.CYAN);
        gbc.gridx = 2;
        gbc.gridy = 2;
        toolBarPanel.add(l2, gbc);

        //THIRD "PUZZLE" TEXT

        JLabel p1 = new JLabel("      PUZZLE ");
        p1.setPreferredSize(new Dimension(100, 30));
        p1.setForeground(Color.YELLOW);
        gbc.gridx = 2;
        gbc.gridy = 3;
        toolBarPanel.add(p1, gbc);

        //FORTH "PUZZLE" LABEL

        JLabel p2 = new JLabel("           " + level);
        p2.setPreferredSize(new Dimension(100, 30));
        p2.setBackground(Color.CYAN);
        p2.setOpaque(true);
        gbc.gridx = 2;
        gbc.gridy = 4;
        toolBarPanel.add(p2, gbc);


        //FIFTH "PREVIOUS" BUTTON

        JButton previousButton = new JButton();
        gbc.gridx = 2;
        gbc.gridy = 5;
        previousButton.addActionListener((e) -> {
            if ((level < levelStrings.size() - 1) && level > 0) {
                level--;
                board = new Board(levelStrings.get(level), boardSizes.get(level));
                p2.setText("           " + (level + 1));
                nOfMovements = 0;
                jboard.repaint();
            }
            jboard.requestFocus();
        });
        previousButton.setText("<<");
        toolBarPanel.add(previousButton, gbc);

        //SIXTH "NEXT" BUTTON

        JButton nextButton = new JButton();
        gbc.gridx = 2;
        gbc.gridy = 6;
        nextButton.addActionListener((e) -> {
            if ((level < levelStrings.size() - 1) && level >= 0) {
                level++;
                board = new Board(levelStrings.get(level), boardSizes.get(level));
                p2.setText("           " + (level + 1));
                nOfMovements = 0;
                jboard.repaint();
            }
            jboard.requestFocus();
        });
        nextButton.setText(">>");
        toolBarPanel.add(nextButton, gbc);

        //SEVENTH "MOVES" TEXT

        JLabel m1 = new JLabel("      MOVES");
        gbc.gridx = 2;
        gbc.gridy = 7;
        m1.setPreferredSize(new Dimension(100, 30));
        m1.setForeground(Color.YELLOW);
        toolBarPanel.add(m1, gbc);

        //EIGHTS "MOVES" LABEL

        JLabel m2 = new JLabel("           0");
        gbc.gridx = 2;
        gbc.gridy = 8;
        m2.setPreferredSize(new Dimension(100, 30));
        m2.setOpaque(true);
        m2.setBackground(Color.CYAN);
        toolBarPanel.add(m2, gbc);

        //NINTH "RESET" BUTTON

        JButton resetButton = new JButton("RESET(ESC)");
        gbc.gridx = 2;
        gbc.gridy = 9;
        resetButton.addActionListener((e) -> {
            board = new Board(levelStrings.get(level), boardSizes.get(level));
            nOfMovements = 0;
            jboard.repaint();
            jboard.requestFocus();
        });
        toolBarPanel.add(resetButton, gbc);

        //TENTH "UNDO" BUTTON

        JButton undoButton = new JButton("UNDO(1)");
        gbc.gridx = 2;
        gbc.gridy = 10;
        undoButton.addActionListener((e) -> {
            board.undo();
            jboard.repaint();
            jboard.requestFocus();
        });
        toolBarPanel.add(undoButton, gbc);

        //ELEVENTH "REDO" BUTTON

        JButton redoButton = new JButton("REDO(2)");
        gbc.gridx = 2;
        gbc.gridy = 11;
        redoButton.addActionListener((e) -> {
            board.redo();
            jboard.repaint();
            jboard.requestFocus();
        });
        toolBarPanel.add(redoButton, gbc);

        //KEY LISTENER

        JFrame thisFrame = this;
        jboard.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    board.getPlayer().moveUp();
                    nOfMovements++;
                    board.setMoveHistory();
                    jboard.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    board.getPlayer().moveDown();
                    nOfMovements++;
                    board.setMoveHistory();
                    jboard.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    board.getPlayer().moveRight();
                    nOfMovements++;
                    board.setMoveHistory();
                    jboard.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    board.getPlayer().moveLeft();
                    nOfMovements++;
                    board.setMoveHistory();
                    jboard.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    board = new Board(levelStrings.get(level), boardSizes.get(level));
                    nOfMovements = 0;
                    jboard.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_1) {
                    board.undo();
                    jboard.repaint();
                } else if (e.getKeyCode() == KeyEvent.VK_2) {
                    board.redo();
                    jboard.repaint();
                }
                m2.setText("           " + nOfMovements);
                if (board.getnOfBoxSlots() == board.getnOfSucceedBox()) {

                    int res = JOptionPane.showOptionDialog(thisFrame, "Congrats! You have finished this puzzle. Go to next puzzle: ", "Confiramtion", JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE, null, null, null);
                    if (res == 0) {
                        if ((level < levelStrings.size() - 1) && level >= 0) {
                            level++;
                            board = new Board(levelStrings.get(level), boardSizes.get(level));
                            p2.setText("           " + (level + 1));
                            setnOfMovements(0);
                            jboard.repaint();
                        }
                    }
                }
            }
        });
    }

    //MAIN METHOD

    public static void main(String[] args) {
        int inputLevelFile = 2;

        File levelsDir = new File("levels");
        File[] levelFiles = levelsDir.listFiles();
        if (levelFiles == null) {
            JOptionPane.showInputDialog("The file was not found.");
            System.exit(1);
        }
        // TODO not handled

        try {
            inputLevelFile = Integer.parseInt(JOptionPane.showInputDialog("LevelFile: "));
            inputLevelFile--;

            if(inputLevelFile >= 7 || inputLevelFile < 0){
                JOptionPane.showMessageDialog(null,"File Level should be from 1 to 7");
                System.exit(-1);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null,"File Level should be INTEGER from 1 to 7");
            System.exit(-1);
        }



        ArrayList<StringBuilder> levelStrings = new ArrayList<>();
        levelStrings.add(new StringBuilder());
        ArrayList<Integer[]> levelSizes = new ArrayList<>();
        levelSizes.add(new Integer[]{0, 0});

        try {
            Files.lines(levelFiles[inputLevelFile].toPath()).forEachOrdered(line -> {
                String trimmedLine = line.trim();

                if (!trimmedLine.startsWith(";") && !trimmedLine.isEmpty()) {
                    int lastIndex = levelStrings.size() - 1;
                    levelStrings.get(lastIndex).append(line).append('\n');
                    levelSizes.get(lastIndex)[1]++;

                    if (trimmedLine.length() > levelSizes.get(lastIndex)[0]) {
                        levelSizes.get(lastIndex)[0] = trimmedLine.length();
                    }
                } else {

                    int lastIndex = levelStrings.size() - 1;
                    if (!levelStrings.get(lastIndex).toString().isEmpty()) {
                        levelStrings.add(new StringBuilder());
                        levelSizes.add(new Integer[]{0, 0});
                    }
                }
            });
            levelStrings.remove(levelSizes.size() - 1);
            levelSizes.remove(levelSizes.size() - 1);
        } catch (IOException e) {
            System.err.println("The files were not found.");
        }
        new Main(levelStrings, levelSizes, inputLevelFile).setVisible(true);
    }

    // CANVAS PAINTING

    class Canvas extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int shiftToCenterX = (getWidth() - (board.getWidth() * board.getCellSize())) / 2;
            int shiftToCenterY = (getHeight() - (board.getHeight() * board.getCellSize())) / 2;

            for (int y = 0; y < board.getHeight(); ++y) {
                for (int x = 0; x < board.getWidth(); ++x) {
                    if (board.getElements()[y][x] instanceof Wall) {
                        Image img1 = Toolkit.getDefaultToolkit().getImage("data/Ground.png");
                        g.drawImage(img1, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);
                        Image img = Toolkit.getDefaultToolkit().getImage("data/Wall.png");
                        g.drawImage(img, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);

                    } else if (board.getElements()[y][x] instanceof Area && ((Area) (board.getElements()[y][x])).isInsideBoard()) {
                        Image img = Toolkit.getDefaultToolkit().getImage("data/Ground.png");
                        g.drawImage(img, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);
                        if(((Area)(board.getElements()[y][x])).isBoxSlot()){
                            Image imgIcon = new ImageIcon("data/Goal.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize() - 8, board.getCellSize() - 8, this);
                        }

                    } else if (board.getElements()[y][x] instanceof Player) {
                        Image img = Toolkit.getDefaultToolkit().getImage("data/Ground.png");
                        g.drawImage(img, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);
                        if(board.getPlayer().getPosition().equals("U")) {
                            Image imgIcon = new ImageIcon("data/RobotU.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize() - 10, board.getCellSize(), this);
                        }else if(board.getPlayer().getPosition().equals("D")) {
                            Image imgIcon = new ImageIcon("data/Robot.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize() - 10, board.getCellSize(), this);
                        }else if(board.getPlayer().getPosition().equals("R")) {
                            Image imgIcon = new ImageIcon("data/RobotR.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize() - 10, board.getCellSize(), this);
                        }else if(board.getPlayer().getPosition().equals("L")) {
                            Image imgIcon = new ImageIcon("data/RobotL.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize() - 10, board.getCellSize(), this);
                        }

                    } else if (board.getElements()[y][x] instanceof Box) {
                        Image img = Toolkit.getDefaultToolkit().getImage("data/Ground.png");
                        g.drawImage(img, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);
                        if(((Box)(board.getElements()[y][x])).isInGoal()){
                            Image imgIcon = new ImageIcon("data/BoxRed.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);
                        }else {
                            Image imgIcon = new ImageIcon("data/BoxBlue.png").getImage();
                            g.drawImage(imgIcon, shiftToCenterX + x * board.getCellSize(), shiftToCenterY + y * board.getCellSize(), board.getCellSize(), board.getCellSize(), this);
                        }
                    }
                }
            }
        }
    }

    public void setnOfMovements(int nOfMovements) {
        this.nOfMovements = nOfMovements;
    }
}
