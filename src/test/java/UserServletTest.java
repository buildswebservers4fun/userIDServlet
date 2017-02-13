
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;

import handlers.User;
import handlers.UserServlet;
import protocol.HttpRequest;
import protocol.response.IHttpResponse;

public class UserServletTest {

    private HttpRequest request;
    private UserServlet userServlet;
    private Field uri;
    private Field body;
    private Field method;

    @Before
    public void setUp() throws Exception {

        userServlet = new UserServlet();
        
        Constructor<HttpRequest> constructor;
        constructor = HttpRequest.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        request = constructor.newInstance();

        method = HttpRequest.class.getDeclaredField("method");
        method.setAccessible(true);
        method.set(request, "PUT");

        uri = HttpRequest.class.getDeclaredField("uri");
        uri.setAccessible(true);

        body = HttpRequest.class.getDeclaredField("body");
        body.setAccessible(true);

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPostAndGetReturn200() throws Exception {
        Gson gson = new Gson();
        User user = new User(0, "test user", "testuser@email.com");
        String content = gson.toJson(user);
        
        Constructor<HttpRequest> constructor;
        constructor = HttpRequest.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        request = constructor.newInstance();
        
        method.set(request, "POST");
        uri.set(request, "/UserID/");
        body.set(request, content.toCharArray());

        IHttpResponse response = userServlet.handlePost(request);
        assertEquals(200, response.getStatus());
        
        // now check get
        request = constructor.newInstance();
        
        method = HttpRequest.class.getDeclaredField("method");
        method.setAccessible(true);
        method.set(request, "GET");
        uri.set(request, "/UserID/0");
        
        response = userServlet.handleGet(request);
        assertEquals(200, response.getStatus());
        
        // now check get all
        request = constructor.newInstance();
        
        method = HttpRequest.class.getDeclaredField("method");
        method.setAccessible(true);
        method.set(request, "GET");
        uri.set(request, "/UserID/");
        
        response = userServlet.handleGet(request);
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testHeadReturns200() throws IllegalArgumentException, IllegalAccessException, IOException, NoSuchMethodException, SecurityException, InstantiationException, InvocationTargetException, NoSuchFieldException {
        
        Constructor<HttpRequest> constructor;
        constructor = HttpRequest.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        request = constructor.newInstance();
        
        method.set(request, "HEAD");
        uri.set(request, "/UserID/");

        IHttpResponse response = userServlet.handleHead(request);
        assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testPostAndDeleteReturn200() throws Exception {
            Gson gson = new Gson();
            User user = new User(0, "test user", "testuser@email.com");
            String content = gson.toJson(user);
            
            Constructor<HttpRequest> constructor;
            constructor = HttpRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();
            
            method.set(request, "POST");
            uri.set(request, "/UserID/");
            body.set(request, content.toCharArray());

            IHttpResponse response = userServlet.handlePost(request);
            assertEquals(200, response.getStatus());
            
            // now check get
            request = constructor.newInstance();
            
            method = HttpRequest.class.getDeclaredField("method");
            method.setAccessible(true);
            method.set(request, "DELETE");
            
            uri.set(request, "/UserID/0");
            
            response = userServlet.handleDelete(request);
            assertEquals(200, response.getStatus());
    }
    
    @Test
    public void testPostAndPutReturn200() throws Exception {
            Gson gson = new Gson();
            User user = new User(0, "test user", "testuser@email.com");
            String content = gson.toJson(user);
            
            Constructor<HttpRequest> constructor;
            constructor = HttpRequest.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            request = constructor.newInstance();
            
            method.set(request, "POST");
            uri.set(request, "/UserID/");
            body.set(request, content.toCharArray());

            IHttpResponse response = userServlet.handlePost(request);
            assertEquals(200, response.getStatus());
            
            // now check get
            request = constructor.newInstance();
            
            method = HttpRequest.class.getDeclaredField("method");
            method.setAccessible(true);
            method.set(request, "Put");
            body.set(request, content.toCharArray());
            
            uri.set(request, "/UserID/0");
            
            response = userServlet.handleDelete(request);
            assertEquals(200, response.getStatus());
    }

}
