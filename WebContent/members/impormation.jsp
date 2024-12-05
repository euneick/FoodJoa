<%@page import="VOs.MemberVO"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
	request.setCharacterEncoding("UTF-8");
	response.setContentType("text/html; charset=utf-8");
	String contextPath = request.getContextPath();
%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<style>
/* 전체 폰트 설정 */
body {
    font-family: "Noto Serif KR", serif;
    color: #000000;
    line-height: 1.6;
    background-color: #f9f9f9;
    margin: 0;
    padding: 0;
}

/* 메뉴바 스타일 */
nav {
    background-color: #BF917E;
    color: white;
    padding: 10px 20px;
    font-size: 18px;
    text-align: center;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

nav a {
    color: white;
    text-decoration: none;
    margin: 0 15px;
    font-weight: bold;
}

nav a:hover {
    text-decoration: underline;
}

/* 메인 컨테이너 */
.container {
    width: 80%;
    margin: 20px auto;
    padding: 20px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* 기본 텍스트 스타일 */
p, li {
    margin: 10px 0;
    color: #000000;
}

/* 보조설명 텍스트 스타일 */
.small-text {
    color: #616161;
    font-size: 14px;
    margin-top: -8px;
}

/* 제목 스타일 */
h1, h2, h3 {
    color: #000000;
    margin-bottom: 15px;
    font-weight: bold;
}

/* 목록 스타일 */
ul {
    padding-left: 20px;
}

ul li {
    margin: 5px 0;
}

/* 개인정보 보호 문서의 강조 부분 */
.highlight {
    color: #BF917E;
    font-weight: bold;
}

/* 버튼 스타일 */
button {
    background-color: #BF917E;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
}

button:hover {
    background-color: #a47666;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .container {
        width: 95%;
        padding: 15px;
    }

    nav {
        font-size: 16px;
    }
}
</style>
<title>회원 정보 수정</title>
<script src="http://code.jquery.com/jquery-latest.min.js"> </script>
<div align="left" width="1200px">
 	<div>(주)푸드조아(은)는 개인정보 보호법 제30조에 따라 정보주체(고객)의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리지침을 수립․공개합니다.</div><br><br>

1. 개인정보의 처리목적 <푸드조아>은(는) 다음의 목적을 위하여 개인정보를 처리하고 있으며, 다음의 목적 이외의 용도로는 이용하지 않습니다.<br>

   - 고객 가입의사 확인, 고객에 대한 서비스 제공에 따른 본인 식별․인증, 회원자격 유지․관리, 물품 또는 서비스 공급에 따른 금액 결제, 물품 또는 서비스의 공급․배송 등<br><br>

 

2. 개인정보의 처리 및 보유기간 ① <푸드조아>은(는) 정보주체로부터 개인정보를 수집할 때 동의받은 개인정보 보유․이용기간 또는 법령에 따른 개인정보 보유․이용기간 내에서 개인정보를 처리․보유합니다.<br>

   ② 구체적인 개인정보 처리 및 보유 기간은 다음과 같습니다.<br>

    - 고객 가입 및 관리 : 서비스 이용계약 또는 회원가입 해지시까지, 다만 채권․채무관계 잔존시에는 해당 채권․채무관계 정산시까지<br>

    - 전자상거래에서의 계약․청약철회, 대금결제, 재화 등 공급기록 : 5년<br><br>

 

3. 개인정보의 제3자 제공 <푸드조아>은(는) 정보주체의 별도 동의, 법률의 특별한 규정 등 개인정보 보호법 제17조에 해당하는 경우 외에는 개인정보를 제3자에게 제공하지 않습니다.<br><br>

 

4. 개인정보처리의 위탁 ① <푸드조아>은(는) 원활한 개인정보 업무처리를 위하여 다음과 같이 개인정보 처리업무를 외부에 위탁하고 있습니다.<br>

    - A/S 센터 운영<br>

      ․위탁받는 자 (수탁자) : OO 전자<br>

      ․위탁하는 업무의 내용 : 고객 대상 제품 A/S 제공<br>

   ② <소상공인명>은(는) 위탁계약 체결시 개인정보 보호법 제25조에 따라 위탁업무 수행목적 외 개인정보 처리금지, 재위탁 제한, 수탁자에 대한 관리․감독, 책임에 관한 사항을 문서에 명시하고, 수탁자가 개인정보를 안전하게 처리하는지를 감독하고 있습니다.<br><br>

5. 정보주체와 법정대리인의 권리․의무 및 행사방법 정보주체는 <소상공인명>에 대해 언제든지 다음 각 호의 개인정보 보호 관련 권리를 행사할 수 있습니다.<br>

    1. 개인정보 열람요구<br>

    2. 개인정보에 오류 등이 있을 경우 정정 요구<br>

    3. 삭제요구<br>

    4. 처리정지 요구  <br><br>

 

 6. 처리하는 개인정보 항목 <푸드조아>은(는) 다음의 개인정보 항목을 처리하고 있습니다.<br>

 
   - 성명, 생년월일, 주소, 전화번호, 휴대전화번호, 성별, 이메일주소, 신용카드번호, 은행계좌번호 등 결제정보<br><br>

 

 7. 개인정보의 파기 ① <푸드조아>은(는) 개인정보 보유기간의 경과, 처리목적 달성 등 개인정보가 불필요하게 되었을 때에는 지체없이 해당 개인정보를 파기합니다.<br>

   ② <푸드조아>은(는) 다음의 방법으로 개인정보를 파기합니다.<br>

      - 전자적 파일 : 파일 삭제 및 디스크 등 저장매체 포맷<br>

      - 수기(手記) 문서 : 분쇄하거나 소각<br><br>

 

 8. 개인정보의 안전성 확보조치 <푸드조아>은(는) 개인정보의 안전성 확보를 위해 다음과 같은 조치를 취하고 있습니다.<br>

    - 관리적 조치 : 내부관리계획 수립․시행, 직원․종업원 등에 대한 정기적 교육<br>

    - 기술적 조치 : 개인정보처리시스템(또는 개인정보가 저장된 컴퓨터)의 비밀번호 설정 등 접근권한 관리, 백신소프트웨어 등 보안프로그램 설치, 개인정보가 저장된 파일의 암호화<br>

    - 물리적 조치 : 개인정보가 저장․보관된 장소의 시건, 출입통제 등<br><br>

 

 9. 개인정보 자동 수집 장치의 설치∙운영 및 거부에 관한 사항<br>      

   ① <푸드조아>은(는) 이용자에게 개별적인 맞춤서비스를 제공하기 위해 이용정보를 저장하고 수시로 불러오는 ‘쿠기(cookie)’를 사용합니다.<br>

   ② 쿠키는 웹사이트를 운영하는데 이용되는 서버(http)가 이용자의 컴퓨터 브라우저에게 보내는 소량의 정보이며 이용자들의 PC 컴퓨터내의 하드디스크에 저장되기도 합니다.<br>

      가. 쿠키의 사용목적: 이용자가 방문한 각 서비스와 웹 사이트들에

     대한 방문 및 이용형태, 인기 검색어, 보안접속 여부, 등을 파악하여

      이용자에게 최적화된 정보 제공을 위해 사용됩니다.<br>

      나. 쿠키의 설치∙운영 및 거부 : 웹브라우저 상단의 도구>인터넷 옵션>개인정보 메뉴의 옵션 설정을 통해 쿠키 저장을 거부 할 수 있습니다.<br>

      다. 쿠키 저장을 거부할 경우 맞춤형 서비스 이용에 어려움이 발생할 수 있습니다.<br><br>

 

 10. 개인정보 보호책임자 <푸드조아>은(는) 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제를 처리하기 위하여 아래와 같이 개인정보 보호책임자를 지정하고 있습니다.<br>

   ▶ 개인정보 보호책임자 (사업주 또는 대표자)<br>

       성명 : OOO          직책 : OOO<br>

       연락처 : <전화번호>, <이메일>, <팩스번호><br><br>

 

 11. 개인정보 처리방침 변경 이 개인정보 처리방침은 20XX. X. X.부터 적용됩니다. <푸드조아>(은)는 개인정보 보호법 제30조에 따라 정보주체(고객)의 개인정보를 보호하고 이와 관련한 고충을 신속하고 원활하게 처리할 수 있도록 하기 위하여 다음과 같이 개인정보 처리지침을 수립․공개합니다.</div><br><br>

</div> 
</body>
</html>
