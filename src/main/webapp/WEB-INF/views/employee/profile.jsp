<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec"
           uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="kr">
<head>
    <meta charset="utf-8">
    <meta content="width=device-width, initial-scale=1.0" name="viewport">
    <title>S치킨-그룹웨어</title>
    <c:import url="../template/head.jsp"/>
</head>

<body>
<!-- ======= Header ======= -->
<c:import url="../template/header.jsp"/>
<!-- ======= Sidebar ======= -->
<c:import url="../template/sidebar.jsp"/>
<main id="main" class="main">
    <div class="pagetitle">
        <h1>프로필</h1>
    </div>
    <section class="section">
        <section class="section profile">
            <div class="row">
                <div class="col-xl-4">

                    <div class="card">
                        <div
                                class="card-body profile-card pt-4 d-flex flex-column align-items-center">
                            <img src="/fileDown?id=${detail.file.id}" alt="Profile"
                                 class="rounded-circle"
                                 onerror="this.onerror=null; this.src='/img/기본.jpg';">

                            <h2>${detail.name}</h2>
                            <h3>${detail.department.name}/${detail.position.name}</h3>
                        </div>
                    </div>

                </div>

                <div class="col-xl-8">

                    <div class="card">
                        <div class="card-body pt-3">
                            <!-- Bordered Tabs -->
                            <ul class="nav nav-tabs nav-tabs-bordered">

                                <li class="nav-item">
                                    <button class="nav-link active" data-bs-toggle="tab"
                                            data-bs-target="#profile-overview">상세내용
                                    </button>
                                </li>

                                <li class="nav-item">
                                    <button class="nav-link" data-bs-toggle="tab"
                                            data-bs-target="#profile-edit">수정
                                    </button>
                                </li>
                                <sec:authorize access="isAuthenticated()">
                                    <sec:authentication property="principal" var="emp"/>
                                <c:if test="${detail.id eq emp.id}">
                                <li class="nav-item">
                                    <a href="/employee/paystub" class="nav-link">월급명세서</a>
                                </li>
                                </c:if>
                                </sec:authorize>
                                <c:if test="${detail.id eq emp.id}">
                                <li class="nav-item">
                                    <button class="nav-link" data-bs-toggle="tab"
                                            data-bs-target="#profile-change-password">비밀번호 변경
                                    </button>
                                </li>
                                </c:if>
                                <li class="nav-item">
                                    <form action="./employeeResetPassword" method="post"
                                          id="reset">

                                        <input type="hidden" value="${detail.id}" name="hiddenId">
                                        <input type="hidden" value="${detail.dateOfEmployment}"
                                               name="dateOf">
                                        <button id="resertPassword" class="nav-link text-danger"
                                                type="submit"
                                                <sec:authorize access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">

                                                </sec:authorize>
                                                <sec:authorize access="!hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">
                                                    disabled
                                                </sec:authorize>>
                                            <sec:authorize
                                                    access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">비밀번호 초기화</sec:authorize>
                                        </button>
                                    </form>
                                </li>
                            </ul>
                            <!-- Detail -->
                            <div class="tab-content pt-2">

                                <div class="tab-pane fade show active profile-overview"
                                     id="profile-overview">
                                    <h5 class="card-title">상세 페이지</h5>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label ">사원 번호</div>
                                        <div class="col-lg-9 col-md-8">${detail.id}</div>

                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">이름</div>
                                        <div class="col-lg-9 col-md-8">${detail.name}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">입사일</div>
                                        <div class="col-lg-9 col-md-8">${detail.dateOfEmployment}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">전화번호</div>
                                        <div class="col-lg-9 col-md-8">${detail.phoneNumber}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">이메일</div>
                                        <div class="col-lg-9 col-md-8">${detail.email}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">생일</div>
                                        <div class="col-lg-9 col-md-8">${detail.residentNumber}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">부서</div>
                                        <div class="col-lg-9 col-md-8">${detail.department.name}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">직급</div>
                                        <div class="col-lg-9 col-md-8">${detail.position.name}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">은행/계좌</div>
                                        <div class="col-lg-9 col-md-8">${detail.bankName}/${detail.accountNumber}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">주소</div>
                                        <div class="col-lg-9 col-md-8">${detail.address}</div>
                                    </div>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">상세주소</div>
                                        <div class="col-lg-9 col-md-8">${detail.addressDetail}</div>
                                    </div>
                                    <input type="hidden" id="isLeaved" name="isLeaved" value="true">
                                    <sec:authorize
                                            access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">
                                        <div class="row">
                                            <div class="col-lg-3 col-md-4 label">퇴사여부</div>
                                            <div class="col-lg-9 col-md-8">${detail.isLeaved == 'true' ? '재직자' : '퇴사자'}</div>
                                        </div>
                                    </sec:authorize>

                                    <div class="row">
                                        <div class="col-lg-3 col-md-4 label">연차내역</div>
                                        <div class="col-lg-9 col-md-8">
                                            <a href="../annual/list?id=${detail.id}">연차확인</a> <input
                                                type="hidden" value="${detail.id}" name="employeeId">
                                        </div>
                                    </div>
                                    <sec:authorize
                                            access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">
                                        <div class="row">
                                            <div class="col-lg-3 col-md-4 label">연차관리</div>
                                            <div class="col-lg-9 col-md-8">
                                                <a href="#" data-bs-toggle="modal"
                                                   data-bs-target="#annualInsertModal">연차 부여</a> <input
                                                    type="hidden" value="${detail.id}" name="employeeId">
                                            </div>
                                        </div>
                                    </sec:authorize>
                                </div>

                                <div class="tab-pane fade profile-edit pt-3" id="profile-edit">

                                    <!-- Profile Edit Form -->
                                    <form action="./updateEmployee" method="post"
                                          enctype="multipart/form-data" onsubmit="return submitForm()">
                                        <div class="row mb-3">
                                            <label for="profileImage"
                                                   class="col-md-4 col-lg-3 col-form-label">프로필</label>
                                            <div class="col-md-8 col-lg-9">
                                                <img id="previewImage" src="/fileDown?id=${detail.file.id}"
                                                     alt="Profile"
                                                     onerror="this.onerror=null; this.src='/img/기본.jpg';">
                                                <div class="pt-2">
                                                    <input type="file" onchange="showUploadedPreview(this)"
                                                           id="fileInput" name="attach"
                                                           style="display: none;"/> <a href="#"
                                                                                       class="btn btn-primary btn-sm"
                                                                                       title="Upload new profile image"
                                                                                       id="uploadLink"> <i
                                                        class="bi bi-upload"></i></a>
                                                    <!-- <a href="#"
                                                        class="btn btn-danger btn-sm"
                                                        title="Remove my profile image"> <i
                                                        class="bi bi-trash"></i></a> -->
                                                </div>
                                            </div>
                                        </div>
                                        <input type="hidden" name="id" id="id" value="${detail.id}"/>
                                        <div class="row mb-3">
                                            <label for="name" class="col-md-4 col-lg-3 col-form-label">이름</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input name="name" type="text" class="form-control"
                                                       id="name" value="${detail.name}" disabled>
                                            </div>
                                        </div>


                                        <div class="row mb-3">
                                            <label for="dateOfEmployment"
                                                   class="col-md-4 col-lg-3 col-form-label">입사일</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input id="dateOfEmployment" type="text"
                                                       name="dateOfEmployment" class="form-control"
                                                       value="${detail.dateOfEmployment}" disabled>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="phoneNumber"
                                                   class="col-md-4 col-lg-3 col-form-label">전화번호</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input name="phoneNumber" type="text" class="form-control"
                                                       id="phoneNumber" value="${detail.phoneNumber}"/>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="email" class="col-md-4 col-lg-3 col-form-label">이메일</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input id="email" type="email" class="form-control"
                                                       name="email" placeholder="이메일" value="${detail.email}"/>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="residentNumber"
                                                   class="col-md-4 col-lg-3 col-form-label">생년월일</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input id="residentNumber"
                                                       type="text"
                                                       class="form-control"
                                                       name="residentNumber"
                                                       value="${detail.residentNumber}"
                                                       disabled
                                                >
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="department"
                                                   class="col-md-4 col-lg-3 col-form-label">부서</label>
                                            <div class="col-md-8 col-lg-9">
                                                <select class="form-select" id="department"
                                                        <sec:authorize access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">

                                                        </sec:authorize>
                                                        <sec:authorize
                                                                access="!hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">
                                                            disabled value="gld"
                                                        </sec:authorize>>
                                                    <option value="${detail.departmentId}">${detail.department.name}</option>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="row mb-3">
                                            <label for="team" class="col-md-4 col-lg-3 col-form-label">팀</label>
                                            <div class="col-md-8 col-lg-9">
                                                <select class="form-select" id="team" name="departmentId"
                                                        <sec:authorize access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">

                                                        </sec:authorize>
                                                        <sec:authorize
                                                                access="!hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">
                                                            disabled
                                                        </sec:authorize>>
                                                    <option value="${detail.departmentId}">${detail.department.name}</option>
                                                </select>
                                            </div>
                                        </div>


                                        <div class="row mb-3">
                                            <label for="posId" class="col-md-4 col-lg-3 col-form-label">직급</label>
                                            <div class="col-md-8 col-lg-9">
                                                <select class="form-select" id="posId" name="posId"
                                                        <sec:authorize access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">

                                                        </sec:authorize>
                                                        <sec:authorize
                                                                access="!hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">
                                                            disabled
                                                        </sec:authorize>>
                                                    <option value="${detail.posId}">${detail.position.name}</option>
                                                    <option value="2">사원</option>
                                                    <option value="3">주임</option>
                                                    <option value="4">계장</option>
                                                    <option value="5">대리</option>
                                                    <option value="6">과장</option>
                                                    <option value="7">차장</option>
                                                    <option value="8">부장</option>
                                                    <option value="9">감사</option>
                                                    <option value="10">이사</option>
                                                    <option value="11">상무</option>
                                                    <option value="12">부사장</option>
                                                    <option value="13">사장</option>
                                                </select>
                                            </div>
                                        </div>


                                        <div class="row mb-3">
                                            <label for="bankName"
                                                   class="col-md-4 col-lg-3 col-form-label">은행명</label>
                                            <div class="col-md-8 col-lg-9">
                                                <select class="form-select" id="bankName" name="bankName"
                                                        value="${detail.bankName}">
                                                    <option value="0">은행 선택</option>
                                                    <option value="국민은행">국민은행</option>
                                                    <option value="신한은행">신한은행</option>
                                                    <option value="농협">농협</option>
                                                    <option value="신협">신협</option>
                                                    <option value="기업은행">기업은행</option>
                                                    <option value="카카오뱅크">카카오뱅크</option>
                                                    <option value="토스뱅크">토스뱅크</option>
                                                </select>
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="accountNumber"
                                                   class="col-md-4 col-lg-3 col-form-label">계좌번호</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input id="accountNumber" type="text" class="form-control"
                                                       name="accountNumber" placeholder="계좌번호"
                                                       value="${detail.accountNumber}">
                                            </div>
                                        </div>

                                        <sec:authorize
                                                access="hasAnyRole('ADMIN', 'PERSONNEL_WRITER')">


                                            <div class="row mb-3">
                                                <label for="isLeaved"
                                                       class="col-md-4 col-lg-3 col-form-label">퇴사여부</label>
                                                <div class="col-md-8 col-lg-9">
                                                    <select class="form-select" id="isLeaved" name="isLeaved">
                                                        <option value="true"
                                                            ${detail.isLeaved == "true" ? 'selected' : ''}>재직자
                                                        </option>
                                                        <option value="false"
                                                            ${detail.isLeaved == "false" ? 'selected' : ''}>퇴사자
                                                        </option>

                                                    </select>
                                                </div>
                                            </div>
                                        </sec:authorize>

                                        <label for="address" class="form-label"><b>주소</b></label>
                                        <div class="form-group mb-3 d-flex">
                                            <input type="text" id="postcode" name="postcode"
                                                   placeholder="우편번호" class="form-control"
                                                   value="${detail.postcode}"> <input type="button"
                                                                                      onclick="openPostcodePopup()"
                                                                                      value="우편번호 찾기"
                                                                                      class="btn btn-primary" name="">
                                        </div>

                                        <div class="form-group mb-3 d-flex">
                                            <input type="text" id="address" name="address"
                                                   class="form-control" placeholder="주소"
                                                   value="${detail.address}"> <input type="text"
                                                                                     id="addressDetail"
                                                                                     name="addressDetail"
                                                                                     class="form-control ml-2"
                                                                                     placeholder="상세주소"
                                                                                     value="${detail.addressDetail}">
                                        </div>


                                        <div class="text-center">
                                            <input type="hidden" value="${detail.file.id}" name="fid"/>
                                            <button type="submit" class="btn btn-primary"
                                                    onclick="enableFields()">저장
                                            </button>
                                        </div>
                                    </form>
                                    <!-- End Profile Edit Form -->

                                </div>


                                <div class="tab-pane fade pt-3" id="profile-change-password">
                                    <!-- Change Password Form -->
                                    <form action="./updatePassword" method="post">

                                        <div class="row mb-3">
                                            <label for="password"
                                                   class="col-md-4 col-lg-3 col-form-label">현재 비밀번호</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input type="password" class="form-control" id="password"
                                                       name="password">
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="newPassword"
                                                   class="col-md-4 col-lg-3 col-form-label">변경할 비밀번호</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input name="newpassword" type="password"
                                                       class="form-control" id="newPassword">
                                            </div>
                                        </div>

                                        <div class="row mb-3">
                                            <label for="renewPassword"
                                                   class="col-md-4 col-lg-3 col-form-label">변경비밀번호 확인</label>
                                            <div class="col-md-8 col-lg-9">
                                                <input name="renewpassword" type="password"
                                                       class="form-control" id="renewPassword">
                                            </div>
                                        </div>
                                        <input type="hidden" value="${detail.id}" name="hiddenId">
                                        <div class="text-center col-mt-5">
                                            <button type="submit" class="btn btn-primary">Change
                                                Password
                                            </button>
                                        </div>
                                    </form>
                                    <!-- End Change Password Form -->

                                </div>

                            </div>
                            <!-- End Bordered Tabs -->

                        </div>
                    </div>

                </div>
            </div>
        </section>
    </section>
