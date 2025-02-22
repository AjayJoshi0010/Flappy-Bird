import javax.swing.*;

public class Main {
    public static void main(String[] args) throws Exception{
        int width = 360;
        int height = 640;

        JFrame frame = new JFrame("Flappy Bird");

        frame.setSize(width,height);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        flappyBird flappybird = new flappyBird();
        frame.add(flappybird);
        frame.pack();  // not include the title bar in height
        frame.requestFocus();
        frame.setVisible(true);
    }
}