package nz.ac.vuw.swen301.assignment3.server;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LogServer extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        int num = Integer.parseInt(request.getParameter("limit"));

    }
    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }
}