</main>
<!-- End #main -->
<!-- ======= Footer ======= -->
<c:import url="../template/footer.jsp"/>
<!-- ======= Script ======= -->
<c:import url="../template/script.jsp"/>


<div class="modal fade justify-content-center" id="annualInsertModal"
     tabindex="-1" aria-labelledby="annualInsertLabel" aria-hidden="true"
     style="margin-top: 10%">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="annualInsertLabel">연차 부여</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"
                        aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <form id="joinForm" method="POST" action="/annual/annualInsert"
                      onsubmit="return validateForm()">
                    <div class="form-group mb-3 ">
                        <label for="history" class="form-label"><b>제목</b></label> <input
                            id="history" name="history" type="text" class="form-control"
                            placeholder="사유를 적어주세요.">
                    </div>

                    <label for="remainderAnnual" class="form-label"><b>연차
                        개수</b></label>
                    <div class="input-group mb-3">
                        <button class="btn btn-primary" type="button" id="minusBtn">-</button>
                        <div class="">
                            <input type="text" class=" form-control text-center"
                                   id="remainderAnnual" name="remainderAnnual" value="0" readonly>
                        </div>
                        <button class="btn btn-primary" type="button" id="plusBtn">+</button>
                    </div>
                    <div class="form-group mb-3 ">
                        <label for="name class=" form-label"><b>이름</b></label> <input
                            readonly id="name" type="text" class="form-control"
                            value="${detail.name}">
                    </div>

                    <input type="hidden" value="0" name="isAnnual"/> <input
                        type="hidden" value="0" name="annual"/> <input type="hidden"
                                                                       value="${detail.id}" name="employeeId"/> <input
                        type="hidden"
                        value="${detail.id}" name="hiddenId">
                    <button type="submit" id="submitButton"
                            class="btn btn-primary mt-3">등록
                    </button>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
