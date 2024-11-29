package Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONObject;

import Common.NaverLoginAPI;
import DAOs.MealkitDAO;
import DAOs.MemberDAO;
import DAOs.RecipeDAO;
import Services.MemberService;
import VOs.MemberVO;

@WebServlet("/Member/*")
@MultipartConfig(location = "/tmp")
public class MemberController extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private MemberService memberService;
	private String nextPage;

	private String header;

	@Override
	public void init() throws ServletException {
		memberService = new MemberService();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doHandle(request, response);
	}

	protected void doHandle(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html; charset=utf-8");

		String action = request.getPathInfo();
		System.out.println("action : " + action);

		switch (action) {
		case "/snsjoin.me": openMemberJoinView(request, response); break; //sns회원가입화면
		case "/joinPro.me": processMemberJoin(request, response); break; //정보 다 받고 찐 가입완료
		case "/join.me": openJoinMain(request,response); break; //추가정보화면 				              
		case "/naverjoin.me": handleNaverJoin(request, response); return;
		case "/kakaojoin.me": handleKakaoJoin(request, response); return;
		case "/getUserProfile.me": NaverLoginAPI.handleNaverLogin(request, response); return;
		case "/login.me": openLoginView(request, response); break;
		case "/naverlogin.me": processNaverLogin(request, response); return;
		case "/kakaologin.me": processKakaoLogin(request, response); return;
		case "/loginPro.me": processMemberLogin(request, response); break;
		case "/logout.me" : processMemberLogOut(request, response); return;
		case "/deleteMember.me": openDeleteMember(request, response); break;
		case "/deleteMemberPro.me": processDeleteMember(request, response); return;
		case "/WishList.me": openWishList(request, response); break;
		case "/RecentList.me": openRecentList(request, response); break;	
		case "/cartList.me" : openCartList(request, response); break;

		case "/mypagemain.me": openMypagemainView(request, response); break;
		case "/update.me": openMemberUpdateView(request, response); break;
		case "/updatePro.me": processMemberUpdate(request, response); break;
		case "/viewMyDelivery.me": openMyDeliveryView(request, response); break;
		case "/viewMySend.me": openMySendView(request, response); break;
		case "/viewMyRecipe.me" : openMyRecipeView(request, response); break;
		case "/myReviews": openMyReviewsView(request, response); break;

		default:
			nextPage = "/main.jsp";
		}

		RequestDispatcher dispatcher = request.getRequestDispatcher(nextPage);
		dispatcher.forward(request, response);
	}

	
		private void openMemberJoinView(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
	
			request.setAttribute("center", "members/snsjoin.jsp");
			nextPage = "/main.jsp";
		}
		

	    private void processMemberJoin(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		System.out.println("processMemberJoin 호출됨");

		// 회원 가입 처리
		// 사용자가 입력한 추가 정보를 처리해서 DB에 저장
		if (memberService.insertMember(request)) {
			// 네이버나 카카오 로그인 후 받은 아이디를 세션에서 가져옴
			HttpSession session = request.getSession(true); // 세션이 없으면 새로 생성
			String userId = (String) session.getAttribute("userId"); // 네이버나 카카오에서 받은 아이디

			// 세션에 아이디가 없으면 예외 처리 (아이디가 없을 경우 탈퇴나 다른 처리 불가)
			if (userId == null) {
				throw new ServletException("로그인 정보가 없습니다. 로그인 후 다시 시도해주세요.");
			}

			// 회원 가입 후 아이디를 세션에 저장
			session.setAttribute("userId", userId); // 로그인한 사용자의 아이디를 세션에 저장
		    }

		    nextPage = "/main.jsp";
	    }
	
	    private void openJoinMain(HttpServletRequest request, HttpServletResponse response) {
			//request객체에 "members/join.jsp" 중앙화면 뷰 주소 바인딩
			request.setAttribute("center", "members/join.jsp");
			request.setAttribute("userId", request.getParameter("userId"));
			
			nextPage = "/main.jsp";
		}
	 
		private void handleNaverJoin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		    String naverId = processNaverJoin(request, response);
		    if (naverId != null) {
		        request.getSession().setAttribute("userId", naverId);
		        response.sendRedirect(request.getContextPath() + "/Member/join.me");
		    }
		}
		
		private String processNaverJoin(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			// 네이버 인증 후 콜백 처리
			// 네이버 아이디가 존재하면 추가 정보 입력 페이지로 리다이렉트
			// 네이버 아이디를 DB에 저장
			String naverId = memberService.insertNaverMember(request, response);

			// 네이버 아이디가 세션에 저장되지 않았거나 빈 값일 경우 처리
			if (naverId == null || naverId.trim().isEmpty()) {
				// 네이버 로그인 실패 시, 다시 네이버 로그인 페이지로 리다이렉트
				response.sendRedirect(request.getContextPath() + "/Member/snsjoin.me");
				return null;
			}

			return naverId;
		}

		
		private void handleKakaoJoin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		    String kakaoId = processKakaoJoin(request, response);
		    if (kakaoId != null) {
		        request.getSession().setAttribute("userId", kakaoId);
		        response.sendRedirect(request.getContextPath() + "/Member/join.me");
		    }

			nextPage = "/main.jsp"; // 회원가입 후 메인 페이지로 이동
		}
		
		private String processKakaoJoin(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {


			System.out.println("processKakaoJoin 호출됨");
			
	        // 요청 파라미터에서 카카오 인가 코드 받아오기
	        String code = request.getParameter("code");
	        if (code == null || code.trim().isEmpty()) {
	            System.out.println("인가 코드 없음");
	            response.sendRedirect(request.getContextPath() + "/Member/snsjoin.me"); // 로그인 페이지로 리디렉트
	            return null;
	        }

			try {
				// 카카오 로그인 API를 통해 사용자 정보 가져오기
				String kakaoId = memberService.insertKakaoMember(code);


				if (kakaoId == null || kakaoId.trim().isEmpty()) {
					// 카카오 로그인 실패 시, 로그인 페이지로 리다이렉트
					response.sendRedirect(request.getContextPath() + "/Member/join.me");
					return null;
				}

				// 카카오 ID를 request에 담기 (세션이 아닌 request로 전달)
				request.setAttribute("userId", kakaoId);

				return kakaoId;

			} catch (Exception e) {
				System.out.println("에러 발생: " + e.getMessage());
				e.printStackTrace();
				response.sendRedirect(request.getContextPath() + "/error.jsp"); // 에러 페이지로 리디렉트
				return null;
			}
		}
		
		private void openLoginView(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			request.setAttribute("center", "members/login.jsp");

			nextPage = "/main.jsp";
		}
		
		// 네이버 로그인 처리 메소드
		private void processNaverLogin(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			try {
				String userId = memberService.getNaverId(request, response); // 네이버 ID 가져오기
				handleLogin(userId, request, response); // 공통 로직 호출
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "네이버 로그인 중 오류가 발생했습니다.");
			}
		}

		// 카카오 로그인 처리 메소드
		private void processKakaoLogin(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			try {
				String code = request.getParameter("code"); // 카카오 인증 후 전달된 코드
				String userId = memberService.getKakaoId(code); // 카카오 ID 가져오기
				handleLogin(userId, request, response); // 공통 로직 호출
			} catch (Exception e) {
				e.printStackTrace();
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "카카오 로그인 중 오류가 발생했습니다.");
			}
		}
		
		private void handleLogin(String userId, HttpServletRequest request, HttpServletResponse response)
	            throws IOException, ServletException {
	        try {
	            if (userId != null) {
	                // 사용자 ID로 DB 조회 (회원 존재 여부 확인)
	                boolean isUserExists = memberService.isUserExists(userId);

	                if (isUserExists) {
	                	
	                	  HttpSession session = request.getSession();
	                      session.setAttribute("userId", userId);  // 로그인한 사용자의 아이디를 세션에 저장
	                    // 회원 존재 시 메인 페이지로 리다이렉트
	                    System.out.println("회원 존재 확인: " + userId);
	                    response.sendRedirect(request.getContextPath() + "/main.jsp"); // 메인 페이지로 리다이렉트
	                   
	                  
	                    return; // 리다이렉트 후 더 이상 코드를 실행하지 않도록 return
	                } else {
	                    // 회원이 존재하지 않으면 회원가입 페이지로 이동
	                    System.out.println("회원 존재하지 않음: " + userId);
	                    response.setContentType("text/html; charset=UTF-8");
	                    PrintWriter out = response.getWriter();
	                    out.println("<script>");
	                    out.println("alert('회원가입이 필요합니다. 회원가입 페이지로 이동합니다.');");
	                    out.println("location.href='" + request.getContextPath() + "/Member/join.me?userId=" + userId + "';");
	                    out.println("</script>");
	                    out.close();
	                    return; // 더 이상 코드를 실행하지 않도록 return
	                }
	            } else {
	                // userId가 null이면 에러 처리
	                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "아이디를 가져오는 데 실패했습니다.");
	            }
	        } catch (Exception e) {
	            // 예외 처리
	            e.printStackTrace();
	            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "로그인 처리 중 오류가 발생했습니다.");
	        }
	    }
		
		private void processMemberLogin(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			String uri = request.getRequestURI(); // 요청된 URI

			if (uri.endsWith("/naverlogin.me")) {
				processNaverLogin(request, response); // 네이버 로그인 처리
			} else if (uri.endsWith("/kakaologin.me")) {
				processKakaoLogin(request, response); // 카카오 로그인 처리
			} else {
				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
			}
		}

		private void processMemberLogOut(HttpServletRequest request, HttpServletResponse response) throws IOException {
			// 세션 무효화
			HttpSession session = request.getSession(false); // 기존 세션 가져오기
			if (session != null) {
				session.invalidate(); // 세션 무효화
			}

			// 리다이렉트: 로그아웃 후 메인 페이지로 이동
			response.sendRedirect(request.getContextPath() + "/main.jsp");
		}
		
		private void openDeleteMember(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			request.setAttribute("center", "members/deletemember.jsp");
			nextPage = "/main.jsp";
		}

		private void processDeleteMember(HttpServletRequest request, HttpServletResponse response) throws IOException {
			// 세션에서 로그인된 사용자 아이디를 가져옵니다.
			HttpSession session = request.getSession();
			String readonlyId = (String) session.getAttribute("userId");

			// 사용자가 입력한 아이디를 가져옵니다.
			String inputId = request.getParameter("inputId");

			// 아이디가 일치하지 않을 경우
			if (readonlyId == null || !readonlyId.equals(inputId)) {
				session.setAttribute("message", "입력한 아이디가 일치하지 않습니다.");
				response.sendRedirect(request.getContextPath() + "/Member/deleteMember.me"); // 탈퇴 페이지로 리다이렉트
				return;
			}

			// 서비스 레이어를 호출하여 탈퇴 처리
			boolean isDeleted = memberService.deleteMember(readonlyId);

			if (isDeleted) {
				// 탈퇴 성공 시 세션 무효화 및 메인 페이지로 이동
				session.invalidate();
				response.sendRedirect(request.getContextPath() + "/main.jsp");
			} else {
				// 탈퇴 실패 시 메시지 설정 후 탈퇴 페이지로 리다이렉트
				session.setAttribute("message", "탈퇴 처리 중 문제가 발생했습니다. 다시 시도해주세요.");
				response.sendRedirect(request.getContextPath() + "/Member/deleteMember.me");
			}

		}
		
		private void openWishList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		    // 세션에서 사용자 ID를 가져옴
		    HttpSession session = request.getSession();
		    String userId = (String) session.getAttribute("userId");

		    if (userId == null) {
		        // 사용자 ID가 없을 경우 로그인 페이지로 리다이렉트
		        response.sendRedirect(request.getContextPath() + "/login.jsp");
		        return;
		    }

		    // MemberService 인스턴스 생성 및 위시리스트 정보 가져오기
		    memberService.getWishListInfos(request, userId); // 서비스에서 레시피 및 밀키트 위시리스트 정보를 가져옴

		    // 포워딩할 페이지 설정
		    request.setAttribute("center", "members/wishlist.jsp");
		    String nextPage = "/main.jsp"; // 메인 페이지로 이동하도록 설정

		}
 
	    private void openRecentList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
	    	 // 세션에서 사용자 ID를 가져옴
	        HttpSession session = request.getSession();
	        String userId = (String) session.getAttribute("userId");

	        if (userId == null) {
	            // 사용자 ID가 없을 경우 로그인 페이지로 리다이렉트
	            response.sendRedirect(request.getContextPath() + "/login.jsp");
	            return;
	        }

	        // MemberService를 통해 최근 본 목록 데이터를 가져옴
	        try {
	            ArrayList<HashMap<String, Object>> recentList = memberService.getRecentViews(userId);
	            request.setAttribute("recentList", recentList); // 가져온 데이터를 요청 속성에 추가
	            
	            // recent.jsp로 포워딩
	            request.setAttribute("center", "members/recent.jsp");  // 중심 콘텐츠 설정
	            request.getRequestDispatcher("/main.jsp").forward(request, response);  // main.jsp에서 recent.jsp로 포워딩
	            
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            // 예외 발생 시 오류 페이지로 포워딩
	            request.setAttribute("errorMessage", "최근 본 목록을 가져오는 중 오류가 발생했습니다.");
	            request.setAttribute("center", "error.jsp");
	            request.getRequestDispatcher("/main.jsp").forward(request, response);
	            return;
	        }

	    }
		
		private void openCartList(HttpServletRequest request, HttpServletResponse response) {
		    
		    // center 부분에 해당하는 JSP 파일 설정
		    request.setAttribute("center", "members/cartlist.jsp");
		    
		    // 메인 페이지로 이동
		    nextPage = "/main.jsp";
		}

		//혜원 파트
		//------------------------------------------------------------------------------------------
		//민석 파트
		
		private void openMypagemainView(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			try {
				// 세션에서 사용자 아이디를 가져옵니다.
				HttpSession session = request.getSession();
				String userId = (String) session.getAttribute("userId");

				// 아이디가 세션에 없으면 로그인 페이지로 리다이렉트
				if (userId == null) {
					response.sendRedirect("login.jsp");
					return;
				}

				// 서비스 메서드를 호출하여 회원 정보 처리
				memberService.getMemberProfile(request, userId);

				// 처리 후 마이페이지로 이동
				request.setAttribute("center", "members/mypagemain.jsp");
				request.getRequestDispatcher("main.jsp").forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("error", "회원 프로필 처리 중 오류가 발생했습니다.");
				request.getRequestDispatcher("error.jsp").forward(request, response);
			}

		}

		private void openMemberUpdateView(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {

			MemberVO vo = memberService.getMember(request);

			request.setAttribute("vo", vo);
			request.setAttribute("center", "members/profileupdate.jsp");

			nextPage = "/main.jsp";
		}
		
		private void processMemberUpdate(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			int result = memberService.updateProfile(request);
			nextPage = "/Member/mypagemain.me";
		}

		private void openMyDeliveryView(HttpServletRequest request, HttpServletResponse response) {
			ArrayList<HashMap<String, Object>> orderedMealkitList = memberService.getDeliveredMealkit(request);
			
			request.setAttribute("orderedMealkitList", orderedMealkitList);
			request.setAttribute("center", "members/mydelivery.jsp");
			nextPage = "/main.jsp";
		}
		
		private void openMySendView(HttpServletRequest request, HttpServletResponse response){ 
			ArrayList<HashMap<String, Object>> orderedMealkitList = memberService.getSendedMealkit(request);
					  
			request.setAttribute("orderedMealkitList", orderedMealkitList); 
			request.setAttribute("center","members/sendmealkit.jsp"); 
			nextPage = "/main.jsp"; 
		}

		private void openMyRecipeView(HttpServletRequest request, HttpServletResponse response) {
			request.setAttribute("center", "members/myreceipe.jsp");
			nextPage = "/main.jsp";
		}
		
	// 건용 추가----
	private void openMyReviewsView (HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HashMap<String, Object> reviews = memberService.getReviews(request);
		
		request.setAttribute("reviews", reviews);
		
		request.setAttribute("pageTitle", "내 리뷰 관리");
		request.setAttribute("center", "members/myreview.jsp");
		
		nextPage = "/main.jsp";
	}
    
}