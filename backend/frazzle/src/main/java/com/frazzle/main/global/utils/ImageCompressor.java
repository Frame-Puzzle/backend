package com.frazzle.main.global.utils;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageCompressor {

    public static BufferedImage convertMultiPartFileToBufferedImage(MultipartFile file) throws IOException {
        return ImageIO.read(file.getInputStream());
    }

    public static File compressImage(BufferedImage image, String formatName, float quality, String outputFileName) throws IOException {
        File compressedImageFile = new File(outputFileName);
        try (FileOutputStream fos = new FileOutputStream(compressedImageFile);
             ImageOutputStream ios = ImageIO.createImageOutputStream(fos)) {
            ImageWriter writer = ImageIO.getImageWritersByFormatName(formatName).next();
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();
            if (param.canWriteCompressed()) {
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(quality);
            }

            writer.write(null, new IIOImage(image, null, null), param);
            writer.dispose();
        }
        return compressedImageFile;
    }
}
