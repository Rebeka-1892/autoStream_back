package com.projet_voiture.projet_voiture.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class Photo {
    String base64;
    String filename;
    
    public Photo(String base64, String filename) {
        this.base64 = base64;
        this.filename = filename;
    }

    public File convertToFile() throws IOException {
        if (base64.startsWith("data:image/png;base64,")) {
            base64 = base64.substring("data:image/png;base64,".length());
        }
      byte[] decodedBytes = Base64.getDecoder().decode(base64);
      ByteArrayInputStream inputStream = new ByteArrayInputStream(decodedBytes);
      File outputImage = new File(this.filename);
      ImageIO.write(ImageIO.read(inputStream), "png", outputImage);
      return outputImage;
    }

    public String getBase64() {
        return base64;
    }
    public void setBase64(String base64) {
        this.base64 = base64;
    }
    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }
}