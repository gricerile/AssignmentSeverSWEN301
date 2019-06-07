package nz.ac.vuw.swen301.assignment3.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.swen301.assignment3.server.LogEvent;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class LogServer extends HttpServlet {

    private ArrayList<LogEvent> logs = new ArrayList<LogEvent>();

    //Searches for Log
    //By passing in the appropriate options, you can search for
    //available logs in the system. Logs are returned ordered by timestamp, the latest logs first.
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        int num = Integer.parseInt(request.getParameter("limit"));
        String lev = request.getParameter("level");
        ArrayList<LogEvent> returnLogs = new ArrayList<LogEvent>();
        for(int i=0;i<this.logs.size();i++){
            int count = 0;
            if(this.logs.get(i).getLevel().equals(lev)){
               LogEvent log = clone(this.logs.get(i));
               returnLogs.add(log);
               count++;
            }
            if(count > num){
                break;
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(returnLogs);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        response.setHeader("logs", jString);
    }

    private LogEvent clone(LogEvent log) {
        LogEvent newLog = new LogEvent();
        String id = log.getId();
        newLog.setId(id);
        String message = log.getMessage();
        newLog.setMessage(message);
        String timestamp = log.getTimestamp();
        newLog.setTimestamp(timestamp);
        String thread = log.getThread();
        newLog.setThread(thread);
        String logger = log.getLogger();
        newLog.setLogger(logger);
        String level = log.getLevel();
        newLog.setLevel(level);
        String errorDetails = log.getErrorDetails();
        newLog.setErrorDetails(errorDetails);
        return newLog;
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
