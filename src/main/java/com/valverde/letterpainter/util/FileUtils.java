package com.valverde.letterpainter.util;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.model.ImagePointsPart;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.valverde.letterpainter.letter.strategy.LetterRecognitionStrategy.LETTERS_PATH;

public class FileUtils {

    public static BufferedImage getBufferedImage(final String path) throws Exception {
        final File img = new File(path);
        return ImageIO.read(img);
    }

    public static File convertToFile(MultipartFile multipartFile) throws IOException {
        final File file = new File(multipartFile.getOriginalFilename());
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }

    public static File convertToFile(final MultipartFile multipartFile, final String fileName) throws IOException {
        final File file = new File(fileName);
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(multipartFile.getBytes());
        fileOutputStream.close();
        return file;
    }

    public static void deleteFile(File file) {
        if (AppUtils.isNotNull(file))
            file.delete();
    }

    public static List<String> getFileNames(final Letter letter) {
        File file = new File(LETTERS_PATH + "/" + letter);
        final String[] fileNames = file.list();
        if (AppUtils.isNotNull(fileNames))
            return new ArrayList<>(Arrays.asList(fileNames));
        throw new IllegalArgumentException("No files found for letter "+letter);
    }

    public static String createCsvRow(final List<ImagePointsPart> parts, final Letter letter) {
        String row = "";
        for (ImagePointsPart part : parts) {
            row += part.getSumValues() + ",";
        }
        row += letter +"\n";
        return row;
    }
}