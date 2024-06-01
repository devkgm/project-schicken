package com.groups.schicken.erp.order;

import com.groups.schicken.Employee.EmployeeVO;
import com.groups.schicken.common.vo.ResultVO;
import com.groups.schicken.erp.supplier.SupplierVO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/")
public class HeadOrderAPI {
    private final HeadOrderService headOrderService;

    @GetMapping("orders")
    public ResponseEntity<?> getOrderList(HeadOrderVO headOrderVO, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) throws Exception {
        try {
            List<HeadOrderVO> list = headOrderService.getOrderList(headOrderVO);
            if(startDate!=null && endDate != null){
                list.removeIf(order -> order.getWriteDate().compareTo(startDate) < 0 || order.getWriteDate().compareTo(endDate) > 0);
            }
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), list));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }
    @GetMapping("orders/sups")
    public ResponseEntity<?> getOrderSupList(HeadOrderVO headOrderVO) throws Exception {
        try {
            List<HeadOrderVO> result = headOrderService.getOrderSupList(headOrderVO);
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), headOrderService.getOrderSupList(headOrderVO)));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }
    @GetMapping("orders/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) throws Exception {
        HeadOrderVO headOrderVO = new HeadOrderVO();
        headOrderVO.setId(id);
        try {
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), headOrderService.getOrder(headOrderVO)));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }
    @GetMapping("orders/{orderId}/{supplierId}")
    public ResponseEntity<?> getOrderList(@PathVariable Long orderId,@PathVariable Long supplierId) throws Exception {
        HeadOrderVO headOrderVO = new HeadOrderVO();
        SupplierVO supplierVO = new SupplierVO();
        headOrderVO.setId(orderId);
        supplierVO.setId(supplierId);
        headOrderVO.setSupplier(supplierVO);
        try {
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), headOrderService.getOrder(headOrderVO)));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    @PostMapping("orders")
    @Transactional
    public ResponseEntity<?> addOrder(@AuthenticationPrincipal EmployeeVO employeeVO, @RequestBody HeadOrderVO headOrderVO) throws Exception {
        try {
            headOrderVO.setEmployee(employeeVO);
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, "발주서 작성 완료", headOrderService.addOrder(headOrderVO)));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    @PutMapping("orders")
    public ResponseEntity<?> updateOrder(HeadOrderVO headOrderVO) throws Exception {
        try {
            int result = headOrderService.updateOrder(headOrderVO);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ResultVO.res(HttpStatus.OK, "발주 실패", null));
            } else {
                if (headOrderVO.getStatus() == 1){
                    return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, "발주 완료", null));
                } else  {
                    return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, "반려 완료", null));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    @GetMapping("orderSheets")
    public ResponseEntity<?> getOrderSheetList(HeadOrderVO headOrderVO, @RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) throws Exception {
        try {
            List<HeadOrderVO> list = headOrderService.getOrderList(headOrderVO);
            if(startDate!=null && endDate != null){
                list.removeIf(order -> order.getWriteDate().compareTo(startDate) < 0 || order.getWriteDate().compareTo(endDate) > 0);
            }
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), list));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    @GetMapping("orderSheets/{id}")
    public ResponseEntity<?> getOrderSheet(@PathVariable String id) throws Exception {
        HeadOrderVO headOrderVO = new HeadOrderVO();
        SupplierVO supplierVO = new SupplierVO();
        String[] ids = id.split("-");
        headOrderVO.setId(Long.parseLong(ids[0]));
        supplierVO.setId(Long.parseLong(ids[1]));
        headOrderVO.setSupplier(supplierVO);
        try {
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, HttpStatus.OK.toString(), headOrderService.getOrder(headOrderVO)));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }

    @PutMapping("orderDetails")
    @Transactional
    public ResponseEntity<?> updateOrderDetail(@RequestBody List<HeadOrderDetailVO> headOrderDetailVOList) throws Exception {
        try {
            return ResponseEntity.ok(ResultVO.res(HttpStatus.OK, "저장 되었습니다.", headOrderService.updateOrderDetail(headOrderDetailVOList)));
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResultVO.res(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", null));
        }
    }
}
