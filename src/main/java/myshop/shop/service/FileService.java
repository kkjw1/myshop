package myshop.shop.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
        return uuid + ext;
    }



    /**
     * 파일 저장
     * @return storeFileName
     */
    public String storeFile(MultipartFile multipartFile) throws IOException {
        String storeFileName = null;
        if (!multipartFile.isEmpty()) {
            String storeName = createStoreName(multipartFile.getOriginalFilename());
            storeFileName = fileDir + storeName;
            multipartFile.transferTo(new File(storeFileName));
        }

        return storeFileName;
    }



    /**
     * 파일 여러개 저장
     * @return List<storeFileName>
     */
    public List<String> storeFiles(List<MultipartFile> multipartFileList) throws IOException {
        List<String> storeFileNameList = new ArrayList<>();

        for (MultipartFile multipartFile : multipartFileList) {
            if (!multipartFile.isEmpty()) {
                storeFileNameList.add(storeFile(multipartFile));
            }
        }
        return storeFileNameList;
    }
}
