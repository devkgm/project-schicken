package com.groups.schicken.franchise;

import com.groups.schicken.common.vo.ResultVO;
import com.groups.schicken.common.vo.Pager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/")
@RequiredArgsConstructor
@Slf4j
public class FranchiseApi {
    private final FranchiseService franchiseService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("franchise")
    public ResponseEntity<?> getFranchise(Pager pager) throws Exception {
        List<FranchiseVO> list = franchiseService.getFranchiseList(pager);
        return ResponseEntity.ok(list);
    }

    @GetMapping("franchise/{id}")
    public ResponseEntity<ResultVO<FranchiseVO>> getFranchise (@PathVariable("id") String id) throws Exception {
        FranchiseVO vo = new FranchiseVO();
        vo.setId(id);
        try {
            vo = franchiseService.getFranchise(vo);
        } catch (Exception e){
            log.info("{}", e);
        }
        if(vo == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultVO.res(HttpStatus.BAD_REQUEST, "찾을 수 없는 가맹점.", null));
        } else {
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), vo));
        }
    }
    @PostMapping("/franchise/updatePassword")
    public ResponseEntity<ResultVO<String>> updatePassword(FranchiseVO franchiseVO, @RequestParam(value = "prevPassword", required = false) String prevPassword) throws Exception {
        FranchiseVO vo = franchiseService.getFranchise(franchiseVO);

        if(!passwordEncoder.matches(prevPassword, vo.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultVO.res(HttpStatus.BAD_REQUEST, "기존 비밀번호가 일치하지 않습니다.", null));
        }
        if(passwordEncoder.matches(franchiseVO.getPassword(), vo.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultVO.res(HttpStatus.BAD_REQUEST, "기존 비밀번호와 동일한 비밀번호입니다.", null));
        }
        int result = franchiseService.updatePassword(franchiseVO);
        if (result == 1){
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, "비밀번호 변경이 되었습니다.", null));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ResultVO.res(HttpStatus.BAD_REQUEST, "비밀번호 변경에 실패했습니다.", null));
        }
    }

}
