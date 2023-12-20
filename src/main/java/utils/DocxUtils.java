package main.java.utils;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import main.java.models.CV;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Utility class for working with DOCX files and extracting CV information.
 * 
 * @author <a href="https://github.com/Trustacean">Edward</a>
 */
public class DocxUtils {

    /**
     * Retrieves an array of files from the specified folder path.
     *
     * @return An array of File objects representing files in the specified folder.
     */
    public static File[] getFiles() {
        // Directory CV Sorting/src/main/docs/file.docx
        String folderPath = "src" + File.separator + "main" + File.separator + "docs";
        File folder = new File(folderPath);

        if (folder.exists() && folder.isDirectory()) {
            return folder.listFiles();
        } else {
            System.out.println("The specified folder does not exist or is not a directory.");
            return null;
        }
    }

    /**
     * Extracts CV information from an array of DOCX files.
     *
     * @param files An array of File objects representing DOCX files.
     * @return An array of CV objects extracted from the provided DOCX files.
     */
    public static CV[] extractCVsFromFile(File[] files) {
        ArrayList<CV> result = new ArrayList<>();

        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".docx")) {
                try (FileInputStream fis = new FileInputStream(file.getAbsolutePath());
                        XWPFDocument document = new XWPFDocument(fis);
                        XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {

                    // Extracting text from the document
                    String fileText = extractor.getText();
                    String[] lines = fileText.split("\n");

                    // Variables to store CV information
                    String id = "";
                    String name = "";
                    String position1 = "";
                    String position2 = "";

                    // Parsing each line to extract relevant information
                    for (String line : lines) {
                        if (line.matches(".*\\bNIM\\b.*")) {
                            id = line.replaceAll("[^0-9]", "");
                        } else if (line.matches(".*\\bNama Lengkap\\s*:.*")) {
                            name = line.replaceFirst("Nama Lengkap\\s*:", "").trim();
                        } else if (line.matches(".*\\bPilihan 1\\s*:.*")) {
                            position1 = line.replaceFirst("Pilihan 1\\s*:", "").trim();
                        } else if (line.matches(".*\\bPilihan 2\\s*:.*")) {
                            position2 = line.replaceFirst("Pilihan 2\\s*:", "").trim();
                        }
                    }

                    // Creating a new CV object and adding it to the result list
                    CV newCV = new CV(name, id, position1, position2, file);
                    result.add(newCV);

                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }

        return result.toArray(new CV[0]);
    }
}