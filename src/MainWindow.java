import javax.swing.*;

public class MainWindow extends JFrame {

    public MainWindow(){
        add(new GameField());
        setTitle("Snake");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);// крестик для закрытия окна
        setSize(320, 345);
        setLocation(300,300);
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
    }
}
