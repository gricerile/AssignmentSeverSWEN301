package nz.ac.vuw.swen301.assignment3.server;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.swen301.assignment3.server.LogEvent;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        String jsonStringLogs = request.getParameter("logs");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LogEvent> listLogEvents = objectMapper.readValue(jsonStringLogs, new TypeReference<List<LogEvent>>(){});
            for (LogEvent event : listLogEvents){
                logs.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<LogEvent> getLogs(){
        return logs;
    }

}
