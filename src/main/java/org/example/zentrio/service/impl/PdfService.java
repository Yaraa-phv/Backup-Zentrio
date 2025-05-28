package org.example.zentrio.service.impl;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    public byte[] generatePdf(String templateName, Map<String, Object> data) {
        try {
            Context context = new Context();
            context.setVariables(data);

            String html = templateEngine.process(templateName, context);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ITextRenderer renderer = new ITextRenderer();

//            addFontsToRenderer(renderer);

            String baseUrl = Objects.requireNonNull(getClass().getResource("/templates/")).toExternalForm();
            renderer.setDocumentFromString(html, baseUrl);

            renderer.layout();
            renderer.createPDF(outputStream);

            return outputStream.toByteArray();

        } catch (DocumentException e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

//    private void addFontsToRenderer(ITextRenderer renderer) {
//        try {
//            ClassLoader classLoader = getClass().getClassLoader();
//            String fontPath = "static/fonts/Exile-Regular.ttf";
//
//            if (classLoader.getResource(fontPath) != null) {
//                renderer.getFontResolver().addFont(
//                        fontPath,
//                        BaseFont.IDENTITY_H,
//                        BaseFont.EMBEDDED
//                );
//                System.out.println("Loaded font: " + fontPath);
//            } else {
//                System.err.println("Font not found: " + fontPath);
//            }
//
//        } catch (Exception e) {
//            System.err.println("Font loading failed: " + e.getMessage());
//        }
//    }
}
