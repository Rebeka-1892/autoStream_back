package com.projet_voiture.projet_voiture.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.springframework.stereotype.Service;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("stockage-photo-b8c98.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("stockage-photo-b8c98-firebase-adminsdk-lj5rn-a27f5ad515.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
  
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/stockage-photo-b8c98.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public String upload(Photo photo) throws Exception {
        String filename = UUID.randomUUID().toString().concat(this.getExtension(photo.getFilename()));
        File file = photo.convertToFile();
        String URL = this.uploadFile(file, filename);
        file.delete();
        return URL;
    }

    public List<String> upload(String[] photos) throws Exception {
        List<String> images = new ArrayList<>();
        for (int i = 0; i < photos.length; i++) {
            Photo photo = new Photo(photos[i], "image" + i + ".png");
            images.add(this.upload(photo));
        }
        return images;
    }

}