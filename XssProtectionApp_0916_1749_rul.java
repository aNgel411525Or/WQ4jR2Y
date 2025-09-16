// 代码生成时间: 2025-09-16 17:49:28
import static spark.Spark.*;
import spark.template.freemarker.FreeMarkerEngine;
import spark.ModelAndView;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import javax.servlet.http.HttpServletRequest;

public class XssProtectionApp {
    
    // Entry point for the Spark application
    public static void main(String[] args) {
        // Configure Freemarker template engine
        configureTemplateEngine();
        
        // Define route for handling web requests
        get("/", (req, res) -> {
            try {
                // Sanitize input and set up model for template rendering
                String userInput = sanitizeInput(req.queryParams("userInput"));
                ModelAndView modelAndView = new ModelAndView(new HashMap<>(), "index.ftl");
                modelAndView.getModel().put("userInput", userInput);
                return modelAndView;
            } catch (Exception e) {
                // Handle any exceptions that occur
                halt(400, "Error: Invalid input.");
                return "";
            }
        }, new FreeMarkerEngine());
    }
    
    // Method to sanitize user input to prevent XSS attacks
    private static String sanitizeInput(String input) {
        // Remove all HTML tags from the input
        return input == null ? "" : input.replaceAll("<[^>]*>", "");
    }
    
    // Configure Freemarker template engine
    private static void configureTemplateEngine() {
        Configuration freemarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freemarkerConfig.setClassForTemplateLoading(XssProtectionApp.class, "/");
        
        staticFiles.location("/public");
        templateEngine(new FreeMarkerEngine(freemarkerConfig));
    }
    
    // Method to render a Freemarker template with a given model
    private static String renderTemplate(String templateName, Object model) throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(XssProtectionApp.class, "/");
        Template template = cfg.getTemplate(templateName);
        Writer out = new StringWriter();
        template.process(model, out);
        return out.toString();
    }
}