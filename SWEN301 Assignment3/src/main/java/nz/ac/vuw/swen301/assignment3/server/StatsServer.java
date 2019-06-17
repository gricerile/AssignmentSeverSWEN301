package nz.ac.vuw.swen301.assignment3.server;

import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
//https://www.mkyong.com/java/apache-poi-reading-and-writing-excel-file-in-java/

public class StatsServer extends HttpServlet {


    private ArrayList<LogEvent> logs;

    public void doGet(HttpServletRequest request, HttpServletResponse response){
       //make work book and sheet

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Stats");

        //request to logServer
        ArrayList<LogEvent> logs = LogServer.getLogs();

        //gets different dates
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
//        for(int i=0;i<dates.size();i++) {
//            System.out.println(dates.get(i));
//        }
        //add them to columns

        //levels and log name rows added to first column

        //get tallies of different names, logs and levels for all different dates

        //add tallies into specific dates and rows

        //save file and load it
    }

    public void giveLogs(ArrayList<LogEvent> logs){
        this.logs = logs;
    }

}
