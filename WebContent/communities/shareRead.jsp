<%@page import="java.text.SimpleDateFormat"%>
<%@page import="VOs.MemberVO"%>
<%@page import="VOs.CommunityShareVO"%>
<%@page import="java.util.HashMap"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>  
<%
	String contextPath = request.getContextPath();

	HashMap<String, Object> shareMap = (HashMap<String, Object>) request.getAttribute("share");

	CommunityShareVO share = (CommunityShareVO) shareMap.get("share");
	MemberVO member = (MemberVO) shareMap.get("member");
	
	String nowBlock = (String)request.getAttribute("nowBlock");
	String nowPage = (String)request.getAttribute("nowPage");
	
	String id = (String)session.getAttribute("userId");
%>

<!DOCTYPE html>
<html>

<head>
	<meta charset="UTF-8">
	<title>Share Read</title>

    <script type="text/javascript" src="https://oapi.map.naver.com/openapi/v3/maps.js?ncpClientId=ug8ym1cpbw&submodules=geocoder"></script>
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>
	<link rel="stylesheet" href="<%=contextPath%>/css/community/shareread.css">
	    
    
</head>

<body>
	<div id="top_container" align="center">
		<p class="community_p1">COMMUNITY</p>
		<p class="community_p2">나눔 / 같이 먹어요</p>
		<p>자유롭게 글을 작성해보세요</p>
	</div>
	<div id="container">
		<table width="100%">
			<tr>
				<td colspan="4" align="right">
					<input type="button" value="목록" onclick="onListButton()">
					<%
					if(id != null && id.equals(share.getId())){
						%>
						<input type="button" value="수정" onclick="onUpdateButton()">
						<input type="button" value="삭제" onclick="onDeleteButton()">
						<%
					}
					%>
				</td>
			</tr>
			<tr>
				<td>
					<div class="image_profile">
						<img src="<%= contextPath %>/images/member/userProfiles/<%= share.getId() %>/<%= member.getProfile() %>">
					</div>
				</td>
				<td>
					제목 : <%=share.getTitle() %>
				</td>
				<td>
					작성자 : <%=member.getNickname()%>
				</td>
				<td>
					<p>
						조회수 : <%=share.getViews()%>
					</p>
					<p>
						작성날짜 : <%= new SimpleDateFormat("yyyy-MM-dd").format(share.getPostDate()) %>
					</p>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<div>
						<%=share.getContents()%>
					</div>	
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<div id="imageContainer">
						<img src="<%=contextPath%>/images/community/thumbnails/<%=share.getNo()%>/<%=share.getThumbnail()%>">
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="4">
					<input type="hidden" id="lat" value="<%=share.getLat()%>">
					<input type="hidden" id="lng" value="<%=share.getLng()%>">
				
					<div id="map" style="width:100%;height:400px;"></div>
				</td>
			</tr>
			<tr>
				<td colspan="4" align="right">
					<input type="button" value="목록" onclick="onListButton()">		
				</td>
			</tr>
		</table>
	</div>
	
	<script src="<%=contextPath%>/js/community/shareRead.js"></script>

	<script>
		let lat = $("#lat").val();
		let lng = $("#lng").val();

		initialize();
		
		function initialize(){
			point = new naver.maps.Point(Number(lng), Number(lat));
			
			searchCoordinateToAddress(point);
		}
		
		function onListButton(){
			location.href='<%=contextPath%>/Community/shareList?nowBlock=<%=nowBlock%>&nowPage=<%=nowPage%>';
		}
		
		function onUpdateButton(){
			location.href='<%=contextPath%>/Community/shareUpdate?no=<%=share.getNo()%>&nowBlock=<%=nowBlock%>&nowPage=<%=nowPage%>';
		}
		
		function onDeleteButton() {
			location.href='<%=contextPath%>/Community/shareDeletePro?no=<%=share.getNo()%>';
		}
	</script>
</body>

</html>