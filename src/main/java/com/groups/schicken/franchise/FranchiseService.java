package com.groups.schicken.franchise;

import com.groups.schicken.Employee.EmployeeService;
import com.groups.schicken.common.util.FileManager;
import com.groups.schicken.common.vo.FileVO;
import com.groups.schicken.common.vo.Pager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FranchiseService {
    private final FranchiseMapper franchiseMapper;
    private final FileManager fileManager;
    private final EmployeeService employeeService;
    private final PasswordEncoder passwordEncoder;

    public List<FranchiseVO> getFranchiseList(Pager pager) throws Exception {
        return franchiseMapper.getFranchiseList(pager);
    }
    public FranchiseVO getFranchise(FranchiseVO franchiseVO) throws Exception {
        franchiseVO = franchiseMapper.getFranchise(franchiseVO);
        franchiseVO.getManager().setId(franchiseVO.getManager().getId());
        franchiseVO.setManager(employeeService.userDetail(franchiseVO.getManager()));
        return franchiseVO;
    }
    public int addFranchise(FranchiseVO franchiseVO, MultipartFile[] attach) throws Exception {
        int result = 0;

        FileVO contract = new FileVO();
        FileVO register = new FileVO();
        contract.setTblId("101");
        register.setTblId("101");
        if(!fileManager.uploadFile(attach[0], contract)) return 0;

        franchiseVO.setContract(contract);
//        franchiseVO.getContract().setId(file.getId());
        if(!fileManager.uploadFile(attach[1], register)) return 0;
        franchiseVO.setRegister(register);
//        franchiseVO.getRegister().setId(file.getId());

        franchiseVO.setPassword(passwordEncoder.encode(franchiseVO.getEmail()));//초기 비밀번호는 이메일
        result += franchiseMapper.addFranchise(franchiseVO);

        return result;
    }
    public int updateFranchise(FranchiseVO franchiseVO) throws Exception {
        return franchiseMapper.updateFranchise(franchiseVO);
    }
    public int deleteFranchise(FranchiseVO franchiseVO) throws Exception {
        return franchiseMapper.deleteFranchise(franchiseVO);
    }


    public int initPassword(FranchiseVO franchiseVO) throws Exception {
        franchiseVO.setPassword(passwordEncoder.encode(franchiseVO.getEmail()));
        return franchiseMapper.updateFranchise(franchiseVO);
    }


    public int updatePassword(FranchiseVO franchiseVO) throws  Exception {
        franchiseVO.setPassword(passwordEncoder.encode(franchiseVO.getPassword()));
        return franchiseMapper.updateFranchise(franchiseVO);
    }
}
