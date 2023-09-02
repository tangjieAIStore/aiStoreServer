package cn.aistore.ai.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

@Slf4j
public class CharacterCounter {

    public static int countCharacters(String filePath) throws IOException {
        String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);
        int count = 0;
        switch (fileType) {
            case "txt":
                count = countTextCharacters(filePath);
                break;
            case "doc":
                count = countWordDocCharacters(filePath);
                break;
            case "docx":
                count = countWordDocxCharacters(filePath);
                break;
            case "pdf":
                count = countPdfCharacters(filePath);
                break;
            default:
                log.error("Unsupported file type: {}", fileType);
        }
        return count;
    }

    public static int countTextCharacters(String filePath) throws IOException {
        BufferedReader reader = null;
        URL url = new URL(filePath);
        reader = new BufferedReader(new InputStreamReader(url.openStream()));
        int count = 0;
        int character;
        while ((character = reader.read()) != -1) {
            count++;
        }
        reader.close();
        return count;
    }

    public static int countWordDocCharacters(String filePath) throws IOException {
        URL url = new URL(filePath);
        HWPFDocument documentHW = new HWPFDocument(url.openStream());
        WordExtractor extractor = new WordExtractor(documentHW);
        String text = extractor.getText();
        int count = text.length();
        documentHW.close();
        return count;
    }

    public static int countWordDocxCharacters(String filePath) throws IOException {
        URL url = new URL(filePath);
        XWPFDocument documentXW = new XWPFDocument(url.openStream());
        List<XWPFParagraph> paragraphs = documentXW.getParagraphs();

        int characterCount = 0;
        for (XWPFParagraph para : paragraphs) {
            characterCount += para.getText().length();
        }

        documentXW.close();
        return characterCount;
    }

    public static int countPdfCharacters(String filePath) throws IOException {
        URL url = new URL(filePath);
        PDDocument document = PDDocument.load(url.openStream());
        PDFTextStripper stripper = new PDFTextStripper();
        String text = stripper.getText(document);
        int count = text.length();
        document.close();
        return count;
    }

}
