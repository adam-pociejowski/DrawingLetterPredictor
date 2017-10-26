package com.valverde.letterpainter.service;

import com.valverde.letterpainter.util.AppUtils;
import com.valverde.letterpainter.util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

@Service
public class LetterUploadService {

    public void saveLetter(final MultipartFile multipartFile, final String letter) throws Exception {
        if (AppUtils.isNull(letter) || letter.length() != 1) {
            throw new IllegalArgumentException("Letter must be filled");
        }
        final String letterUpCase = letter.toUpperCase();
        final String fileName = getFileName(letterUpCase);
        FileUtils.convertToFile(multipartFile, LETTERS_PATH +
                "/" + letterUpCase + "/" +
                fileName);
    }

    private String getFileName(final String letter) {
        createDirectoryIfNotExists(letter);
        Integer letterNumber = getLetterNumber(LETTERS_PATH + "/" + letter);
        return letter+"_"+letterNumber+IMAGE_EXTENSION;
    }

    private Integer getLetterNumber(final String dirPath) {
        final File file = new File(dirPath);
        final String[] fileNames = file.list();
        Integer letterNumber = 0;
        if (AppUtils.isNotNull(fileNames)) {
            letterNumber += fileNames.length;
        }
        return letterNumber;
    }

    private void createDirectoryIfNotExists(final String directoryName) {
        final File file = new File(LETTERS_PATH);
        final String[] fileNames = file.list();
        boolean directoryExists = false;
        if (AppUtils.isNotNull(fileNames)) {
            for (String fileName: fileNames) {
                if (fileName.equals(directoryName)) {
                    directoryExists = true;
                }
            }
        }

        if (!directoryExists) {
            final File dir = new File(LETTERS_PATH+"/"+directoryName);
            dir.mkdir();
        }
    }

    @Value("${letters.path}")
    private String LETTERS_PATH;

    private final static String IMAGE_EXTENSION = ".png";
}