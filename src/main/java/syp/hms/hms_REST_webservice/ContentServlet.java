package syp.hms.hms_REST_webservice;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ContentServlet", value = "/ContentServlet")
public class ContentServlet extends HttpServlet {
    private DAL dal;

    public void init() {
        dal = new DAL();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("loggedIn", request.getAttribute("loggedIn"));
        try {
            request.setAttribute("anfragen", dal.getAll());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        request.getRequestDispatcher("page.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
