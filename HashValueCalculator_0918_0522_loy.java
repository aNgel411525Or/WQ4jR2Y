// 代码生成时间: 2025-09-18 05:22:20
 * This program is designed to be extensible and maintainable,
 * following best practices and providing clear structure.
 */

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashValueCalculator {

    // Main entry point of the application
    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("HashValueCalculator").setMaster("local[*]");
        try (JavaSparkContext sc = new JavaSparkContext(conf)) {
            // Check if there are input arguments
            if (args.length == 0) {
                System.err.println("Usage: HashValueCalculator <input-path>");
                return;
            }

            // Initialize the input path
            String inputPath = args[0];

            // Calculate hash values for each file in the input path
            calculateHashValues(sc, inputPath);
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Hash algorithm not found: " + e.getMessage());
        }
    }

    // Method to calculate hash values for each file
    private static void calculateHashValues(JavaSparkContext sc, String inputPath) throws NoSuchAlgorithmException {
        // Read files from the input path
        JavaRDD<String> textFile = sc.textFile(inputPath);

        // Calculate hash for each line (file content)
        JavaRDD<String> hashValues = textFile.map(line -> calculateHash(line));

        // Collect and print hash values
        hashValues.collect().forEach(System.out::println);
    }

    // Method to calculate hash for a given string (file content)
    private static String calculateHash(String input) throws NoSuchAlgorithmException {
        // Get a MessageDigest instance for the desired hash algorithm (e.g., SHA-256)
        MessageDigest digest = MessageDigest.getInstance("SHA-256");

        // Update the digest using the input bytes
        digest.update(input.getBytes());

        // Calculate the hash bytes
        byte[] hashBytes = digest.digest();

        // Convert the hash bytes to a hexadecimal string
        StringBuilder hexString = new StringBuilder(2 * hashBytes.length);
        for (int i = 0; i < hashBytes.length; i++) {
            String hex = Integer.toHexString(0xff & hashBytes[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        // Return the hash as a hexadecimal string
        return hexString.toString();
    }
}
