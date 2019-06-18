package nz.ac.vuw.swen301.assignment3.server;

import sun.rmi.runtime.Log;

import java.util.ArrayList;

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

    public int append(ArrayList<LogEvent> array) {
        int number = 0;
        for (LogEvent log : array){
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

    public void clear(){
        this.logs.clear();
    }

    public void sort(){
        this.logs.sort((o1,o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
    }
}
