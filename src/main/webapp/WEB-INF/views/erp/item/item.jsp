<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>S치킨-그룹웨어</title>
    <c:import url="../../template/head.jsp"/>
</head>

<body>
<!-- ======= Header ======= -->
<c:import url="../../template/header.jsp"/>
<!-- ======= Sidebar ======= -->
<c:import url="../../template/sidebar.jsp"/>
<main id="main" class="main">
    <section class="section erp ms-auto me-auto">
        <div class="pagetitle">
            <h1>계약품목 관리</h1>
        </div>
        <div class="row">
            <div class="col">
                <div class="card">
                    <c:import url="./itemSearch.jsp"/>
                    <div class="card-body mt-3 row">
                        <div class="p-3 d-flex flex-column" >
                            <div >
                                <div style="line-height: 40px; padding-bottom: 3px;">
                                    <b>목록</b>
                                </div>
                                <div class="mb-3" style="box-shadow: 0 0 0 1px #ccc inset;" >
                                    <div id="example"></div>
                                </div>
                            </div>
                            <div class="d-flex justify-content-start">
                                <button id="addButton" class="btn btn-outline-primary me-1"><i class="bi bi-database-add"></i>신규</button>
                                <button id="editButton" class="btn btn-outline-primary me-1" ><i class="bi bi-pencil"></i>수정</button>
                                <button id="exportButton" class="btn btn-primary"><i class="bi bi-file-earmark-spreadsheet-fill"></i> 저장</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main><!-- End #main -->
