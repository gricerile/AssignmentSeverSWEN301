package nz.ac.vuw.swen301.assignment3.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.swen301.assignment3.server.LogEvent;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

public class LogServer extends HttpServlet {

    private ArrayList<LogEvent> logs = new ArrayList<LogEvent>();

    //Searches for Log
    //By passing in the appropriate options, you can search for
    //available logs in the system. Logs are returned ordered by timestamp, the latest logs first.
    public void doGet(HttpServletRequest request, HttpServletResponse response){
            int num = Integer.parseInt(request.getParameter("limit"));
            String lev = request.getParameter("level");
            int check = checkInputs(num, lev);
            if (check == 400) {
                response.setStatus(check);
                return;
            }
            ArrayList<LogEvent> returnLogs = new ArrayList<LogEvent>();
            for (int i = 0; i < this.logs.size(); i++) {
                int count = 0;
                if (this.logs.get(i).getLevel().equals(lev)) {
                    LogEvent log = clone(this.logs.get(i));
                    returnLogs.add(log);
                    count++;
                }
                if (count > num) {
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
            response.setStatus(200);
            response.setContentType("text/html");
            PrintWriter out = null;
            try {
                out = response.getWriter();
            } catch (IOException e) {
                e.printStackTrace();
            }


            out.println("<html>");
            out.println("<head>");
            out.println("<title>Logs Servlet</title>");
            out.println("</head>");
            out.println("<body>");

            out.println("<h1>Logs of the Server</h1>");
            java.util.Date now = new java.util.Date();
            out.println(now);

            out.println("</body>");
            out.println("</html>");

            out.close();
    }

    public int checkInputs(int num, String lev) {
        if(num==0){
            return 400;
        }
        if(lev.equals("ALL")||lev.equals("DEBUG")||lev.equals("INFO")||lev.equals("WARN")||lev.equals("ERROR")||lev.equals("FATAL")||lev.equals("TRACE")||lev.equals("OFF")){
            return 200;
        }
        else{
            return 400;
        }
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
        String result ="";
        try {
            result = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //String jsonStringLogs = request.getParameter("logs");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<LogEvent> listLogEvents = objectMapper.readValue(result, new TypeReference<List<LogEvent>>(){});
            for (LogEvent event : listLogEvents){
                int num = check(event);
                if(num==400){//invalid item
                    response.setStatus(num);
                    return;
                }
                if(num==409){//log with id already exists
                    response.setStatus(num);
                    return;
                }
            }
            for(LogEvent event : listLogEvents){
                this.logs.add(event);
            }
            response.setStatus(201);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(400);
        }
    }

    public int check(LogEvent event) {
        for(LogEvent log : this.logs){
            if(event.getId()==log.getId()){
                return 409;
            }
        }
        if(event.getLevel().equals("ALL")||event.getLevel().equals("DEBUG")||event.getLevel().equals("INFO")||event.getLevel().equals("WARN")||event.getLevel().equals("ERROR")||event.getLevel().equals("FATAL")||event.getLevel().equals("TRACE")||event.getLevel().equals("OFF")){
            return 200;
        }
        else{
            return 400;
        }
    }

    public ArrayList<LogEvent> getLogs(){
        return logs;
    }

}
