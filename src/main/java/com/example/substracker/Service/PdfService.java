package com.example.substracker.Service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.Map;
@Service
public class PdfService {

    private final TemplateEngine templateEngine;

   public PdfService(TemplateEngine templateEngine){
       this.templateEngine=templateEngine;
   }

    public byte[] generatePdf(String templateName, Map<String,Object> data){
        try {
            Context context = new Context();
            if (data != null) context.setVariables(data);

            String html = templateEngine.process(templateName, context);

            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(html, null);
                builder.toStream(baos);
                builder.run();
                return baos.toByteArray();
            }
        } catch (Exception e){
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
   public byte[] buildSubscriptionReceipt(Integer subscriptionId,
                                             String userName,
                                             String userEmail,
                                             String serviceName,
                                             String planName,
                                             String period,
                                             double price) {

        Map<String, Object> vars = Map.of(
                "subscriptionId", subscriptionId,
                "userName", userName,
                "userEmail", userEmail,
                "serviceName", serviceName,
                "planName", planName,
                "period", period,
                "priceFormatted", formatMoney(price),
                "totalFormatted", formatMoney(price),
                "date", LocalDate.now().toString()
        );
        // template name WITHOUT .html
        return generatePdf("subscription-receipt", vars);
    }
    private static String formatMoney(double amount) {
        return String.format("%.2f SAR", amount);
    }

}