<div class="modal" tabindex="-1" id="register-modal">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title text-nowrap">상품 추가</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="stepwizard mb-3" >
                    <div class="nav stepwizard-row row">
                        <div class="stepwizard-step col-4">
                            <button type="button" class="btn active btn-circle" data-bs-toggle="tab" data-bs-target="#product-select">1</button>
                            <p>품목</p>
                        </div>
                        <div class="stepwizard-step col-4">
                            <button type="button" class="btn btn-circle" data-bs-toggle="tab" data-bs-target="#supplier-select">2</button>
                            <p>거래처</p>
                        </div>
                        <div class="stepwizard-step col-4">
                            <button type="button" class="btn btn-circle" data-bs-toggle="tab" data-bs-target="#item-select">3</button>
                            <p>상세</p>
                        </div>
                    </div>
                </div>
                    <div class="position-relative" style="height: 50vh">
                        <div class="tab-content pt-2">
                            <div class="tab-pane fade show active" id="product-select" >
                                <c:import url="../../erp/product/productSearch.jsp"/>
                                <div style="box-shadow: 0 0 0 1px #ccc inset;">
                                    <div id="productContainer" class="overflow-auto"></div>
                                </div>
                                <div class="position-absolute end-0 bottom-0 nav">
                                    <button type="button" class="btn btn-primary" id="nextButton1">다음</button>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="supplier-select">
                                <c:import url="../../erp/supplier/supplierSearch.jsp"/>
                                <div style="box-shadow: 0 0 0 1px #ccc inset;">
                                    <div id="supplierContainer" class="overflow-auto mb-3"></div>
                                </div>
                                <div class="position-absolute end-0 bottom-0 nav">
                                    <button type="button" class="btn btn-primary" id="nextButton2">다음</button>
                                </div>
                            </div>
                            <div class="tab-pane fade" id="item-select">
                                <form id="addForm">
                                    <div id="itemContainer">
                                        <div class="row">
                                            <div class="col-6">
                                                <div class="mb-3 row">
                                                    <label for="addName" class="col-4 col-form-label">품목명</label>
                                                    <div class="col-8">
                                                        <input type="hidden" class="form-control" id="addName" name="product.id" sw="value_productId">
                                                        <input type="text" disabled class="form-control" id="addName" name="product.name" sw="value_productName">
                                                    </div>
                                                </div>
                                                <div class="mb-3 row">
                                                    <label for="addName" class="col-4 col-form-label">규격</label>
                                                    <div class="col-8">
                                                        <input type="text" disabled class="form-control"  sw="value_productStandard">
                                                    </div>
                                                </div>
                                                <div class="mb-3 row">
                                                    <label for="addName" class="col-4 col-form-label">단위</label>
                                                    <div class="col-8">
                                                        <input type="text" disabled class="form-control" sw="value_productUnitName">
                                                    </div>
                                                </div>
                                                <div class="mb-3 row">
                                                    <label for="addStandard" class="col-4 col-form-label">거래처</label>
                                                    <div class="col-8">
                                                        <input type="hidden" class="form-control" id="addStandard" name="supplier.id" sw="value_supplierId">
                                                        <input type="text" disabled class="form-control" id="addStandard" name="supplier.name" sw="value_supplierName">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-6">
                                                <div class="mb-3 row">
                                                    <label for="" class="col-4 col-form-label">판매단가</label>
                                                    <div class="col-8">
                                                        <input type="text" disabled class="form-control" sw="value_productSellPrice">
                                                    </div>
                                                </div>
                                                <div class="mb-3 row">
                                                    <label for="editName" class="col-4 col-form-label">계약단가</label>
                                                    <div class="col-8">
                                                        <input type="text" class="form-control" name="contractPrice">
                                                    </div>
                                                </div>
                                                <div class="mb-3 row">
                                                    <label for="editOwnerName" class="col-4 col-form-label">최소구매수량</label>
                                                    <div class="col-8">
                                                        <input type="text" class="form-control" name="minQuantity" value="1">
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>

                    </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="addSubmitButton">저장</button>
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
<div class="modal" tabindex="-1" id="edit-modal">
    <div class="modal-dialog  modal-dialog-centered ">
        <div class="modal-content">
            <div class="modal-header pb-0">
                <ul class="nav nav-tabs nav-tabs-bordered">
                    <li class="nav-item">
                        <button class="nav-link active" data-bs-toggle="tab" data-bs-target="#supplier-overview">품목 정보</button>
                    </li>
                    <li class="nav-item">
                        <button class="nav-link" data-bs-toggle="tab" data-bs-target="#profile-edit">공급 거래처</button>
                    </li>
                </ul>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="position-relative" style="height: 50vh">
                    <div class="tab-content pt-2">
                        <div class="tab-pane fade show active profile-overview" id="supplier-overview">
                            <form id="editForm" class="text-nowrap text-end">
                                <input type="hidden" name="id" sw="value_id"/>
                                <div class="row">
                                    <div class="col-6">
                                        <div class="mb-3 row">
                                            <label for="addName" class="col-4 col-form-label">품목명</label>
                                            <div class="col-8">
                                                <input type="text" disabled class="form-control"  sw="value_productName">
                                            </div>
                                        </div>
                                        <div class="mb-3 row">
                                            <label for="addName" class="col-4 col-form-label">규격</label>
                                            <div class="col-8">
                                                <input type="text" disabled class="form-control"  sw="value_productStandard">
                                            </div>
                                        </div>
                                        <div class="mb-3 row">
                                            <label for="addName" class="col-4 col-form-label">단위</label>
                                            <div class="col-8">
                                                <input type="text" disabled class="form-control" sw="value_productUnitName">
                                            </div>
                                        </div>
                                        <div class="mb-3 row">
                                            <label for="addStandard" class="col-4 col-form-label">거래처</label>
                                            <div class="col-8">
                                                <input type="text" disabled class="form-control" id="addStandard" sw="value_supplierName">
                                            </div>
                                        </div>
                                    </div>
                                    <div class="col-6">
                                        <div class="mb-3 row">
                                            <label for="" class="col-4 col-form-label">판매단가</label>
                                            <div class="col-8">
                                                <input type="text" disabled class="form-control" sw="value_productSellPrice">
                                            </div>
                                        </div>
                                        <div class="mb-3 row">
                                            <label for="editName" class="col-4 col-form-label">계약단가</label>
                                            <div class="col-8">
                                                <input type="text" class="form-control" name="contractPrice" sw="value_contractPrice">
                                            </div>
                                        </div>
                                        <div class="mb-3 row">
                                            <label for="editOwnerName" class="col-4 col-form-label">최소구매수량</label>
                                            <div class="col-8">
                                                <input type="text" class="form-control" name="minQuantity" sw="value_minQuantity">
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-12">
                                        <div class="mb-3 row">

                                        </div>
                                        <div class="mb-3 row">

                                        </div>
                                        <div class="mb-3 row">

                                        </div>
                                    </div>
                                </div>
                            </form>
                            <div class="position-absolute end-0 bottom-0">
                                <button type="button" class="btn btn-primary" id="editSubmitButton">수정</button>
                            </div>
                        </div>
                    </div>

                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-outline-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>
<!-- ======= Footer ======= -->
<c:import url="../../template/footer.jsp"/>
<!-- ======= Script ======= -->
<c:import url="../../template/script.jsp"/>
<script src="/js/erp/item.js" type="module"></script>
</body>

</html>