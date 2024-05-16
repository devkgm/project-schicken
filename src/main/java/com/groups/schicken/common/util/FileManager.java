package com.groups.schicken.common.util;

import com.groups.schicken.common.aws.S3Service;
import com.groups.schicken.common.vo.FileVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Component
public class FileManager {
    @Autowired
    private S3Service s3Service;
    @Autowired
    private FileMapper fileMapper;

    @Deprecated
    public boolean uploadFile(MultipartFile file, FileVO fileVO) throws Exception{
        String uid = UUID.randomUUID().toString();
        fileVO.setName(uid);
        fileVO.setOriginName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
        fileVO.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        s3Service.uploadFile(file, fileVO);
        fileMapper.uploadFile(fileVO);
        return true;
    }

    public FileVO uploadFile(MultipartFile file, Long parentId, String tableId) throws Exception{
        String uid = UUID.randomUUID().toString();
        FileVO fileVO = new FileVO();
        fileVO.setParentId(parentId);
        fileVO.setTblId(tableId);
        fileVO.setName(uid);
        fileVO.setOriginName(file.getOriginalFilename().substring(0,file.getOriginalFilename().lastIndexOf(".")));
        fileVO.setExtension(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1));
        s3Service.uploadFile(file, fileVO);
        fileMapper.uploadFile(fileVO);
        return fileVO;
    }
//    @Transactional
    public boolean deleteFile(FileVO fileVO) throws Exception {
        fileMapper.deleteFile(fileVO);
        return s3Service.deleteFile(fileVO);
    }
    public List<FileVO> getFiles() {
        return null;
    }
    public ResponseEntity<byte[]> downFile(FileVO fileVO) throws Exception{
    	if(fileVO==null || fileVO.getId() == null) {
    		return null;
    	}
    	fileVO = fileMapper.downFile(fileVO);
    	
		return s3Service.downFile(fileVO);
    	
    }
}
