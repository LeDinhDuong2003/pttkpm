package com.example.mau_service.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path fileStorageLocation; // Giữ lại cho trường hợp cần lưu tạm thời
    private final Cloudinary cloudinary;
    private final boolean useCloudinary;

    public FileStorageService(
            @Value("${app.upload-dir:./uploads}") String uploadDir,
            @Value("${cloudinary.cloud-name:#{null}}") String cloudName,
            @Value("${cloudinary.api-key:#{null}}") String apiKey,
            @Value("${cloudinary.api-secret:#{null}}") String apiSecret,
            @Value("${cloudinary.enabled:false}") boolean useCloudinary) {

        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        // Tạo thư mục lưu trữ tạm thời nếu không tồn tại
        try {
            if (!Files.exists(this.fileStorageLocation)) {
                Files.createDirectories(this.fileStorageLocation);
                logger.info("Created directory: {}", this.fileStorageLocation);
            }
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored: " + ex.getMessage(), ex);
        }

        // Khởi tạo Cloudinary nếu có đủ thông tin
        boolean validCloudinaryConfig = cloudName != null && !cloudName.isEmpty() &&
                apiKey != null && !apiKey.isEmpty() &&
                apiSecret != null && !apiSecret.isEmpty();

        this.useCloudinary = useCloudinary && validCloudinaryConfig;

        if (this.useCloudinary) {
            cloudinary = new Cloudinary(ObjectUtils.asMap(
                    "cloud_name", cloudName,
                    "api_key", apiKey,
                    "api_secret", apiSecret,
                    "secure", true
            ));
            logger.info("Cloudinary initialized successfully with cloud name: {}", cloudName);
        } else {
            cloudinary = null;
            logger.info("Cloudinary is disabled or missing configuration. Using local storage.");
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }

            // Tạo tên file ngẫu nhiên để tránh trùng lặp
//            String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
//            String fileExtension = "";
//
//            if (originalFileName.contains(".")) {
//                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
//            }
//
//            String fileName = UUID.randomUUID().toString() + fileExtension;
//
//            // kiem tra ki tu dac biet
//            if (fileName.contains("..")) {
//                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + fileName);
//            }

            if (useCloudinary && cloudinary != null) {
                try {
                    // Upload file len Cloudinary
                    String publicId = "violence_sample_" + UUID.randomUUID().toString();
                    Map uploadResult = cloudinary.uploader().upload(
                            file.getBytes(),
                            ObjectUtils.asMap(
                                    "public_id", publicId,
                                    "folder", "violence_samples",
                                    "resource_type", "auto"
                            )
                    );

                    logger.info("File uploaded to Cloudinary: {}", publicId);


                    return (String) uploadResult.get("secure_url");
                } catch (Exception e) {
                    logger.error("Error uploading to Cloudinary: {}", e.getMessage());
                    logger.warn("Falling back to local storage");
                }
            }

            // Lưu file vào thư mục đích
//            Path targetLocation = this.fileStorageLocation.resolve(fileName);
//
//            // Sử dụng Files.copy với tham số InputStream
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            logger.info("File stored locally: {}", fileName);
//
//            return fileName;
            return "";
        } catch (Exception ex) {
            throw new RuntimeException("Could not store file. Please try again!", ex);
        }
    }

//    public Resource loadFileAsResource(String fileName) {
//        try {
//            if (fileName == null || fileName.isEmpty()) {
//                throw new RuntimeException("File name is empty");
//            }
//
//            if (fileName.startsWith("http")) {
//                URL url = new URL(fileName);
//                return new UrlResource(url);
//            } else {
//                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//                Resource resource = new UrlResource(filePath.toUri());
//
//                if (resource.exists()) {
//                    return resource;
//                } else {
//                    throw new RuntimeException("File not found " + fileName);
//                }
//            }
//        } catch (MalformedURLException ex) {
//            throw new RuntimeException("File not found " + fileName, ex);
//        }
//    }

    public boolean deleteFile(String fileName) {
        try {
            if (fileName == null || fileName.isEmpty()) {
                return false;
            }

            if (fileName.startsWith("http")) {
                // Xóa từ Cloudinary nếu có thể
                if (useCloudinary && cloudinary != null) {
                    try {
                        // Trích xuất publicId từ URL
                        String publicId = extractPublicIdFromUrl(fileName);
                        Map result = cloudinary.uploader().destroy(
                                publicId,
                                ObjectUtils.asMap("resource_type", "video")
                        );

                        logger.info("File deleted from Cloudinary: {}", publicId);
                        return "ok".equals(result.get("result"));
                    } catch (Exception e) {
                        logger.error("Error deleting from Cloudinary: {}", e.getMessage());
                        return false;
                    }
                }
                // Nếu không dùng Cloudinary, không thể xóa URL
                return false;
            }
//            else {
//                // Xóa file local
//                Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//                boolean result = Files.deleteIfExists(filePath);
//                if (result) {
//                    logger.info("File deleted locally: {}", fileName);
//                }
//                return result;
//            }
        } catch (Exception ex) {
            logger.error("Error deleting file {}: {}", fileName, ex.getMessage());
            return false;
        }
        return false;
    }

    private String extractPublicIdFromUrl(String url) {

        try {
            // Tách đường dẫn
            String[] parts = url.split("/upload/");
            if (parts.length < 2) {
                logger.warn("Could not parse Cloudinary URL correctly: {}", url);
                return "";
            }

            String afterUpload = parts[1];
            if (afterUpload.startsWith("v") && afterUpload.length() > 10) {
                afterUpload = afterUpload.substring(afterUpload.indexOf('/') + 1);
            }

            // Bỏ phần mở rộng nếu có
            if (afterUpload.contains(".")) {
                afterUpload = afterUpload.substring(0, afterUpload.lastIndexOf("."));
            }

            return afterUpload;
        } catch (Exception e) {
            logger.error("Error extracting public ID from URL {}: {}", url, e.getMessage());
            return "";
        }
    }

    /**
     * Lấy đường dẫn tương đối của file
     * Giữ tên hàm nguyên bản: getRelativeFilePath
     */
    public String getRelativeFilePath(String fileName) {
        // Nếu đã là URL thì trả về nguyên
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        if (fileName.startsWith("http")) {
            return fileName;
        }

        return "/uploads/" + fileName;
    }
}