package com.mever.api.domain.mainAdmin.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

@Service
public class FileService {
    public void uploadFile(MultipartHttpServletRequest multiRequest) throws Exception {
        // 파일 정보를 값으로 하는 Map을 가져온다.
        Map<String, MultipartFile> files = multiRequest.getFileMap();
        // files.entrySet()의 요소를 가져온다.
        Iterator<Map.Entry< String, MultipartFile>> itr = files.entrySet().iterator();
        // MultipartFile 초기화
        MultipartFile mFile = null;
        // 파일이 업로드 될 경로를 지정
        String filePath = "C:\\Users\\PC\\IdeaProjects\\mever\\src\\main\\resources\\static";

        // 읽어 올 요소가 있으면 true, 없으면 false를 반환
        while (itr.hasNext()) {
            // Iterator의 MultipartFile 요소를 가져온다.
            Map.Entry<String, MultipartFile> entry = itr.next();

            // entry에 값을 가져온다.
            mFile = entry.getValue();
            // 원본 파일명, 저장 될 파일명 생성
            String fileOrgName = mFile.getOriginalFilename();
            if (!fileOrgName.isEmpty()) {
                // filePath에 해당되는 파일의 File 객체를 생성
                File fileFolder = new File(filePath);
                if (!fileFolder.exists()) {
                    // 부모 폴더까지 포함하여 경로에 폴더 생성
                    if (fileFolder.mkdirs()) {
                        System.out.println("[file.mkdirs] : Success");
                    }
                }
                File saveFile = new File(filePath, fileOrgName);
                // 생성한 파일 객체를 업로드 처리하지 않으면 임시파일에 저장된 파일이 자동적으로 삭제되기 때문에
                // transferTo(File f) 메서드를 이용해서 업로드 처리
                mFile.transferTo(saveFile);
            }
        }
    }
}