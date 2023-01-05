package demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import org.json.simple.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/customer")
public class CustomerServlet extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		JSONObject jsonObj = new JSONObject();
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		CustomerDAO customerDAO;
		try {
			customerDAO = new CustomerDAO();
			Customer customer = customerDAO.get(username);
			if (customer != null) {
				boolean validationResult = BCrypt.checkpw(password, customer.getPassword());
				if (validationResult) {
					HttpSession session = req.getSession();
					session.setAttribute("customer", username);
					session.setAttribute("role", customer.getRole());
					jsonObj.put("notification", "authorized");
					resp.setStatus(200);
				} else {
					jsonObj.put("notification", "invalid parameter");
					resp.setStatus(400);
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			jsonObj.put("notification", "can not authorize");
			resp.setStatus(500);
			e.printStackTrace();
		}
		PrintWriter out = resp.getWriter();
		out.print(jsonObj);
		out.flush();
	}
}
