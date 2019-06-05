package test.nz.ac.vuw.swen301.assignment3.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nz.ac.vuw.swen301.assignment3.server.LogEvent;
import nz.ac.vuw.swen301.assignment3.server.LogServer;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.util.ArrayList;

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

    }

    @Test
    public void testDo1Get(){

    }

    @Test
    public void testDoMultipleGet(){

    }
}
