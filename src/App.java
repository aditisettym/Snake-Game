import javax.swing.*;


public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 600;
        int boardHeight = boardWidth;

        JFrame frame = new JFrame("Snake");
        frame.setVisible(true);
        frame.setSize(boardWidth,boardHeight);
        frame.setLocationRelativeTo(null);//open up the window at the center of the screen
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //terminates the program on clicking on X

        //creating a Jpanel to draw our game so for that we will create a new class
        SnakeGame snakeGame = new SnakeGame(boardWidth, boardHeight);
        frame.add(snakeGame);
        //place jpanel inside the frame with proper dimesions else includinf the tittle bar it was 600*600
        frame.pack();
        //snakeGame will be the one listening for the key presses
        snakeGame.requestFocus();

    }
}
