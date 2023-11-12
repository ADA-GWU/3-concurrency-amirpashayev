import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main extends JFrame
{
    private BufferedImage image;
    private BufferedImage resultImage;
    private BufferedImage displayImage;
    private final int MAX_WIDTH = 600;
    private double scaleFactor;
    private int squareSize;
    private Graphics2D graphics;


    public Main(String title) throws HeadlessException
    {
        super(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void updateUI()
    {
        SwingUtilities.invokeLater(this::repaint);
    }

    private Color getAverageColor(int startX, int startY)
    {
        int red = 0, green = 0, blue = 0;
        int count = 0;

        for (int x = startX; x < startX + this.squareSize; x++) {
            for (int y = startY; y < startY + this.squareSize; y++) {
                if (x < this.image.getWidth() && y < this.image.getHeight()) {
                    Color pixelColor = new Color(this.image.getRGB(x, y));
                    red += pixelColor.getRed();
                    green += pixelColor.getGreen();
                    blue += pixelColor.getBlue();
                    count++;
                }
            }
        }

        return new Color(red / count, green / count, blue / count);
    }

    private void colorPixelsInsideSquare(Color pixelColor, int startX, int startY)
    {
        for (int x = startX; x < startX + this.squareSize; x++) {
            for (int y = startY; y < startY + this.squareSize; y++) {
                if (x < this.resultImage.getWidth() && y < this.resultImage.getHeight()) {
                    this.resultImage.setRGB(x, y, pixelColor.getRGB());
                    this.displayImage.setRGB((int)(x * this.scaleFactor), (int)(y * this.scaleFactor), pixelColor.getRGB());
                }

                int dx = (int)(x * this.scaleFactor);
                int dy = (int)(y * this.scaleFactor);
                if (dx < this.displayImage.getWidth() && dy < this.displayImage.getHeight()) {
                    this.displayImage.setRGB(dx, dy, pixelColor.getRGB());
                }
            }
        }
    }

    private void processSquare(int startX, int startY)
    {
        Color averageColor = this.getAverageColor(startX, startY);
        this.colorPixelsInsideSquare(averageColor, startX, startY);

        updateUI();
        try {
            Thread.sleep(5);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }


    private void initImage(String filename) throws IOException
    {
        this.image = ImageIO.read(new File(filename));
        this.resultImage = new BufferedImage(this.image.getWidth(), this.image.getHeight(), BufferedImage.TYPE_INT_RGB);

        this.scaleFactor = Math.min(1d, MAX_WIDTH / (double) this.image.getWidth());
        int scaledWidth = (int) (this.image.getWidth() * this.scaleFactor);
        int scaledHeight = (int) (this.image.getHeight() * this.scaleFactor);
        this.displayImage = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_RGB);
        this.graphics = displayImage.createGraphics();
        this.graphics.drawImage(this.image.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH),
        0, 0, this);

        setSize(scaledWidth, scaledHeight);
        setVisible(true);
    }

    private void executeMultiThread()
    {
        int cores = Runtime.getRuntime().availableProcessors();
        int totalRows = image.getHeight();
        int rowsPerThread = totalRows / cores;
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        for (int i = 0; i < cores; i++) {
            final int startRow = i * rowsPerThread;
            final int endRow = (i == cores - 1) ? totalRows : (startRow + rowsPerThread);

            executor.submit(() -> this.processSquaresInImage(startRow, endRow));
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void processSquaresInImage(int startRow, int endRow)
    {
        for (int y = startRow; y < endRow; y += this.squareSize) {
            for (int x = 0; x < this.image.getWidth(); x += this.squareSize) {
                processSquare(x, y);
            }
        }
    }


    public void processImage(String filename, int squareSize, char mode) throws Exception
    {
        this.squareSize = squareSize;
        this.initImage(filename);

        if (mode == 'S') {
            this.processSquaresInImage(0, this.image.getHeight());
        } else if (mode == 'M') {
            this.executeMultiThread();
        }

        graphics.dispose();
        ImageIO.write(resultImage, "jpg", new File("result.jpg"));
    }

    @Override
    public void paint(Graphics g) {
        if (displayImage != null) {
            g.drawImage(displayImage, 0, 0, this);
        } else {
            super.paint(g);
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.out.println("Usage: java Main.java <filename> <squareSize> <S|M>");
            return;
        }
        String filename = args[0];
        int squareSize = Integer.parseInt(args[1]);
        char mode = args[2].charAt(0);
        Main processor = new Main("Image Processor");
        processor.processImage(filename, squareSize, mode);
    }
}
