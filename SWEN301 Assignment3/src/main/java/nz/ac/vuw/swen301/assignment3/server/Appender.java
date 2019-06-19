package nz.ac.vuw.swen301.assignment3.server;

import sun.rmi.runtime.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Appender {
    private ArrayList<LogEvent> logs = new ArrayList<LogEvent>();


    public ArrayList<LogEvent> getLogs(String lev, int num) {
        ArrayList<LogEvent> returnLogs = new ArrayList<LogEvent>();
        for (int i = 0; i < this.logs.size(); i++) {
            if(returnLogs.size()<num) {
                if (this.logs.get(i).getLevel().equals(lev)) {
                    LogEvent log = this.logs.get(i);
                    returnLogs.add(log);
                }
            }
            if(returnLogs.size()>=num){
                return returnLogs;
            }
        }
        return returnLogs;
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

    public ArrayList<LogEvent> getAllLogs() {
        return this.logs;
    }

    public int check(LogEvent event) {
        for(LogEvent log : this.logs){
            if(event.getId().equals(log.getId())){
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

    public boolean incorrectIDFormatt(String id) {//returns true if no problems
        try {
            UUID.fromString(id);
            return false;
        } catch (Exception ex) {
            return true;
        }
    }

    public boolean incorrectTimestampFormatt(String timestamp) {//returns true if no problems

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        dateFormat.setLenient(false);
        Date date = null;

        try {
            date = dateFormat.parse(timestamp);
            return false;
        } catch (ParseException e) {
            return true;
        }
    }

    public int append(ArrayList<LogEvent> array) {
        int number = 0;
        ArrayList<String> IDs = new ArrayList<>();
        for (LogEvent log : array){
            if(checkNulls(log)){
                return 400;
            }
            if(incorrectTimestampFormatt(log.getTimestamp())){
                return 400;
            }
            if(incorrectIDFormatt(log.getId())){
                return 400;
            }
            if(IDs.contains(log.getId())){
                return 400;
            }
            IDs.add(log.getId());
            number = check(log);
            if(number==400){
                return 400;
            }
            if(number==409){
                //System.out.println(number);
                return 409;
            }
        }
        for(LogEvent log : array){
            this.logs.add(log);
        }
        sort();
        return 200;
    }

    private boolean checkNulls(LogEvent log) {
        if(log.getTimestamp()==null){
            return true;
        }
        if(log.getLogger()==null){
            return true;
        }
        if(log.getLevel()==null){
            return true;
        }
        if(log.getId()==null){
            return true;
        }
        if(log.getThread()==null){
            return true;
        }
        if(log.getErrorDetails()==null){
            return true;
        }
        if(log.getMessage()==null){
            return true;
        }

        return false;
    }

    public void clear(){
        this.logs.clear();
    }

    public void sort(){
        this.logs.sort((o1,o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
    }
}
