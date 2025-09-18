// 代码生成时间: 2025-09-18 17:43:55
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NetworkStatusChecker {
    private static final String HOST = "example.com"; // Replace with the actual host to check
    private static final int PORT = 80; // HTTP port is usually 80

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setAppName("Network Status Checker");
        JavaSparkContext sc = new JavaSparkContext(conf);
        SparkSession spark = SparkSession.builder()
                .appName("Network Status Checker")
                .master("local")
                .getOrCreate();

        // List of hosts to check
        List<String> hosts = Arrays.asList("google.com", "bing.com", HOST);

        try {
            JavaRDD<NetworkStatus> statusRDD = sc.parallelize(hosts)
                    .map(host -> checkHost(host));

            statusRDD.foreach(status -> System.out.println(status));

        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
        } finally {
            sc.close();
        }
    }

    /**
     * Check the network status of a given host
     *
     * @param host The hostname or IP address to check
     * @return A NetworkStatus object containing the result
     */
    private static NetworkStatus checkHost(String host) {
        try (Socket socket = new Socket(host, PORT)) {
            return new NetworkStatus(host, true, "Connection successful");
        } catch (UnknownHostException e) {
            return new NetworkStatus(host, false, "Unknown host: " + e.getMessage());
        } catch (Exception e) {
            return new NetworkStatus(host, false, "Connection failed: " + e.getMessage());
        }
    }

    /**
     * A simple POJO class to hold the network status information
     */
    public static class NetworkStatus {
        private String host;
        private boolean connected;
        private String message;

        public NetworkStatus(String host, boolean connected, String message) {
            this.host = host;
            this.connected = connected;
            this.message = message;
        }

        @Override
        public String toString() {
            return "Host: " + host + ", Connected: " + connected + ", Message: " + message;
        }
    }
}