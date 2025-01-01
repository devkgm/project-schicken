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
            <h1>발주</h1>
        </div>
        <div class="row">
            <div class="col">
                <div class="card">
                    <c:import url="../../erp/product/productSearch.jsp"/>
                    <div class="card-body mt-3 row">
                        <div class="p-3 d-flex" >
                            <div class="me-2" style="width: 500px;">
                                <div style="line-height: 40px; padding-bottom: 3px;">
                                    <b>목록</b>
                                </div>
                                <div class="mb-3" style="box-shadow: 0 0 0 1px #ccc inset;" >
                                    <div id="productListContainer" ></div>
                                </div>
                                <div>
                                    <button type="button" class="btn btn-outline-primary" id="productToOrderButton">추가</button>
                                    <button type="button" class="btn btn-outline-primary" id="addRowButton">행추가</button>
                                    <button type="button" class="btn btn-outline-danger" id="deleteRowButton">행삭제</button>
                                </div>
                            </div>
                            <div style="width: calc(100% - 500px);">
                                <div class="d-flex justify-content-between" style="line-height: 40px; padding-bottom: 3px;">
                                    <div>
                                        <button class="btn btn-outline-primary d-none" id="receivePreviewButton">입고현황</button>
                                    </div>
                                </div>
                                <div class="" style="line-height: 40px; padding-bottom: 3px;">
                                    <b>상세</b>
                                </div>
                                <div class="mb-3" style="box-shadow: 0 0 0 1px #ccc inset;" >
                                    <div id="orderListContainer"></div>
                                </div>
                                <div class="d-flex justify-content-end d-none" id="visibleContainer">
                                    <div class="row  me-1">
                                        <label for="comment" class="form-label text-nowrap text-end col-2 m-0 align-self-center">내용</label>
                                        <div class="col-10">
                                            <input type="text" id="comment" class="form-control" placeholder="발주내용">
                                        </div>
                                    </div>
                                    <button class="btn btn-primary" id="orderButton">저장</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
</main><!-- End #main -->
<div class="modal" tabindex="-1" id="receive-modal">
    <div class="modal-dialog modal-dialog-centered modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">입고 현황</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div class="" style="box-shadow: 0 0 0 1px #ccc inset;" >
                    <div id="receiveListContainer"></div>
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
<script type="module" src="/js/franchise/order.js"> </script>

</body>

</html>