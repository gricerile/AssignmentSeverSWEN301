package nz.ac.vuw.swen301.assignment3.server;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import sun.rmi.runtime.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
//https://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/

public class StatsServer extends HttpServlet {



    public void doGet(HttpServletRequest request, HttpServletResponse response){
       //make work book and sheet

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Stats");

        //request to logServer
        ArrayList<LogEvent> logs = LogServer.getLogs();

        //gets different dates
        ArrayList<String> dates = getDates(logs);
//        for(int i=0;i<dates.size();i++) {
//            System.out.println(dates.get(i));
//        }
        //get all different levels into cataloged arrays
        ArrayList<LogEvent> debugLogs = new ArrayList<LogEvent>();
        ArrayList<LogEvent> errorLogs = new ArrayList<LogEvent>();
        ArrayList<LogEvent> traceLogs = new ArrayList<LogEvent>();
        ArrayList<LogEvent> infoLogs = new ArrayList<LogEvent>();
        ArrayList<LogEvent> fatalLogs = new ArrayList<LogEvent>();
        ArrayList<LogEvent> warnLogs = new ArrayList<LogEvent>();

        for (LogEvent log: logs) {
            if(log.getLevel().equals("DEBUG")){
               debugLogs.add(log);
            }
            else if (log.getLevel().equals("ERROR")){
                errorLogs.add(log);
            }
            else if (log.getLevel().equals("TRACE")){
                traceLogs.add(log);
            }
            else if (log.getLevel().equals("INFO")){
                infoLogs.add(log);
            }
            else if (log.getLevel().equals("FATAL")){
                fatalLogs.add(log);
            }
            else if (log.getLevel().equals("WARN")){
                warnLogs.add(log);
            }
        }

//        System.out.println(debugLogs.size());
//        System.out.println(errorLogs.size());
//        System.out.println(fatalLogs.size());
//        System.out.println(traceLogs.size());
//        System.out.println(infoLogs.size());

        //make catalog each level array into specific date count array
        ArrayList<Integer> countDebugDates = getCountOfLevelsDates(dates, debugLogs);
        ArrayList<Integer> countErrorDates = getCountOfLevelsDates(dates, errorLogs);
        ArrayList<Integer> countTraceDates = getCountOfLevelsDates(dates, traceLogs);
        ArrayList<Integer> countInfoDates = getCountOfLevelsDates(dates, infoLogs);
        ArrayList<Integer> countFatalDates = getCountOfLevelsDates(dates, fatalLogs);
        ArrayList<Integer> countWarnDates = getCountOfLevelsDates(dates, warnLogs);
//        for(int i=0;i<countErrorDates.size();i++){
//            System.out.println(countErrorDates.get(i));
//        }

        //Find different loggers
        ArrayList<String> loggers = getLoggers(logs);
//        for(int i=0;i<loggers.size();i++){
//            System.out.println(loggers.get(i));
//        }

        //count different loggers on different dates
        Integer[][] datesWithLogCounts = getCountsForLoggerAndDates(loggers ,dates, logs);//left "values" are dates, right values are different log counts
//        for (int i = 0; i < dates.size(); i++) {
//            for (int j = 0; j < loggers.size(); j++) {
//                System.out.println(datesWithLogCounts[i][j]);
//            }
//        }

        //find different threads
        ArrayList<String> threads = getThreads(logs);
//        for(int i=0;i<threads.size();i++){
//            System.out.println(threads.get(i));
//        }

        //count different threads on different dates
        Integer[][] datesWithThreadCounts = getCountsForThreadsAndDates(threads ,dates, logs);
//        for (int i = 0; i < dates.size(); i++) {
//            for (int j = 0; j < loggers.size(); j++) {
//                System.out.println(datesWithThreadCounts[i][j]);
//            }
//        }

        //make worksheet double array with correct sizes
        int types = loggers.size() + threads.size() + 6;
        Object[][] workSheetArray = new Object[types+1][dates.size()+1];

        //add dates to columns
        for(int i=1; i<workSheetArray[0].length;i++){
            workSheetArray[0][i] = dates.get(i-1);
        }

        //add levels rows to first column and add relevant data
        workSheetArray[1][0] = "DEBUG";
        for(int i=1;i<workSheetArray[1].length;i++){
            workSheetArray[1][i] = countDebugDates.get(i-1);
        }
        workSheetArray[2][0] = "ERROR";
        for(int i=1;i<workSheetArray[2].length;i++){
            workSheetArray[2][i] = countErrorDates.get(i-1);
        }
        workSheetArray[3][0] = "TRACE";
        for(int i=1;i<workSheetArray[3].length;i++){
            workSheetArray[3][i] = countTraceDates.get(i-1);
        }
        workSheetArray[4][0] = "INFO";
        for(int i=1;i<workSheetArray[4].length;i++){
            workSheetArray[4][i] = countInfoDates.get(i-1);
        }
        workSheetArray[5][0] = "FATAL";
        for(int i=1;i<workSheetArray[5].length;i++){
            workSheetArray[5][i] = countFatalDates.get(i-1);
        }
        workSheetArray[6][0] = "WARN";
        for(int i=1;i<workSheetArray[5].length;i++){
            workSheetArray[6][i] = countWarnDates.get(i-1);
        }

        //add logger rows to first column and add relevant data
        for(int i=7;i<(7+loggers.size());i++){
            workSheetArray[i][0] = loggers.get(i-7);
            for(int j=1;j<workSheetArray[i].length;j++){
                workSheetArray[i][j] = datesWithLogCounts[j-1][i-7];
            }
            //System.out.println(workSheetArray[0][i]);
        }

        //add thread rows to first column and add relevant data
        for(int i=(7+loggers.size());i<(7+loggers.size()+threads.size());i++){
            workSheetArray[i][0] = threads.get(i-7-loggers.size());
            for(int j=1;j<workSheetArray[i].length;j++){
                workSheetArray[i][j] = datesWithThreadCounts[j-1][i-7-loggers.size()];
            }
            //System.out.println(workSheetArray[0][i]);
        }

        //save file and load it
        int rowNum = 0;

        for (Object[] datatype : workSheetArray) {
            Row row = sheet.createRow(rowNum++);
            int colNum = 0;
            for (Object field : datatype) {
                Cell cell = row.createCell(colNum++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                }
            }
        }

        try {
            FileOutputStream outputStream = new FileOutputStream("statsTable.xlsx");
            workbook.write(outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "filename=\"statsTable.xlsx\"");
        File srcFile = new File("statsTable.xlsx");
        try {
            FileUtils.copyFile(srcFile, response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        response.setStatus(200);
    }

    public Integer[][] getCountsForThreadsAndDates(ArrayList<String> threads, ArrayList<String> dates, ArrayList<LogEvent> logs) {
        Integer[][] datesWithThreadCounts = new Integer[dates.size()][threads.size()];
        for (int i = 0; i < dates.size(); i++) {
            for (int j = 0; j < threads.size(); j++) {
                datesWithThreadCounts[i][j] = 0;
            }
        }
        for(int k=0; k<logs.size(); k++) {
            for (int i = 0; i < dates.size(); i++) {
                for (int j = 0; j < threads.size(); j++) {
                    if(logs.get(k).getThread().equals(threads.get(j))){
                        if(logs.get(k).getTimestamp().contains(dates.get(i))){
                            datesWithThreadCounts[i][j] = datesWithThreadCounts[i][j] + 1;
                        }
                    }
                }
            }
        }
        return datesWithThreadCounts;
    }

    public ArrayList<String> getThreads(ArrayList<LogEvent> logs) {
        ArrayList<String> threads = new ArrayList<String>();
        for(LogEvent logevent : logs){
            String logThread = logevent.getThread();
            if(!threads.contains(logThread)){
                threads.add(logThread);
            }
        }
        return threads;
    }


    public Integer[][] getCountsForLoggerAndDates(ArrayList<String> loggers, ArrayList<String> dates, ArrayList<LogEvent> logs) {
        Integer[][] datesWithLogCounts = new Integer[dates.size()][loggers.size()];
        for (int i = 0; i < dates.size(); i++) {
            for (int j = 0; j < loggers.size(); j++) {
                datesWithLogCounts[i][j] = 0;
            }
        }
        for(int k=0; k<logs.size(); k++) {
            for (int i = 0; i < dates.size(); i++) {
                for (int j = 0; j < loggers.size(); j++) {
                    if(logs.get(k).getLogger().equals(loggers.get(j))){
                        if(logs.get(k).getTimestamp().contains(dates.get(i))){
                            datesWithLogCounts[i][j] = datesWithLogCounts[i][j] + 1;
                        }
                    }
                }
            }
        }

        return datesWithLogCounts;
    }

    public ArrayList<Integer> getCountOfLevelsDates(ArrayList<String> dates, ArrayList<LogEvent> logs){
        ArrayList<Integer> countDates = new ArrayList<Integer>();
        for(int i=0;i<dates.size();i++){
            int count = 0;
            for(LogEvent log : logs){
                if(log.getTimestamp().contains(dates.get(i))){
                    count++;
                }
            }
            countDates.add(count);
        }
        return countDates;
    }

    public ArrayList<String> getDates(ArrayList<LogEvent> logs){
        ArrayList<String> dates = new ArrayList<String>();
        for(LogEvent log : logs){
            String stamp = log.getTimestamp().substring(0,10);
            boolean contains = false;
            for(String date : dates){
                if(stamp.equals(date)){
                    contains=true;
                }
            }
            if(!contains){
                dates.add(stamp);
            }
        }
        return dates;
    }

    public ArrayList<String> getLoggers(ArrayList<LogEvent> logs){
        ArrayList<String> loggers = new ArrayList<String>();
        for(LogEvent logevent : logs){
            String logEventName = logevent.getLogger();
            if(!loggers.contains(logEventName)){
                loggers.add(logEventName);
            }
        }
        return loggers;
    }

}
