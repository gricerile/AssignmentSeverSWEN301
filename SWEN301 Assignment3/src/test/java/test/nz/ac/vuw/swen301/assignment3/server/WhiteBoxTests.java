package test.nz.ac.vuw.swen301.assignment3.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.swen301.assignment3.server.LogEvent;
import nz.ac.vuw.swen301.assignment3.server.LogServer;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WhiteBoxTests {

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

        request.setParameter("logs", jString);

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

        request.setParameter("logs", jString);

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

        request.setParameter("logs", jString);

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

        request.setParameter("logs", jString);

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
