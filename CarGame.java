//java program to implement car game using JFrames// 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*; 
import java.util.ArrayList; 
import java.util.Random; 
 
public class CarGame extends JPanel implements KeyListener, ActionListener { 
    private static final int WIDTH = 500; 
    private static final int HEIGHT = 500; 
    private static final int LANE_WIDTH = 100; 
    private static final int CAR_WIDTH = 45; // Car width 
    private static final int CAR_HEIGHT = 100; // Car height 
    private static final int VEHICLE_SPEED_INCREMENT = 5; 
 
    private Timer timer; 
    private int playerX = 250; 
    private int playerY = 400; 
    private int speed = 15; 
    private int score = 0; 
    private boolean gameOver = false; 
 
    private Image playerCar; 
    private ArrayList<Vehicle> vehicles; 
    private Random random; 
 
    private int roadOffset = 0; // Variable to simulate the road movement 
 
    public CarGame() { 
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT)); 
        this.setBackground(new Color(76, 208, 56)); 
        this.addKeyListener(this); 
        this.setFocusable(true); 
 
        // Load and scale player car image to fit within the lane width (45px width) 
        playerCar = new ImageIcon("C:\\Users\\kavur\\Desktop\\Screenshot 2024-11-02 
193729.png").getImage(); 
        playerCar = playerCar.getScaledInstance(CAR_WIDTH, CAR_HEIGHT, 
Image.SCALE_SMOOTH); 
 
        vehicles = new ArrayList<>(); 
        random = new Random(); 
 
        // Game timer 
        timer = new Timer(1000 / 60, this); // 60 FPS 
        timer.start(); 
    } 
 
    @Override 
    public void paintComponent(Graphics g) { 
        super.paintComponent(g); 
 
        // Scroll the road effect (move the lanes downward) 
        roadOffset += speed; // Increase roadOffset to scroll downward 
        if (roadOffset >= 100) { // Reset road offset after it has scrolled down past the screen 
            roadOffset = 0; 
        } 
 
        // Draw road 
        g.setColor(Color.WHITE); 
        g.fillRect(100, 0 + roadOffset, 300, HEIGHT);  // Road moves downward with roadOffset 
        g.fillRect(100, 0 + roadOffset - HEIGHT, 300, HEIGHT); // Duplicate road for continuous 
effect 
 
        // Draw edge markers 
        g.setColor(Color.YELLOW); 
        g.fillRect(95, 0 + roadOffset, 10, HEIGHT); // Left edge marker moves downward 
        g.fillRect(395, 0 + roadOffset, 10, HEIGHT); // Right edge marker moves downward 
        g.fillRect(95, 0 + roadOffset - HEIGHT, 10, HEIGHT); // Left edge duplicate 
        g.fillRect(395, 0 + roadOffset - HEIGHT, 10, HEIGHT); // Right edge duplicate 
 
        // Draw lane markers 
        g.setColor(Color.BLACK); 
        for (int i = roadOffset - 50; i < HEIGHT; i += 100) { 
            g.fillRect(150, i, 10, 50); 
            g.fillRect(250, i, 10, 50); 
            g.fillRect(350, i, 10, 50); 
        } 
 
        // Draw player's car 
        g.drawImage(playerCar, playerX - CAR_WIDTH / 2, playerY - CAR_HEIGHT, CAR_WIDTH, 
CAR_HEIGHT, this); 
 
        // Draw vehicles 
        for (Vehicle vehicle : vehicles) { 
            g.drawImage(vehicle.getImage(), vehicle.getX() - CAR_WIDTH / 2, vehicle.getY() - 
CAR_HEIGHT, CAR_WIDTH, CAR_HEIGHT, this); 
        } 
 
        // Display score 
        g.setColor(Color.WHITE); 
        g.setFont(new Font("Arial", Font.PLAIN, 16)); 
        g.drawString("SCORE: " + score, 20, HEIGHT - 20); 
 
        // Game over screen 
        if (gameOver) { 
            g.setColor(Color.RED); 
            g.fillRect(0, 50, WIDTH, 100); 
            g.setColor(Color.WHITE); 
            g.drawString("GAME OVER. Play Again? (Y/N)", WIDTH / 2 - 100, HEIGHT / 2); 
        } 
    } 
 
    @Override 
    public void keyPressed(KeyEvent e) { 
        if (gameOver) return; 
 
        if (e.getKeyCode() == KeyEvent.VK_LEFT && playerX > 150) { 
            playerX -= 100; 
        } 
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && playerX < 350) { 
            playerX += 100; 
        } 
    } 
 
    @Override 
    public void keyReleased(KeyEvent e) {} 
 
    @Override 
    public void keyTyped(KeyEvent e) {} 
 
    @Override 
    public void actionPerformed(ActionEvent e) { 
        if (gameOver) return; 
 
        // Move vehicles 
        for (int i = vehicles.size() - 1; i >= 0; i--) { 
            Vehicle vehicle = vehicles.get(i); 
            vehicle.setY(vehicle.getY() + speed); 
 
            if (vehicle.getY() > HEIGHT) { 
                vehicles.remove(i); 
                score++; 
                if (score % VEHICLE_SPEED_INCREMENT == 0) { 
                    speed++; // Increase speed as score increases 
                } 
            } 
 
            // Check for collision with player 
            if (vehicle.getBounds().intersects(new Rectangle(playerX - CAR_WIDTH / 2, playerY - 
CAR_HEIGHT, CAR_WIDTH, CAR_HEIGHT))) { 
                gameOver = true; 
            } 
        } 
 
        // Add new vehicles, ensuring no more than 1 vehicle on the screen at once 
        if (vehicles.size() < 1) { // Ensures only 1 vehicle can be on the screen at a time 
            int lane = random.nextInt(3);  // Randomly choose a lane (0, 1, 2) 
             
            // Load and scale the vehicle image to fit in the lane 
            Image vehicleImage = new ImageIcon("C:\\Users\\kavur\\Desktop\\Screenshot 2024
11-02 193740.png").getImage(); 
            vehicleImage = vehicleImage.getScaledInstance(CAR_WIDTH, CAR_HEIGHT, 
Image.SCALE_SMOOTH); 
 
            // Add new vehicle to the screen at random lane 
            vehicles.add(new Vehicle(vehicleImage, 150 + lane * 100, -50)); 
        } 
 
        repaint(); 
    } 
 
    // Vehicle class for random vehicles 
    private class Vehicle { 
        private Image image; 
        private int x, y; 
 
        public Vehicle(Image image, int x, int y) { 
            this.image = image; 
            this.x = x; 
            this.y = y; 
        } 
 
        public Image getImage() { 
            return image; 
        } 
 
        public int getX() { 
            return x; 
        } 
 
        public int getY() { 
            return y; 
        } 
 
        public void setY(int y) { 
            this.y = y; 
        } 
 
        public Rectangle getBounds() { 
            return new Rectangle(x - CAR_WIDTH / 2, y - CAR_HEIGHT, CAR_WIDTH, CAR_HEIGHT); 
        } 
    } 
 
    // Reset game after game over 
    private void resetGame() { 
        playerX = 250; 
        playerY = 400; 
        score = 0; 
        speed = 1; 
        gameOver = false; 
        vehicles.clear(); 
        repaint(); 
    } 
 
    public static void main(String[] args) { 
        JFrame frame = new JFrame("Car Game"); 
        CarGame game = new CarGame(); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        frame.getContentPane().add(game); 
        frame.pack(); 
        frame.setVisible(true); 
 
        // Add listener for keyboard input to restart game 
        game.addKeyListener(new KeyAdapter() { 
            @Override 
            public void keyPressed(KeyEvent e) { 
                if (game.gameOver) { 
                    if (e.getKeyCode() == KeyEvent.VK_Y) { 
                        game.resetGame(); 
                    } else if (e.getKeyCode() == KeyEvent.VK_N) { 
                        System.exit(0); 
                    } 
                } 
            } 
        }); 
    } 
} 