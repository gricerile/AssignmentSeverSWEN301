package nz.ac.vuw.swen301.assignment3.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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

    static Appender storage = new Appender();

    //Searches for Log
    //By passing in the appropriate options, you can search for
    //available logs in the system. Logs are returned ordered by timestamp, the latest logs first.
    public void doGet(HttpServletRequest request, HttpServletResponse response){
        if(request.getParameter("statsRequest")!=null){
            ArrayList<LogEvent> allLogsStats = getLogs();
            Gson gson = new Gson();
            String j = gson.toJson(allLogsStats);
            try {
                PrintWriter out = response.getWriter();
                response.getOutputStream().print(j);
                out.print(j);
                out.close();
                //System.out.println(j);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(request.getParameter("statsRequest")==null) {
            int num = 0;
            try {
                num = Integer.parseInt(request.getParameter("limit"));
            } catch (NumberFormatException e) {
                response.setStatus(400);
                return;
            }
            String lev = request.getParameter("level");
            int check = checkInputs(num, lev);
            if (check == 400) {
                response.setStatus(check);
                return;
            }
            ArrayList<LogEvent> returnLogs = storage.getLogs(lev, num);
            Gson gson = new Gson();
            String j = gson.toJson(returnLogs);

            response.setStatus(200);
            response.setContentType("application/json");

            try {
                PrintWriter out = response.getWriter();
                out.print(j);
                out.close();
                //System.out.println(j);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public int checkInputs(int num, String lev) {
        if(num<0){
            return 400;
        }
        if(lev.equals("ALL")||lev.equals("DEBUG")||lev.equals("INFO")||lev.equals("WARN")||lev.equals("ERROR")||lev.equals("FATAL")||lev.equals("TRACE")||lev.equals("OFF")){
            return 200;
        }
        else{
            return 400;
        }
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
        //System.out.println(result);
        List<String> resultStrings = new Gson().fromJson(result, List.class);
        //System.out.print(resultStrings.get(0));

            ArrayList<LogEvent> array = new ArrayList<LogEvent>();
            if(resultStrings==null){
                response.setStatus(400);
                return;
            }
            for(String s : resultStrings){
                //System.out.print(s);
                LogEvent l = new Gson().fromJson(s,LogEvent.class);
                array.add(l);
                //System.out.println(l.getLevel());
            }
            //System.out.println();
            int status = this.storage.append(array);
            response.setStatus(status);
    }


    public void clearStorage(){
        storage.clear();
    }
    public static ArrayList<LogEvent> getLogs(){
        return storage.getAllLogs();
    }

}
