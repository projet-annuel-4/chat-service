package fr.esgi.chat.service;

import fr.esgi.chat.domain.model.FileModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileStorageService {
    private static final String ROOT_URL = "uploads";
    public List<FileModel> store(List<MultipartFile> files, Long chatId) {
        var uploadedFiles = new ArrayList<FileModel>();
        files.stream().forEach (file->{
            var path = Path.of(ROOT_URL, String.valueOf(chatId));
            try {
                Files.copy(file.getInputStream(),
                        path.resolve(UUID.randomUUID().toString() + file.getOriginalFilename().split(".")),
                StandardCopyOption.REPLACE_EXISTING);
                uploadedFiles.add( FileModel.builder()
                        .url(path.toString())
                        .type(!file.getContentType().isEmpty() ? file.getContentType() : "")
                        .build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return uploadedFiles;
    }
}
