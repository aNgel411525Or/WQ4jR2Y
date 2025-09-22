// 代码生成时间: 2025-09-22 23:26:32
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class ImageResizer {

    private SparkSession spark;
    private JavaSparkContext jsc;

    public ImageResizer(SparkSession spark) {
        this.spark = spark;
        this.jsc = new JavaSparkContext(spark.sparkContext());
    }

    /**
     * Adjusts the image size of each image in the directory.
     * @param inputPath Path to the directory containing images.
     * @param outputPath Path to the directory where resized images will be saved.
     * @param targetWidth Target width for resized images.
     * @param targetHeight Target height for resized images.
     */
    public void resizeImages(String inputPath, String outputPath, int targetWidth, int targetHeight) {
        List<File> imageFiles = Arrays.asList(
                Files.walk(Paths.get(inputPath))
                        .filter(Files::isRegularFile)
                        .map(Path::toFile)
                        .filter(file -> file.exists() && isImageFile(file))
                        .collect(Collectors.toList())
        );

        JavaRDD<File> imageFilesRDD = jsc.parallelize(imageFiles);

        imageFilesRDD.foreach(file -> {
            try {
                BufferedImage originalImage = ImageIO.read(file);
                BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);
                File resizedFile = new File(outputPath + "/" + file.getName());
                ImageIO.write(resizedImage, getImageType(file), resizedFile);
            } catch (IOException e) {
                System.err.println("Error resizing image: " + file.getName() + " - " + e.getMessage());
            }
        });
    }

    /**
     * Resizes the image.
     * @param originalImage Original image to be resized.
     * @param targetWidth Target width.
     * @param targetHeight Target height.
     * @return A new BufferedImage with the resized image.
     */
    private BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
        Graphics2D graphics2D = resizedImage.createGraphics();
        graphics2D.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        graphics2D.dispose();
        return resizedImage;
    }

    /**
     * Checks if the file is an image.
     * @param file File to check.
     * @return True if the file is an image, false otherwise.
     */
    private boolean isImageFile(File file) {
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") || fileName.endsWith(".gif");
    }

    /**
     * Gets the image type from the file.
     * @param file Image file.
     * @return The image type as a string.
     */
    private String getImageType(File file) {
        String fileName = file.getName().toLowerCase();
        if (fileName.endsWith(".png")) {
            return "png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "jpg";
        } else if (fileName.endsWith(".gif")) {
            return "gif";
        } else {
            return "png"; // Default to PNG if file type not recognized
        }
    }

    public static void main(String[] args) {
        // Initialize Spark session
        SparkSession spark = SparkSession
                .builder()
                .appName("Image Resizer")
                .master("local[*]")
                .getOrCreate();

        // Create ImageResizer instance
        ImageResizer imageResizer = new ImageResizer(spark);

        // Resize images
        imageResizer.resizeImages("path/to/input/images", "path/to/output/images", 800, 600);

        // Stop Spark session
        spark.stop();
    }
}
