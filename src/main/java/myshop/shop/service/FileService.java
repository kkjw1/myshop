package myshop.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class FileService {

    @Value("${file.dir}")
    private String fileDir;


    /**
     * 파일명 생성
     */
    public String createStoreName(String fileName) {
        int pos = fileName.lastIndexOf(".");
        String ext = fileName.substring(pos);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }


    /**
     * 파일 저장
     */
    public void storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return;
        }
        String storeName = createStoreName(multipartFile.getOriginalFilename());

        multipartFile.transferTo(new File(fileDir+storeName));
    }



    /**
     * 파일 여러개 저장
     */
    public void storeFiles(List<MultipartFile> multipartFileList) {

    }
}
