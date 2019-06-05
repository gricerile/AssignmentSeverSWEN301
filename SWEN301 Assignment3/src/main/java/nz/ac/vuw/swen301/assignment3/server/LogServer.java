package nz.ac.vuw.swen301.assignment3.server;

import org.apache.log4j.pattern.LogEvent;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;

public class LogServer extends HttpServlet {

    private ArrayList<LogEvent> logs = new ArrayList<LogEvent>();

    //Searches for Log
    //By passing in the appropriate options, you can search for
    //available logs in the system. Logs are returned ordered by timestamp, the latest logs first.
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        //int num = Integer.parseInt(request.getParameter("limit"));
        //String lev = request.getParameter("level");
        //System.out.println(lev + " " + num);


    }

    //add log events
    //Used to store log events. Note that arrays of log events (and not just single log events) are processed.
    public void doPost(HttpServletRequest request, HttpServletResponse response){

    }

}
