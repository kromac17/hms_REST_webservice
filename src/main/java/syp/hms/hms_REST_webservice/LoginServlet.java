package syp.hms.hms_REST_webservice;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@WebServlet(name = "LoginServlet", value = "/LoginServlet")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("loggedIn", false);
        request.setAttribute("manager", 0);
        request.getRequestDispatcher("Homepage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int managernum = Integer.parseInt(request.getParameter("mnum"));
        String password = request.getParameter("psw");


        System.out.println(managernum);
        System.out.println(password);

        DAL dal = new DAL();
        try {
            boolean loggedIn = dal.login(managernum, password);
            System.out.println(loggedIn);

            if (loggedIn){
                request.setAttribute("loggedIn", true);
                request.setAttribute("manager", managernum);

                try {
                    request.setAttribute("anfragen", dal.getAll());
                } catch (SQLException | ClassNotFoundException throwables) {
                    throwables.printStackTrace();
                }
                request.getRequestDispatcher("Homepage.jsp").forward(request, response);
            }
            else {
                request.setAttribute("error", "Password falsch!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (SQLException | NoSuchAlgorithmException | ClassNotFoundException throwables) {
            request.setAttribute("error", "Manager nicht gefunden!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}
