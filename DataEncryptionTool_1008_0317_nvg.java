// 代码生成时间: 2025-10-08 03:17:24
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import java.util.Arrays;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * DataEncryptionTool is a program that demonstrates how to use Apache Spark to encrypt data.
 * It generates a symmetric key, encrypts data, and then decrypts it as a demonstration.
 */
public class DataEncryptionTool {

    private static SecretKey generateKey() throws Exception {
        // Generate a secret key using KeyGenerator
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256); // Use 256-bit AES
        return keyGenerator.generateKey();
    }

    private static byte[] encryptData(SecretKey key, byte[] data) throws Exception {
        // Encrypt the data using the provided key
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(data);
    }

    private static byte[] decryptData(SecretKey key, byte[] encryptedData) throws Exception {
        // Decrypt the data using the provided key
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(encryptedData);
    }

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("DataEncryptionTool").setMaster("local[*]");
        JavaSparkContext sc = new JavaSparkContext(conf);

        try {
            // Generate a symmetric key
            SecretKey key = generateKey();

            // Example data to be encrypted
            String data = "Hello, Apache Spark!";
            byte[] dataBytes = data.getBytes();

            // Encrypt the data
            byte[] encryptedData = encryptData(key, dataBytes);
            String encryptedDataBase64 = Base64.getEncoder().encodeToString(encryptedData);

            // Decrypt the data
            byte[] decryptedData = decryptData(key, encryptedData);
            String decryptedDataString = new String(decryptedData);

            // Output the results
            System.out.println("Original data: " + data);
            System.out.println("Encrypted data (Base64): " + encryptedDataBase64);
            System.out.println("Decrypted data: " + decryptedDataString);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sc.close();
        }
    }
}
