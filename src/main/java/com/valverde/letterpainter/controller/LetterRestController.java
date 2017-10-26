package com.valverde.letterpainter.controller;

import com.valverde.letterpainter.enums.Letter;
import com.valverde.letterpainter.service.LetterRecognitionService;
import com.valverde.letterpainter.service.LetterUploadService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@CommonsLog
@RestController
@RequestMapping("/letter")
public class LetterRestController {

    @PostMapping("/save")
    public ResponseEntity saveLetter(@RequestBody MultipartFile file, final String letter) {
        try {
            letterUploadService.saveLetter(file, letter);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception while saving file", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/train")
    public ResponseEntity train() {
        try {
            letterRecognitionService.trainModel();
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception while training model", e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/predict")
    public ResponseEntity<Letter> predict(@RequestBody MultipartFile file) {
        try {
            final Letter letter = letterRecognitionService.predictLetter(file);
            log.info("Predicted letter: "+letter);
            return new ResponseEntity<>(letter, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception while saving file", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public LetterRestController(final LetterUploadService letterUploadService,
                                final LetterRecognitionService letterRecognitionService) {
        this.letterUploadService = letterUploadService;
        this.letterRecognitionService = letterRecognitionService;
    }

    private final LetterUploadService letterUploadService;

    private final LetterRecognitionService letterRecognitionService;
}