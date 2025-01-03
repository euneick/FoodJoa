package Services;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import Common.FileIOController;
import Common.StringParser;
import DAOs.MealkitDAO;
import VOs.MealkitReviewVO;
import VOs.MealkitVO;

public class MealkitService {
	
	private MealkitDAO mealkitDAO;
	private PrintWriter printWriter;
	
	public MealkitService() {
		
		mealkitDAO = new MealkitDAO();
	}
	
	public ArrayList<Map<String, Object>> getMealkitsList(int category) {
		
		return mealkitDAO.selectMealkits(category);
	}

	public MealkitVO getMealkitReview(HttpServletRequest request) {
		
		int no = Integer.parseInt(request.getParameter("no"));
		
		return mealkitDAO.InfoMealkitReview(no);
	}
	
	public HashMap<String, Object> getMealkit(HttpServletRequest request) {
		
		return mealkitDAO.selectMealkit(
				request.getParameter("no"),
				(String) request.getSession().getAttribute("userId"));
	}

	public MealkitVO getMealkitInfo(HttpServletRequest request) {
		
		int no = Integer.parseInt(request.getParameter("no"));
		
		mealkitDAO.incrementViewCount(no);
		
		MealkitVO mealkitVO = mealkitDAO.InfoMealkit(no);
		
		if (mealkitVO != null) {
			int result = mealkitDAO.insertRecentView(
					(String) request.getSession().getAttribute("userId"),
					mealkitVO.getNo());
		}
		
		return mealkitVO;
	}

	public ArrayList<HashMap<String, Object>> getReviewInfo(HttpServletRequest request) {
		
		int no = Integer.parseInt(request.getParameter("no"));
		
		return mealkitDAO.InfoReview(no);
	}
	
	public String getNickName(HttpServletRequest request) {
		String id = (String) request.getSession().getAttribute("userId");
		
		return mealkitDAO.selectNickName(id);
	}

