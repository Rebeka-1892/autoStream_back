package com.projet_voiture.projet_voiture.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    public static String getFileAsBase64(String filePath) throws IOException {
        String bucketName = "stockage-photo-b8c98.appspot.com";

        try (InputStream serviceAccountStream = ImageService.class.getClassLoader().getResourceAsStream("stockage-photo-b8c98-firebase-adminsdk-lj5rn-a27f5ad515.json")) {
            if (serviceAccountStream == null) {
                throw new IOException("Le fichier JSON de l'identifiant Firebase n'a pas pu être chargé.");
            }

            // Chargez les informations d'identification depuis le fichier JSON
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccountStream);

            // Initialisez Firebase Storage avec les informations d'identification
            Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

            // Get Blob Reference
            BlobId blobId = BlobId.of(bucketName, filePath);
            Blob blob = storage.get(blobId);

            if (blob == null) {
                throw new IOException("Le Blob est null. Vérifiez si le blob existe dans le bucket.");
            }

            // Read the content of the file into a byte array
            ByteBuffer byteBuffer = ByteBuffer.allocate(Math.toIntExact(blob.getSize()));
            blob.reader().read(byteBuffer);

            // Convert byte array to Base64
            byte[] byteArray = byteBuffer.array();
            return convertByteArrayToBase64(byteArray);
        } catch (IOException e) {
            e.printStackTrace();
            throw e; // Remonte l'exception pour que le code appelant puisse également la gérer
        }
    }

    private static String convertByteArrayToBase64(byte[] byteArray) {
        return java.util.Base64.getEncoder().encodeToString(byteArray);
    }

    private String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("stockage-photo-b8c98.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        // InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("credentials.json");
        InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("stockage-photo-b8c98-firebase-adminsdk-lj5rn-a27f5ad515.json"); // change the file name with your one
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
  
        // String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/image-server-ef5eb.appspot.com/o/%s?alt=media";
        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/stockage-photo-b8c98.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }
    
    // private String uploadFile(File file, String fileName) throws IOException {
    //     BlobId blobId = BlobId.of("stockage-photo-b8c98.appspot.com", fileName);
    //     BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
    //     InputStream inputStream = ImageService.class.getClassLoader()
    //             .getResourceAsStream("stockage-photo-b8c98-firebase-adminsdk-lj5rn-a27f5ad515.json");
    //     Credentials credentials = GoogleCredentials.fromStream(inputStream);
    //     Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    //     storage.create(blobInfo, Files.readAllBytes(file.toPath()));

    //     // String url = "https://console.firebase.google.com/u/0/project/stockage-photo-b8c98/storage/stockage-photo-b8c98.appspot.com/o/ad8c78ce-fff4-4a49-8ddd-9e90eec46277.png?alt=media";
    //     String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/stockage-photo-b8c98.appspot.com/o/%s?alt=media";
    //     return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    // }

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