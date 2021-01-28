import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener { // здесь будет происходить онс действие игры

    // игровые параметры
    private final int SIZE = 320;
    private final int DOT_SIZE = 16; //размер в пикселях одного квадратика(яблочка)
    private final int ALL_DOTS = 300; // кол-во игровых единиц, которых может разместиться на игровом поле

    private Image dot;
    private Image apple;

    private int appleX, appleY; // позиция яблока

    private int[] x = new int[ALL_DOTS]; // массих для хранения положения змейки по х
    private int[] y = new int[ALL_DOTS];

    private int dots;
    private int points = 0;

    // таймер
    private Timer timer;

    //текущее напр-ие движения змейки
    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;
    //проверка на состояние змейки
    private boolean inGame = true;

    public GameField(){
        setBackground(Color.BLACK);
        setBorder(BorderFactory.createLineBorder(Color.BLUE));
        loadImages();
        initGame();
        addKeyListener(new FieldKeyListener());
        // коннектим нажате клавиш с игровым полем
        setFocusable(true);
    }

    public  void loadImages(){
        ImageIcon iconApple = new ImageIcon("hart.png");
        apple = iconApple.getImage();
        ImageIcon iconDot = new ImageIcon("smile.png");
        dot = iconDot.getImage();
    }

    // метод котор инициализирует начало игры
    public void initGame(){
        dots = 3;

        for (int i = 0; i < dots; i++) {
            x[i] = 48 - i * DOT_SIZE; // начальная х позиция на 48 т.к кратно 16, т.е первое звено змейки будет на 48
            y[i] = 48;
        }
        timer = new Timer(270, this);
        timer.start();
        createApple();
    }

    public void createApple(){
        appleX = new Random().nextInt(19) * DOT_SIZE;
        appleY = new Random().nextInt(19) * DOT_SIZE;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (inGame){
            String str = Integer.toString(points);
            g.setColor(Color.CYAN);
            g.drawString(str, 10, SIZE/2);

            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        } else {
            String str1 = "Game Over";
            g.setColor(Color.CYAN);
            g.drawString(str1, 130, SIZE/2);
            String str2 = "your score = " + points;
            g.setColor(Color.CYAN);
            g.drawString(str2, 130, (int) (SIZE/1.85));
        }
    }

    // логическая перерисовка точек(сдвиг влево, право и т.д)
    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        //если направление лево, то берем первую позицию и перемещаем ее на (х - DOT_SIZE)
        if (left){
            x[0] -= DOT_SIZE;
        }
        if (right){
            x[0] += DOT_SIZE;
        }
        // если меняем позицию на вверх или вниз, то меняем y
        if (up){
            y[0] -= DOT_SIZE;
        }
        if (down){
            y[0] += DOT_SIZE;
        }
    }

    //если змея съедает яблоко, то увеличиваем размер змеи и создаем новое яблоко
    private void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            dots++;
            points++;
            createApple();
        }
    }

    //проверка на столкновения(змея может столкнуться сама с собой если она > 4 ячеек или с границами игровой области)
    private void checkCollisions() {
        for (int i = dots; i > 0 ; i--) {
            if (i>4 && x[0] == x[i] && y[0] == y[i]){
                inGame = false;
            }
        }
        // если голова змеи больше размера поля, то игра закончена
        if(x[0] > SIZE){
            inGame = false;
        }
        if(x[0] < 0){
            inGame = false;
        }
        if(y[0] > SIZE){
            inGame = false;
        }
        if(y[0] < 0){
            inGame = false;
        }
    }

    // он будет вызываться каждые 250 млс
    @Override
    public void actionPerformed(ActionEvent e) {
        // здесь проверяем столкновение с рамками игрового поля и встретила ли змейка еду
        // плюс поле нужно перерисовывать
        if (inGame){
            checkApple();
            checkCollisions();
            move();
        }
        repaint();
    }

    class FieldKeyListener extends KeyAdapter {
    // изменение напр-я движения
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && ! right){
                left = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_RIGHT && ! left){
                right = true;
                up = false;
                down = false;
            }

            if (key == KeyEvent.VK_UP && ! down){
                right = false;
                up = true;
                left = false;
            }

            if (key == KeyEvent.VK_DOWN && ! up){
                right = false;
                left = false;
                down = true;
            }
        }
    }


}
