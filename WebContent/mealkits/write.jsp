<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html; charset=utf-8");
    
    String contextPath = request.getContextPath();
    Object resultObj = request.getAttribute("result");
    int result = Integer.parseInt(resultObj.toString());
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>밀키트 판매 게시글 작성</title>
    <link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/mealkit/write.css">
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <script>
        // 사진 추가 관련 
        $(document).ready(function() {
            let pictureCount = 1;
            const maxPictures = 5;

            $('#addImage').click(function() {
                if (pictureCount < maxPictures) {
                    const newInput = `
                        <div class="image-row">
                            <input type="file" name="pictures" accept="image/*" class="additionalInput">
                            <button type="button" class="removeImage">삭제</button>
                        </div>
                    `;
                    $('#imageContainer').append(newInput);
                    pictureCount++;
                } else {
                    alert("최대 5장의 사진만 추가할 수 있습니다.");
                }
            });

            // 추가된 사진 삭제 기능
            $(document).on('click', '.removeImage', function() {
                $(this).closest('.image-row').remove();
                pictureCount--;
            });
        });
    </script>
 
</head>
<body>
    <div id="container">
        <h2>밀키트 게시글 작성</h2>
        <form action="<%=contextPath%>/Mealkit/write.pro" method="post" id="frmWrite">
            <table border="1">
	            <tr>
				    <th>ID</th>
				    <td>
				    	<!-- 로그인정보를 세션에 저장해야 쓸 수 있음 -->
				    	<!--<input type="text" name="id" value="${sessionScope.userId}" readonly>-->
				    	<input type="text" name="id" value="user1" readonly>
				    </td>
				</tr>
                <tr>
                    <th>글 제목</th>
                    <td><input type="text" name="title" required></td>
                </tr>
                <tr>
				    <th>사진 추가</th>
				    <td>
				        <input type="file" name="pictures" accept="image/*" required id="thumbnail"><br>
				        <div id="imageContainer"></div>
				        <button type="button" id="addImage">추가 사진</button>
				    </td>
				</tr>
                <tr>
                    <th>카테고리</th>
                    <td>
                        <select name="category" required>
                        	<option value="">선택하세요</option>
                            <option value="0">한식</option>
                            <option value="1">중식</option>
                            <option value="2">일식</option>
                            <option value="3">양식</option>
                            <option value="4">분식</option>
                        </select>
                    </td>
                </tr>
                <tr>
                    <th>간단 소개글</th>
                    <td><textarea name="contents" rows="4" required></textarea></td>
                </tr>
                <tr>
					<th>가격</th>
				    <td><input type="text" name="price" required min="0">원</td>
				</tr>
                <tr>
                    <th>재고 수량</th>
                    <td><input type="number" name="stock" required min="0">개</td>
                </tr>
                <tr>
					<td colspan="2">
						<p>간단 조리 순서 작성</p>
						<div class="orders-container"></div>
						<p><input type="button" class="add-orders" value="순서 추가하기"></p>
						<input type="hidden" id="orders" name="orders">
					</td>
				</tr>
                <tr>
                    <th>원산지 표기</th>
                    <td><input type="text" name="origin" required></td>
                </tr>
                <tr>
                <td colspan="2">
					<input type="button" class="write" value="작성 완료" onclick="onSubmit(event)">
				</td>
			</tr>
            </table>
        </form>
    </div>
    
    <script type="text/javascript">
    
	    $(function() {
	    	$(".write").click(function(event) {
	    		event.preventDefault();
	    	});
	    	
	    	$(".add-orders").click(function() {
	    	    var newIngredientHtml = `
	    	        <p class="orders-row">
	    	            <input type="text" class="name-orders" placeholder="조리 순서를 간단히 적어주세요.">
	    	            <button type="button" class="addrow-orders">추가</button>
	    	            <button type="button" class="cancle-orders">취소</button>
	    	        </p>
	    	    `;
	    	    
	    	    $(this).parent().prev().append(newIngredientHtml);
	    	});
			// 추가 버튼을 눌렀을 때 
	    	$(document).on('click', '.addrow-orders', function() {
	    	    var $row = $(this).closest('.orders-row');
	    	    var name = $row.find('.name-orders').val();
	    	    
	    	    if (name) {
	    	    	var newIngredientHtml = 
	    	            '<p class="added-orders">' +
	    				'<span class="added-order">' + name + '</span>' + 
	    	            '<button type="button" class="remove-orders">삭제</button>' +
	    	            '</p>';
	    	        
	    	        $row.replaceWith(newIngredientHtml);
	    	        
	    	    }
	    		else {
	    	        alert("조리 순서를 입력 해주세요.");
	    	    }
	    	});
	
	    	$(document).on('click', '.cancle-orders', function() {
	    	    $(this).closest('.orders-row').remove();
	    	});
	
	    	$(document).on('click', '.remove-orders', function() {
	    	    $(this).closest('.added-orders').remove();
	    	});
	    });
		// 전송버튼 
		function onSubmit(e) {
		    e.preventDefault();
		    
		    setOrdersString();
		    
		    showResultMessage();
		    
		  	document.getElementById('frmWrite').submit();
		};
		// 받은 조리 순서를 하나의 배열로 만드는 함수 
		function setOrdersString() {

			let orders = $(".added-order");
			let ordersString = [];
					
			orders.each(function(index, element) {
				ordersString.push($(element).text());
			});
				
			let combinedOrderString = combineStrings(ordersString);
			
			document.getElementsByName('orders')[0].value = combinedOrderString;
		}
					
		// 문자열을 합치는 함수
		function combineStrings(strings) {
			let result = '';
			     
		    for (let str of strings) {
				// 문자열 길이를 2바이트로 변환
			    let length = str.length;
		        let lengthBytes = String.fromCharCode(length >> 8, length & 0xFF);	
		        // 길이 + 문자열 추가
		        result += lengthBytes + str;
	        }
		    
			return result;
		}
		
		// 등록 되었는지 확인 알람 
		function showResultMessage() {
            var resultValue = <%= result %>;
            
            if (resultValue === 1) {
                alert("등록 성공!");
                history.back();
            } else if (resultValue === 0) {
                alert("등록 실패!");
            } else {
                alert("결과를 확인할 수 없습니다.");
            }
        }
			 
    </script>
</body>
</html>