	public void setWishMealkit(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {
		
		int no = Integer.parseInt(request.getParameter("no"));
		String id = (String) request.getSession().getAttribute("userId");
		
		int result = mealkitDAO.insertMealkitWishlist(no, id);
		
		String res = String.valueOf(result);

		printWriter = response.getWriter();
		printWriter.print(res);
		printWriter.flush();
		printWriter.close();
		
		return;
	}

	public int setCartMealkit(HttpServletRequest request, HttpServletResponse response) {
		
		int mealkitNo = Integer.parseInt(request.getParameter("no"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String id = (String) request.getSession().getAttribute("userId");
		
		System.out.println(quantity);
		
		return mealkitDAO.insertMealkitCartlist(mealkitNo, quantity, id);		
	}

	public void setWriteMealkit(HttpServletRequest request, HttpServletResponse response) 
	        throws ServletException, IOException {

		ServletContext application = request.getServletContext();

		String path = application.getRealPath(File.separator + "images");
		int maxSize = 1024 * 1024 * 1024;

		MultipartRequest multipartRequest = new MultipartRequest(request, path + File.separator + "temp", maxSize, "UTF-8",
				new DefaultFileRenamePolicy());
	    
	    String id = (String) request.getSession().getAttribute("userId");
	    String title = multipartRequest.getParameter("title");
	    String pictures = multipartRequest.getParameter("pictures");
	    String contents = multipartRequest.getParameter("contents");
	    int category = Integer.parseInt(multipartRequest.getParameter("category"));
	    String price = multipartRequest.getParameter("price");
	    int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
	    String orders = multipartRequest.getParameter("orders");
	    String origin = multipartRequest.getParameter("origin");        

	    MealkitVO vo = new MealkitVO();
	    vo.setId(id);
	    vo.setTitle(title);
	    vo.setPictures(pictures);
	    vo.setContents(contents);
	    vo.setCategory(category);
	    vo.setPrice(price);
	    vo.setStock(stock);
	    vo.setOrders(orders);
	    vo.setOrigin(origin);

	    int no = mealkitDAO.insertNewContent(vo);

	    List<String> fileNames = StringParser.splitString(pictures);
	    
        for(String fileName : fileNames) {
    		
    		String srcPath = path + File.separator + "temp";
    	    String destinationPath = path + File.separator + "mealkit" + File.separator + "thumbnails" + File.separator + no;
    		
    		FileIOController.moveFile(srcPath, destinationPath, fileName);
        }

        String nickName = mealkitDAO.selectNickName(id);
		
		PrintWriter printWriter = response.getWriter();
		printWriter.print(no + "," + nickName);
		printWriter.close();
	}

	public int setWriteReview(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String id = (String) request.getSession().getAttribute("userId");
		
		ServletContext application = request.getServletContext();

		String path = application.getRealPath(File.separator + "images");
		int maxSize = 1024 * 1024 * 1024;

		MultipartRequest multipartRequest = new MultipartRequest(request, path + File.separator + "temp", maxSize, "UTF-8",
				new DefaultFileRenamePolicy());
		
		String mealkitNo = multipartRequest.getParameter("mealkit_no");
        String pictures = multipartRequest.getParameter("pictures");
        String contents = multipartRequest.getParameter("contents");
        String rating = multipartRequest.getParameter("rating");
        
        System.out.println("pictures : " + pictures);
        
        List<String> fileNames = StringParser.splitString(pictures);
        
        for(String fileName : fileNames) {
    		
    		String srcPath = path + File.separator + "temp";
    		String destinationPath = path + File.separator + "mealkit" + File.separator +
    				"reviews" + File.separator + String.valueOf(mealkitNo) + File.separator + id;
    		
    		FileIOController.moveFile(srcPath, destinationPath, fileName);
        }
        
        MealkitReviewVO review = new MealkitReviewVO();
        review.setId(id);
        review.setMealkitNo(Integer.parseInt(mealkitNo));
        review.setPictures(pictures);
        review.setContents(contents);
        review.setRating(Integer.parseInt(rating));
        
		return mealkitDAO.insertNewReview(review);
	}

	public int delMealkit(HttpServletRequest request) throws IOException {
		
		String no = request.getParameter("no");
		int result = mealkitDAO.deleteMealkit(Integer.parseInt(no));
		
		if (result > 0) {
			String path = request.getServletContext().getRealPath(File.separator + "images") +
					File.separator + "mealkit" + File.separator + "thumbnails" + File.separator + no;
			FileIOController.deleteDirectory(path);
		}
		
		return result;
	}

	public void updateMealkit(HttpServletRequest request, HttpServletResponse response) throws IOException {

		ServletContext application = request.getServletContext();

		String path = application.getRealPath(File.separator + "images");
		int maxSize = 1024 * 1024 * 1024;

		MultipartRequest multipartRequest = new MultipartRequest(request, path + File.separator + "temp", maxSize, "UTF-8",
				new DefaultFileRenamePolicy());
	    
	    int no = Integer.parseInt(multipartRequest.getParameter("no"));
	    String id = (String) request.getSession().getAttribute("userId");
	    String title = multipartRequest.getParameter("title");
	    String pictures = multipartRequest.getParameter("pictures");
		String originPictures = multipartRequest.getParameter("origin_pictures");
		String originSelectedPictures = multipartRequest.getParameter("origin_selected_pictures");
	    String contents = multipartRequest.getParameter("contents");
	    String price = multipartRequest.getParameter("price");
	    String origin = multipartRequest.getParameter("origin");
	    String orders = multipartRequest.getParameter("orders");
	    int stock = Integer.parseInt(multipartRequest.getParameter("stock"));
	    
	    MealkitVO vo = new MealkitVO();
	    vo.setNo(no);
	    vo.setId(id);
	    vo.setTitle(title);
	    vo.setPictures(originSelectedPictures + pictures);
	    vo.setContents(contents);
	    vo.setPrice(price);
	    vo.setStock(stock);
	    vo.setOrders(orders);
	    vo.setOrigin(origin);

	    mealkitDAO.updateMealkit(vo);
	    
	    String srcPath = path + File.separator + "temp";
		String destinationPath = path + File.separator + "mealkit" + File.separator +
				"thumbnails" + File.separator + String.valueOf(no);
		
		List<String> originFileNames = StringParser.splitString(originPictures);
		List<String> originSelectedFileNames = StringParser.splitString(originSelectedPictures);
		
		for (String fileName : originFileNames) {
			if (!originSelectedFileNames.contains(fileName)) {
				FileIOController.deleteFile(destinationPath, fileName);
			}
		}
		
		List<String> fileNames = StringParser.splitString(pictures);

        for (String fileName : fileNames) {
    		FileIOController.moveFile(srcPath, destinationPath, fileName);
        }
		
		String nickName = mealkitDAO.selectNickName(id);
		
		PrintWriter printWriter = response.getWriter();
		printWriter.print(no + "," + nickName);
		printWriter.close();
	}

	public Map<Integer, Float> getAllRatingAvr(ArrayList<Map<String, Object>> mealkits) {
	    Map<Integer, Float> ratingMap = new HashMap<>();
	    
	    for (Map<String, Object> mealkit : mealkits) {
	        int no = (int) mealkit.get("no");
	        float ratingAvr = mealkitDAO.getRatingAvr(no);
	        
	        ratingMap.put(no, ratingAvr);
	    }
	    
	    return ratingMap;
	}


	public float getRatingAvr(MealkitVO mealkitvo) {
		int no = mealkitvo.getNo();
		float ratingAvr = mealkitDAO.getRatingAvr(no);
		
		return ratingAvr;
	}

	public ArrayList<MealkitVO> searchList(HttpServletRequest request) {
		
		String key = request.getParameter("key");
		String word = request.getParameter("word");
		
		return mealkitDAO.selectSearchList(key, word);
	}

	public String getBytePicturesParser(HttpServletRequest request) {

		String bytePictures = request.getParameter("bytePictures");
		
		List<String> fileNames = StringParser.splitString(bytePictures);
	    
	    String updatePictures = String.join(",", fileNames);
		
	    System.out.println("getBytePicures(service) : " + updatePictures);
	    
		return updatePictures;
	}

	public int buyMealkit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	    String id = (String) request.getSession().getAttribute("userId");
	    String[] mealkitNos = request.getParameterValues("mealkitNos[]");
	    String[] quantities = request.getParameterValues("quantities[]");
	    String address = request.getParameter("address");
	    String isCart = request.getParameter("isCart");

	    return mealkitDAO.insertMealkitOrder(id, mealkitNos, quantities, address, isCart);
	}

	public ArrayList<HashMap<String, Object>> getMealkitsListById(HttpServletRequest request) {
		return mealkitDAO.selectMealkitsById((String) request.getSession().getAttribute("userId"));
	}
	
	public ArrayList<Integer> getCountDelivered(HttpServletRequest request) {
	    String userId = (String) request.getSession().getAttribute("userId");
	    
	    return mealkitDAO.selectCountOrderDelivered(userId); // DAO 호출
	}

	public void delMealkitReview(HttpServletRequest request, HttpServletResponse response) 
			throws IOException {

    	int no = Integer.parseInt(request.getParameter("no"));
    	int mealkitNo = Integer.parseInt(request.getParameter("mealkitNo"));
    	
    	int result = mealkitDAO.deleteMealkitReview(no, mealkitNo);
    	
    	PrintWriter out = response.getWriter();
    	out.print(result);
    	out.close();
	}

	public String getMealkitNickName(MealkitVO mealkit) {
		return mealkitDAO.selectMealkitNickName(mealkit);
	}

	public MealkitReviewVO updateReviewMealkit(HttpServletRequest request) {
		int reviewNo = Integer.parseInt(request.getParameter("no"));
		
		return mealkitDAO.selectReview(reviewNo);
	}

	public void updateReviewpro(HttpServletRequest request) 
			throws IOException {
		
		String id = (String) request.getSession().getAttribute("userId");
		
		ServletContext application = request.getServletContext();

		String path = application.getRealPath(File.separator + "images");
		int maxSize = 1024 * 1024 * 1024;

		MultipartRequest multipartRequest = new MultipartRequest(request, path + File.separator + "temp", maxSize, "UTF-8",
				new DefaultFileRenamePolicy());
		
		String mealkitNo = multipartRequest.getParameter("mealkit_no");
		String reviewNo = multipartRequest.getParameter("review_no");
        String pictures = multipartRequest.getParameter("pictures");
		String originPictures = multipartRequest.getParameter("origin_pictures");
		String originSelectedPictures = multipartRequest.getParameter("origin_selected_pictures");
        String contents = multipartRequest.getParameter("contents");
        String rating = multipartRequest.getParameter("rating");
        
        MealkitReviewVO review = new MealkitReviewVO();
        review.setId(id);
        review.setMealkitNo(Integer.parseInt(mealkitNo));
        review.setNo(Integer.parseInt(reviewNo));
        review.setPictures(pictures + originSelectedPictures);
        review.setContents(contents);
        review.setRating(Integer.parseInt(rating));
        
        mealkitDAO.updateReview(review);
        
        String srcPath = path + File.separator + "temp";
		String destinationPath = path + File.separator + "mealkit" + File.separator +
				"reviews" + File.separator + String.valueOf(mealkitNo) + File.separator + id;
		
		List<String> originFileNames = StringParser.splitString(originPictures);
		List<String> originSelectedFileNames = StringParser.splitString(originSelectedPictures);
		
        for(String fileName : originFileNames) {
        	if (!originSelectedFileNames.contains(fileName)) {
				FileIOController.deleteFile(destinationPath, fileName);
			}
        }
        List<String> fileNames = StringParser.splitString(pictures);
    	
        for (String fileName : fileNames) {
    		FileIOController.moveFile(srcPath, destinationPath, fileName);
        }        
	}
	
	public ArrayList<HashMap<String, Object>> getPurchaseMealkits(HttpServletRequest request) {
		
		String combinedNo = request.getParameter("combinedNo");
		String combinedQuantity = request.getParameter("CombinedQuantity");
		
		String[] mealkitNos = combinedNo.split(",");
		String[] quantities = combinedQuantity.split(",");
		
		return mealkitDAO.selectPurchaseMealkits(mealkitNos, quantities);
	}
}
