package test.nz.ac.vuw.swen301.assignment3.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nz.ac.vuw.swen301.assignment3.server.LogEvent;
import nz.ac.vuw.swen301.assignment3.server.LogServer;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

public class WhiteBoxTests {

    @Test
    public void testDo1PostNEW(){
        ArrayList<String> events = new ArrayList<String>();
        Map<String, Object> data = new LinkedHashMap<>();
        Gson gson = new GsonBuilder().create();
        LoggingEvent loggingEvent = new LoggingEvent(null,null,null,null,null);

        //put data into the hashmap
        UUID uuid = UUID.randomUUID();
        String randomUUIDString = uuid.toString();
        data.put("id", randomUUIDString);
        data.put("message", loggingEvent.getMessage());
        long milliSeconds = System.currentTimeMillis();
        String dateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        String date = formatter.format(new Date());
        data.put("timestamp", date);
        data.put("thread", loggingEvent.getThreadName());
        data.put("logger", loggingEvent.getLogger().getClass().toString());
        data.put("level", loggingEvent.getLevel().toString());
        if(loggingEvent.getThrowableInformation() != null){
            data.put("errorDetails", loggingEvent.getThrowableInformation().toString());
        } else {
            data.put("errorDetails", "No error Details.");
        }

        String j = gson.toJson(data)+"\n";
        events.add(j);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        ObjectMapper objectMapper = new ObjectMapper();

        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(events);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        //make entity and place in request
        request.setContent(jString.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);
        ArrayList<LogEvent> serverLogs = logServer.getLogs();
    }

    @Test
    public void testDo1Post(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent e = new LogEvent("1","test","time123","methodA","logger1","DEBUG", "string");
        ArrayList<LogEvent> events = new ArrayList<LogEvent>();
        events.add(e);

        ObjectMapper objectMapper = new ObjectMapper();
        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(events);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        //request.setParameter("logs", jString);
        request.setContent(jString.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);
        ArrayList<LogEvent> serverLogs = logServer.getLogs();
        assertEquals(serverLogs.get(0).getMessage(),events.get(0).getMessage());
    }

    @Test
    public void testDoMultiplePost(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent e1 = new LogEvent("1","test","time123","methodA","logger1","DEBUG", "string");
        LogEvent e2 = new LogEvent("0","test","time123","methodA","logger1","DEBUG", "string");
        ArrayList<LogEvent> events = new ArrayList<LogEvent>();
        events.add(e1);
        events.add(e2);

        ObjectMapper objectMapper = new ObjectMapper();
        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(events);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        //request.setParameter("logs", jString);
        request.setContent(jString.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);
        ArrayList<LogEvent> serverLogs = logServer.getLogs();
        assertEquals(serverLogs.get(0).getMessage(),events.get(0).getMessage());
        assertEquals(serverLogs.get(1).getMessage(),events.get(1).getMessage());
    }

    @Test
    public void testDo1Get(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent e1 = new LogEvent("1","test","time123","methodA","logger1","DEBUG", "string");
        ArrayList<LogEvent> events = new ArrayList<LogEvent>();
        events.add(e1);

        ObjectMapper objectMapper = new ObjectMapper();
        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(events);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        //request.setParameter("logs", jString);
        request.setContent(jString.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);

        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();

        request2.setParameter("level","DEBUG");
        request2.setParameter("limit","1");

        logServer.doGet(request2,response2);

        String jsonStringLogs = response2.getHeader("logs");

        ArrayList<LogEvent> responseLogs = logServer.getLogs();
        try {
            List<LogEvent> listLogEvents = objectMapper.readValue(jsonStringLogs, new TypeReference<List<LogEvent>>(){});
            for (LogEvent event : listLogEvents){
                responseLogs.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(responseLogs.get(0).getMessage(),events.get(0).getMessage());
    }

    @Test
    public void testDoMultipleGet(){
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        LogEvent e1 = new LogEvent("1","test","time123","methodA","logger1","DEBUG", "string");
        LogEvent e2 = new LogEvent("2","test","time123","methodA","logger1","DEBUG", "string");
        ArrayList<LogEvent> events = new ArrayList<LogEvent>();
        events.add(e1);
        events.add(e2);

        ObjectMapper objectMapper = new ObjectMapper();
        String jString = "";
        try {
            jString = objectMapper.writeValueAsString(events);
        } catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }

        //request.setParameter("logs", jString);
        request.setContent(jString.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);

        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();

        request2.setParameter("level","DEBUG");
        request2.setParameter("limit","1");

        logServer.doGet(request2,response2);

        String jsonStringLogs = response2.getHeader("logs");

        ArrayList<LogEvent> responseLogs = logServer.getLogs();
        try {
            List<LogEvent> listLogEvents = objectMapper.readValue(jsonStringLogs, new TypeReference<List<LogEvent>>(){});
            for (LogEvent event : listLogEvents){
                responseLogs.add(event);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertEquals(responseLogs.get(0).getMessage(),events.get(0).getMessage());
        assertEquals(responseLogs.get(1).getMessage(),events.get(1).getMessage());
    }
}
