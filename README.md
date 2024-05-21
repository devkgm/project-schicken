# 🔥 S-Chicken 그룹웨어

1. 프로젝트 설명 : 가상의 치킨 프랜차이즈 그룹웨어 입니다.

2. 팀원 : 김경모, 김범서, 남명균, 이동일
   
## ⚒️ 구현 기능

1. 전자 결재
2. 인사 관리
3. 메신저, 쪽지
4. 구매 및 판매
5. 입고 및 출고
6. 상품 관리
7. 가맹점 관리
8. 날씨 위젯

## 🔍 가맹점 조회

![가맹점 조회 gif](https://github.com/devkgm/project-schicken/assets/150644571/c33a2a29-e876-412d-9406-ee7e004f34dc)
### 설명
Kakao 지도 API를 사용했습니다. <br>
가맹점 테이블에 정의된 주소 기반으로 마커를 표시합니다.

ApexChart.js 라이브러리를 사용했습니다. <br>
일, 주, 월별 매출 추이 그래프를 렌더링하여 가맹점의 매출 추이를 한눈에 파악하도록 만들었습니다.<br>
<br>
## 🗒️ 구매 발주서 내역

![발주서 확인 gif](https://github.com/devkgm/project-schicken/assets/150644571/b7d501cc-c91f-4c45-821c-5e628c929fbe)
### 설명
발주서를 작성해 거래처에 발주를 넣는 기능입니다. <br>
발주서 버튼을 눌러 작성된 발주서를 문서로 확인 가능합니다. <br>
Handsontable.js 라이브러리를 사용해 Grid를 표현하였습니다. <br>
<br>
## ☀️ 날씨 위젯

![날씨위젯 gif](https://github.com/devkgm/project-schicken/assets/150644571/94f6ddfe-b981-4812-a2f9-02c10b579d14)
### 설명
OpenWeather API를 활용하였습니다.<br>
날씨 발표 시각에 맞춰 스케줄링 서비스에서 RestTemplate을 통해 날씨 데이터를 데이터베이스에 저장합니다. <br>
사용자 요청 시, 저장된 최신 날씨 정보를 전달합니다.<br>
