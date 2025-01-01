package com.groups.schicken.common.util;

import com.groups.schicken.common.aws.S3Service;
import com.groups.schicken.common.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Component
public class FileManager {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private FileMapper fileMapper;
    @Value("${app.upload.base}")
    private String uploadPath;

    public boolean uploadFile(MultipartFile file, FileVO fileVO) throws Exception{
        String uid = UUID.randomUUID().toString();
        fileVO.setName(uid);
        fileVO.setOriginName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
        fileVO.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        uploadFileToLocal(file, fileVO);
        fileMapper.uploadFile(fileVO);
        return true;
    }

    @Transactional
    public boolean deleteFile(FileVO fileVO) throws Exception {
        fileMapper.deleteFile(fileVO);
        return deleteLocalFile(fileVO);
    }
    public List<FileVO> getFiles() {
        return null;
    }
//    public ResponseEntity<byte[]> downFile(FileVO fileVO) throws Exception{
//    	if(fileVO==null || fileVO.getId() == null) {
//    		return null;
//    	}
//    	fileVO = fileMapper.downFile(fileVO);
//
//		return s3Service.downFile(fileVO);
//
//    }


    private FileVO uploadFileToLocal(MultipartFile file, FileVO fileVO) throws Exception {
        try {
            String fileName = fileVO.getName();
            String filePath = uploadPath + fileName;

            // 파일 저장 디렉토리가 없는 경우 생성
            File directory = new File(uploadPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 파일을 로컬 디스크에 저장
            try (FileOutputStream fos = new FileOutputStream(filePath)) {
                fos.write(file.getBytes());
            }

            // 저장된 파일 경로를 FileVO에 설정
            fileVO.setUrl(filePath);

            return fileVO;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean deleteLocalFile(FileVO fileVO) throws Exception {
        try {
            // FileVO에 저장된 파일 경로를 가져옴
            String filePath = fileVO.getUrl();

            // 파일 객체 생성
            File file = new File(filePath);

            // 파일 존재 여부 확인 및 삭제
            if (file.exists()) {
                return file.delete(); // 파일 삭제 성공 시 true 반환
            } else {
                System.err.println("파일이 존재하지 않습니다: " + filePath);
                return false; // 파일이 없으면 false 반환
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false; // 예외 발생 시 false 반환
        }
    }

    public ResponseEntity<byte[]> downFile(FileVO fileVO) throws Exception {
        if(fileVO==null || fileVO.getId() == null) {
            return null;
    	}
    	fileVO = fileMapper.downFile(fileVO);
        // 파일 경로 및 이름 설정
        String filePath = fileVO.getUrl(); // FileVO에서 로컬 파일 경로를 가져옴
        String oriName = fileVO.getOriginName() + "." + fileVO.getExtension();

        // 파일 객체 생성
        File file = new File(filePath);

        // 파일 존재 여부 확인
        if (!file.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 파일이 없을 경우 404 반환
        }

        // 파일 읽기
        byte[] bytes;
        try (FileInputStream fis = new FileInputStream(file)) {
            bytes = fis.readAllBytes();
        }

        // 파일 이름을 UTF-8로 인코딩
        String fileName = URLEncoder.encode(oriName, "UTF-8").replaceAll("\\+", "%20");

        // HTTP 헤더 설정
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM); // 바이너리 파일 타입 설정
        httpHeaders.setContentLength(bytes.length); // 파일 크기 설정
        httpHeaders.setContentDispositionFormData("attachment", fileName); // 파일 다운로드 형식 설정

        // 응답 반환
        return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
    }
}