<script
        src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>

<script src="/js/employee/address.js"></script>
<script src="/js/employee/department.js">

</script>
<script src="/js/employee/password.js"></script>
<script src="/js/employee/employeeupdate.js"></script>
<!-- <script src="/js/annual/annual.js"></script> -->
<!--  	<script type="text/javascript">
		history.replaceState({}, null, location.pathname);

	</script> -->
<script>
    var posIdValue = "${detail.posId}";
    var bankNameValue = "${detail.bankName}";

    // Get the select element
    var selectElement = document.getElementById("posId");
    var bselectElement = document.getElementById("bankName");

    // Set the value of the select element to the posIdValue
    selectElement.value = posIdValue;
    bselectElement.value = bankNameValue;
    /////////////////////////////
    // Set the selected department
</script>
<script>
    function validateForm(e) {
        // 모달이 열렸을 때만 validateForm 함수 실행
        var annualInsertModal = document.getElementById('annualInsertModal');
        if (annualInsertModal && annualInsertModal.classList.contains('show')) {
            var historyInput = document.getElementById('history').value.trim();
            var remainderAnnualInput = document
                .getElementById('remainderAnnual').value.trim();

            if (historyInput === "") {
                alert("제목을 입력하세요.");
                return false;
            }
            if (remainderAnnualInput == 0) {
                alert("연차 갯수를 지정해주세요.")
                return false;
            }
        }
        return true;
    }

    document.getElementById("plusBtn").addEventListener("click", plus);
    document.getElementById("minusBtn").addEventListener("click", minus);

    function plus() {
        var input = document.getElementById('remainderAnnual');
        var currentValue = parseInt(input.value, 10);
        input.value = currentValue + 1;
    }

    function minus() {
        var input = document.getElementById('remainderAnnual');
        var currentValue = parseInt(input.value, 10);
        input.value = currentValue - 1;
    }
</script>
<script>
    function showUploadedPreview(input) {
        const file = input.files[0];
        const reader = new FileReader();

        reader.onload = function (e) {
            const img = document.getElementById('previewImage');
            img.src = e.target.result;
        };

        reader.readAsDataURL(file);
    }
</script>

</html>
