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
        <div class="pagetitle d-flex justify-content-between">
            <div class="d-flex justify-content-center">
                <h1>발주서 목록</h1>

            </div>
            <a href="/order/write" class="btn btn-primary">발주서 작성</a>
        </div>
        <div class="row">
            <div class="col">
                <div class="card">
                    <c:import url="../order/orderSearch.jsp"/>
                    <div class="card-body mt-3 row">
                        <div class="p-3 d-flex" >
                            <div class="me-2" style="width: 400px;">
                                <div style="line-height: 40px; padding-bottom: 3px;">
                                    <b>목록</b>
                                </div>
                                <div class="mb-3" style="box-shadow: 0 0 0 1px #ccc inset;" >
                                    <div id="orderListContainer" ></div>
                                </div>
                            </div>
                            <div style="width: calc(100% - 400px);">
                                <div class="d-flex justify-content-between" style="line-height: 40px; padding-bottom: 3px;">
                                    <b>발주서</b>
                                    <div>
                                        <button class="btn btn-outline-primary d-none" id="receivePreviewButton">입고현황</button>
                                        <button class="btn btn-outline-primary d-none" id="orderPreviewButton">발주서</button>
                                    </div>
                                </div>
                                <div class="" style="box-shadow: 0 0 0 1px #ccc inset;" >
                                    <div id="supplierListContainer"></div>
                                </div>
                                <div class="" style="line-height: 40px; padding-bottom: 3px;">
                                    <b>상세</b>
                                </div>
                                <div class="mb-3" style="box-shadow: 0 0 0 1px #ccc inset;" >
                                    <div id="itemListContainer"></div>
                                </div>
                                <div class="d-flex justify-content-end">
                                    <div id="modifyButtons" class="d-none">
                                        <button class="btn btn-outline-primary" id="approveOrderButton">발주</button>
                                        <button class="btn btn-outline-danger" id="refuseOrderButton">반려</button>
                                    </div>
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
<script type="module" src="/js/erp/order.js"> </script>

</body>

</html>