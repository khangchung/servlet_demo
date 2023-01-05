package demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import org.json.simple.JSONObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("unchecked")
@WebServlet(urlPatterns = {"/products"}, name = "ProductServlet")
public class ProductServlet extends HttpServlet{
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductDAO productDAO = null;
		JSONObject jsonObj = new JSONObject();
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		try {
			productDAO = new ProductDAO();
		} catch (ClassNotFoundException e1) {
			resp.setStatus(500);
			e1.printStackTrace();
		}
		String parameter = req.getParameter("id");
		if (parameter == null) {
			try {
				ArrayList<Product> products = productDAO.gets();
				for (Product product : products) {
					JSONObject jsonObj2 = new JSONObject();
					jsonObj2.put("name", product.getName());
					jsonObj2.put("price", product.getPrice());
					jsonObj2.put("quantity", product.getQuantity());
					jsonObj.put(product.getId(), jsonObj2);
				}
				resp.setStatus(200);
			} catch (SQLException e) {
				resp.setStatus(500);
				e.printStackTrace();
			}
		} else {
			int id = Integer.parseInt(parameter);
			try {
				Product product = productDAO.get(id);
				jsonObj.put("id", product.getId());
				jsonObj.put("name", product.getName());
				jsonObj.put("price", product.getPrice());
				jsonObj.put("quantity", product.getQuantity());
				resp.setStatus(200);
			} catch (SQLException e) {
				resp.setStatus(500);
				e.printStackTrace();
			}
		}
		out.print(jsonObj);
		out.flush();
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductDAO productDAO = null;
		JSONObject jsonObj = new JSONObject();
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		if (req.getParameter("name") != null 
				&& req.getParameter("price") != null  
				&& req.getParameter("quantity") != null) {
			String name = req.getParameter("name");
			int price = Integer.parseInt(req.getParameter("price"));
			int quantity = Integer.parseInt(req.getParameter("quantity"));
			try {
				productDAO = new ProductDAO();
				Product product = new Product(-1, name, price, quantity);
				if (productDAO.insert(product)) {
					jsonObj.put("notification", "inserted");
					resp.setStatus(200);
				} else {
					jsonObj.put("notification", "can not insert");
					resp.setStatus(500);
				}
			} catch (ClassNotFoundException | SQLException e) {
				jsonObj.put("notification", "can not insert");
				resp.setStatus(500);
				e.printStackTrace();
			}
		} else {
			jsonObj.put("notification", "invalid parameter");
			resp.setStatus(400);
		}
		out.print(jsonObj);
		out.flush();
	}
	
	 @Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductDAO productDAO = null;
		JSONObject jsonObj = new JSONObject();
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		ServletInputStream servletInputStream = req.getInputStream();
		byte[] inBuffer = new byte[255];
		int bytesRead = servletInputStream.readLine(inBuffer, 0, inBuffer.length);
		if (req.getParameter("id") != null
				&& bytesRead > 0) {
			String[] parameters = URLDecoder.decode(new String(inBuffer, 0, bytesRead), "UTF-8").split("&");
			int id = Integer.parseInt(req.getParameter("id"));
			String name = parameters[0].split("=")[1];
			int price = Integer.parseInt(parameters[1].split("=")[1]);
			int quantity = Integer.parseInt(parameters[2].split("=")[1]);
			try {
				productDAO = new ProductDAO();
				Product product = new Product(id, name, price, quantity);
				if (productDAO.update(product)) {
					jsonObj.put("notification", "updated");
					resp.setStatus(200);
				} else {
					jsonObj.put("notification", "can not update");
					resp.setStatus(500);
				}
			} catch (ClassNotFoundException | SQLException e) {
				jsonObj.put("notification", "can not update");
				resp.setStatus(500);
				e.printStackTrace();
			}
		} else {
			jsonObj.put("notification", "invalid parameter");
			resp.setStatus(400);
		}
		out.print(jsonObj);
		out.flush();
	}
	 
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ProductDAO productDAO;
		JSONObject jsonObj = new JSONObject();
		PrintWriter out = resp.getWriter();
		resp.setContentType("application/json");
		if (req.getParameter("id") != null) {
			int id = Integer.parseInt(req.getParameter("id"));
			try {
				productDAO = new ProductDAO();
				if (productDAO.delete(id)) {
					jsonObj.put("notification", "deteled");
					resp.setStatus(200);
				} else {
					jsonObj.put("notification", "can not delete");
					resp.setStatus(500);
				}
			} catch (ClassNotFoundException | SQLException e) {
				jsonObj.put("notification", "can not delete");
				resp.setStatus(500);
				e.printStackTrace();
			}
			
		} else {
			jsonObj.put("notification", "invalid parameter");
			resp.setStatus(400);
		}
		out.print(jsonObj);
		out.flush();
	}
}
