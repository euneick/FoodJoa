<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>마이페이지</title>
    <style>
        * {
            box-sizing: border-box;
            font-family: Arial, sans-serif;
        }
        body {
            margin: 0;
            padding: 0;
            background-color: #f2e9e1;
        }
        .container {
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            width: 100%;
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }
        .header {
            text-align: center;
            margin-bottom: 20px;
            padding: 20px;
            border-bottom: 1px solid #ccc;
        }
        .header img {
            width: 150px;
            height: auto;
        }
        .profile-section {
            text-align: center;
            margin-bottom: 20px;
        }
        .profile-image {
            width: 100px;
            height: 100px;
            border-radius: 50%;
            background-color: #f8d7da;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 10px;
        }
        .profile-info {
            font-size: 1.5em;
            color: #333;
            margin-bottom: 5px;
        }
        .profile-days {
            font-size: 18px;
            color: #666;
            margin-bottom: 15px;
        }
        .edit-btn {
            font-size: 0.9em;
            color: #007bff;
            text-decoration: underline;
            cursor: pointer;
        }
        .menu-section {
            display: flex;
            justify-content: space-around;
            margin-bottom: 20px;
        }
        .menu-item {
            text-align: center;
            color: #333;
            font-size: 1em;
        }
        .menu-item img {
            width: 80px;
            height: auto;
            margin-bottom: 5px;
        }
        .info-section {
            width: 100%;
            font-size: 0.9em;
            color: #666;
            margin-bottom: 10px;
            padding: 10px;
            border-top: 1px solid #ddd;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .info-section span {
            margin: 0 5px;
        }
    </style>
</head>
<body>

<div class="container">
    <!-- Header Section -->
   <div class="header">
    <h1>마이페이지</h1>
    <input type="button" class="logout-btn" value="로그아웃">
	</div>

    <!-- Profile Section -->
      <div class="profile-info">
            <h2>김보노님!</h2>
            <p>푸드조아와 함께한지 999일째 ♥</p>
        </div>
        <input type="button" class="edit-btn" value="정보수정">
    </div>

    <div class="manage-section">
        <div>내 레시피 관리
            <img src="../images/레시피.png" alt="레시피 이미지">
        </div>
        <div>내 상품 관리
            <img src="../images/상품사진.png" alt="상품 이미지">
        </div>
        <div>내 리뷰 관리
            <img src="../images/손모양.png" alt="리뷰 이미지">
        </div>
    </div>
</div>
    <!-- Info Section -->
    <div class="info-section">
        <div>주문/배송조회</div>
        <div>
            <span>주문건수: 0</span>
            <span>배송준비중: 1</span>
            <span>배송중: 2</span>
            <span>배송완료: 0</span>
            <span>더보기 ></span>
        </div>
    </div>

    <div class="info-section">
        <div>내 마켓상품 주문/배송조회</div>
        <div>
            <span>주문건수: 0</span>
            <span>배송준비중: 1</span>
            <span>배송중: 2</span>
            <span>배송완료: 0</span>
            <span>더보기 ></span>
        </div>
    </div>

    <div class="info-section">
        <div>※ 개인정보처리방침</div>
        <div>닫기</div>
    </div>
</div>

</body>
</html>