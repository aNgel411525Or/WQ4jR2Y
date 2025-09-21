// 代码生成时间: 2025-09-21 10:44:12
 * count data points and calculate statistics like mean, median, and standard deviation.
 */

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.sql.types.StructField;

public class DataAnalysisApp {

    private static SparkSession sparkSession;
    private static Dataset<Row> dataset;

    // Initialize Spark Session and Dataset
    public static void initialize(SparkSession spark, Dataset<Row> data) {
        sparkSession = spark;
        dataset = data;
    }

    // Calculate mean of the dataset
    public static double calculateMean() {
        return dataset.agg(functions.mean("value")).first().getAs("avg").getDouble(0);
    }

    // Calculate median of the dataset
    public static double calculateMedian() {
        return dataset.stat().approxQuantile("value", new double[]{0.5}, 0.0)[0];
    }

    // Calculate standard deviation of the dataset
    public static double calculateStandardDeviation() {
        return dataset.agg(functions.stdev("value")).first().getAs("stdev").getDouble(0);
    }

    // Main method to run the application
    public static void main(String[] args) {
        try {
            // Initialize Spark Session
            sparkSession = SparkSession.builder()
                .appName("DataAnalysisApp")
                .master("local")
                .getOrCreate();
            
            // Define the schema of the dataset
            StructType schema = DataTypes.createStructType(new StructField[]{
                DataTypes.createStructField("value", DataTypes.DoubleType, false)
            });

            // Load dataset
            dataset = sparkSession.read().schema(schema).csv("data.csv");

            // Initialize application with Spark Session and Dataset
            initialize(sparkSession, dataset);

            // Perform data analysis
            double mean = calculateMean();
            double median = calculateMedian();
            double stdDev = calculateStandardDeviation();

            // Print the results
            System.out.println("Mean: " + mean);
            System.out.println("Median: " + median);
            System.out.println("Standard Deviation: " + stdDev);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Stop the Spark Session
            if (sparkSession != null) {
                sparkSession.stop();
            }
        }
    }
}
