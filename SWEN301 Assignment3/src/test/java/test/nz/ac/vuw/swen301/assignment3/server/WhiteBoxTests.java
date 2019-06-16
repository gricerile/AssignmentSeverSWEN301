package test.nz.ac.vuw.swen301.assignment3.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WhiteBoxTests {

    private final static String SINGLELOG = "[\"{\\\"id\\\":\\\"96898390-4676-459f-b437-ac10cb4a9320\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.431Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\"]";
    private final static String MANYLOGS = "[\"{\\\"id\\\":\\\"96898390-4676-459f-b437-ac10cb4a9320\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.431Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"05b17e16-2d67-496b-ac99-0ecb7fa5984d\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.438Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"9a339a3d-c3fe-4842-abd3-a46ba6156e7f\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.439Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"0a2d0b8a-52c9-4366-a709-d432f23343cc\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.439Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"9f89ec62-e110-489e-b387-1e0e4cf77232\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.439Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"f603b1da-9be6-4b5f-a4a3-3d5e5ae783d6\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.439Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"754be09b-4be4-449a-b698-62248331eb68\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.439Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"bbd96d06-15aa-4055-a16b-5b36748fb386\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.440Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"7e3fc92b-5124-44f5-b7a4-8fdaf6d186eb\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.440Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"a55f0b57-147f-4e22-8920-1aad4e50889f\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.440Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"83df4042-6bf6-4f8c-95ca-2d15613784ad\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.440Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"ec487a32-0d84-44c8-aede-0fce07adac54\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.441Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"72b30415-cca8-40af-94c8-1876ee47a20d\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.441Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"aba331d7-280c-489a-8be0-441cef9d8322\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.443Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"7de61f66-fd38-4b69-9738-e6f88497c265\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.443Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"fe243db2-e55c-4785-8881-96f701d9866e\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.444Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"e5df3c0b-463a-4eb8-b3fd-f3ade96f3d93\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.445Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"bf908a83-4c2b-4631-8d46-c142e65666e5\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.445Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"4ccb2676-5ac8-44b3-9a08-2421a9a9f048\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.445Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"e7c5238c-10bb-468d-91f6-388350a373cc\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.445Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"03914eb4-e69f-4ea9-aa88-4b380885d1c9\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.446Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"5aed2a98-7db5-46a2-b95e-cbc0cd18d7de\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.446Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"1d4bde49-c1c4-4c52-afeb-d59ea753e635\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.446Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"c164211c-abd1-47f2-aeae-58950c78e6f6\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.446Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"c547fc76-f38f-43b9-bf80-afe0c2e1db01\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.446Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"7a656018-fd51-4615-a690-56740d873da7\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.446Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"b95dbd12-f3f6-4f28-8829-97f7fabe4d00\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.447Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"54e15a58-4f03-44d9-838f-e8a1796bcb66\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.447Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"c2590cd8-68e2-4022-b295-2a3dfa51e9aa\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.447Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"2e626422-6ef4-4ee9-8bad-4fe6f365ab4f\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.447Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"c295c97e-427a-4b90-8a81-563cc31c1512\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.447Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"92dc754c-b5a0-42bd-a638-1b1cf42e3ab7\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.447Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"764a3bfc-d14b-4ad3-a404-ef90c7539884\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.448Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"6b41782f-36f2-448e-9194-814ba4723a56\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.448Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"465a1b22-1d47-4a1a-a6d1-019b869ec5ea\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.448Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"886b8950-e5ef-4888-8bae-d384c171a5f7\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.448Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"ccaf2cea-aa57-4e0b-b0fd-c485af9b7da8\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.448Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"5d637a79-d8e4-4ed8-b28b-0107b4214020\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.449Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"baaf016c-7810-4227-83b7-cd097fe70b1c\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.449Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"2985d2db-8496-46fe-9293-661460557d19\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.449Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"a31632b4-1bca-456f-8f3f-2465ad1ae100\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.449Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"92519a39-710d-4844-a6e6-904eb8abb2a1\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.450Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"9353f9b3-139f-41fb-afe5-4ea8c34f9814\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.450Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"f84f1960-5d50-4e99-a57d-dcfc76dbdd2e\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.450Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"79576a22-4798-483a-934f-2d3cfd3de325\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.450Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"ff9c5b79-8025-4a5d-a1a3-bd9d81dbecdf\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.451Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"a50958d9-4c2d-4684-aea9-86d47b077863\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.451Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"cfa6400e-09d4-4fad-9d21-fc14a8b76df7\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.451Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"3dce71bd-7b8f-42ce-8877-96dbfb6f0671\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.451Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"fa58d156-d9c8-4ef7-bcfc-75bb2f9b201e\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.451Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"85336568-dff3-4786-bc87-9a607e86ff47\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.452Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"f27ad36f-88f9-40ee-bcf2-e4c14a4e34be\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.452Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"400206c1-0b24-4dd8-a139-8db1665e2b8c\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.452Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"cd3856f6-ad38-4f3e-8fc3-1312bee9d9e0\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.452Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"c8ccfffb-2517-4c0f-bd6d-a1ba50138982\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.453Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"FATAL\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"156ddbb0-9231-4798-bf9f-b4553484676a\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.453Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"ERROR\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"e0cc9b1a-d1c6-412b-9141-c3272a95976b\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.453Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"WARN\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"922046d1-d1bb-48c1-93ad-4d1e936ec5b1\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.453Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"INFO\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"91232997-5af1-43a4-b413-17a296f78974\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.453Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"DEBUG\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\",\"{\\\"id\\\":\\\"e81a1e5f-bc2a-4c02-8978-f0b97fb10eec\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.454Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"TRACE\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\"]";

    @Test
    public void testDo1Post(){
        Map<String, Object> data = new LinkedHashMap<>();
        Gson gson = new GsonBuilder().create();


        String j = SINGLELOG;
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);
        assertEquals(200, response.getStatus());
        ArrayList<LogEvent> serverLogs = logServer.getLogs();
        assertEquals(serverLogs.get(0).getLevel(), "FATAL");
    }

    @Test
    public void testDoMultiplePost(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);
        ArrayList<LogEvent> serverLogs = logServer.getLogs();
        assertEquals(serverLogs.get(0).getLevel(), "TRACE");
        assertEquals(serverLogs.get(1).getLevel(), "FATAL");
    }

    @Test
    public void testSameIDDoPost(){
        Map<String, Object> data = new LinkedHashMap<>();
        Gson gson = new GsonBuilder().create();


        String j = SINGLELOG;
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);

        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();

        request2.setContent(j.getBytes());

        logServer.doPost(request2,response2);
        assertEquals(409, response2.getStatus());
    }

    @Test
    public void testDo1BADLevelPost(){
        Map<String, Object> data = new LinkedHashMap<>();
        Gson gson = new GsonBuilder().create();


        String j = "[\"{\\\"id\\\":\\\"96898390-4676-459f-b437-ac10cb4a9320\\\",\\\"message\\\":\\\"TEST\\\",\\\"timestamp\\\":\\\"2019-06-12T12:06:10.431Z\\\",\\\"thread\\\":\\\"createDummyLogs\\\",\\\"logger\\\":\\\"serverTest\\\",\\\"level\\\":\\\"BAD\\\",\\\"errorDetails\\\":\\\"java.lang.RuntimeException\\\"}\"]";
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request,response);
        assertEquals(400, response.getStatus());
    }

    @Test
    public void testDoONEPostANDGet(){
        String j = SINGLELOG;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","FATAL");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertTrue(i.contains("FATAL"));

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(response2.getContentAsString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(1,jsonArray.length());
        assertEquals(200,response2.getStatus());
    }

    @Test
    public void testDoMultiplePostANDGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","3");
        request2.setParameter("level","DEBUG");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //System.out.println(i);
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = new JSONArray(response2.getContentAsString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(3,jsonArray.length());
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void BadLevelGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","BAD");

        logServer.doGet(request2, response2);
        assertEquals(400, response2.getStatus());
    }

    @Test
    public void DebugGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","DEBUG");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertTrue(i.contains("DEBUG"));
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void ErrorGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","ERROR");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertTrue(i.contains("ERROR"));
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void TraceGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","TRACE");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertTrue(i.contains("TRACE"));
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void FatalGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","FATAL");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertTrue(i.contains("FATAL"));
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void InfoGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","INFO");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertTrue(i.contains("INFO"));
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void OFFGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","OFF");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void ContentTypeGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","1");
        request2.setParameter("level","DEBUG");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals("application/json", response2.getContentType());
        assertEquals(200, response2.getStatus());
    }

    @Test
    public void NegativeLimitGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","-1");
        request2.setParameter("level","DEBUG");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(400, response2.getStatus());
    }

    @Test
    public void DecimalLimitGet(){
        String j = MANYLOGS;

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        request.setContent(j.getBytes());

        LogServer logServer = new LogServer();
        logServer.doPost(request, response);



        MockHttpServletRequest request2 = new MockHttpServletRequest();
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request2.setParameter("limit","0.5");
        request2.setParameter("level","DEBUG");

        logServer.doGet(request2, response2);
        String i = "fail";
        try {
            i = response2.getContentAsString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assertEquals(400, response2.getStatus());
    }

    @Test
    public void testNullObject(){

    }

    @Test
    public void incorrectTimeStamp(){

    }

    @Test
    public void testLogOrder(){

    }

}
