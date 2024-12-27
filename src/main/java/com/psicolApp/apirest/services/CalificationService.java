package com.psicolApp.apirest.services;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CalificationService {

   // private final String scriptPath = "apirest/src/main/java/com/psicolApp/apirest/utils/Clasificator.py"; // Ruta al script de Python
   private final String scriptPath = "src/main/java/com/psicolApp/apirest/utils/Clasificator.py"; // Ruta al script de Python
    public String evaluateText(String text) {
        try {
            // Crear el proceso para ejecutar el script Python
            ProcessBuilder pb = new ProcessBuilder("python", scriptPath, text);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Leer la salida del proceso
            StringBuilder output = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }

            String outputString = output.toString();
            Pattern pattern = Pattern.compile("\\[\\{'label': '.*?', 'score': .*?\\}\\]");
            Matcher matcher = pattern.matcher(outputString);
            if (matcher.find()) {
                return matcher.group();
            } else {
                throw new RuntimeException("No evaluation result found in the script output.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error evaluating the text", e);
        }
    }
}