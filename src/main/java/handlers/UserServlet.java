package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import dynamic.IServlet;
import dynamic.PluginRouter;
import protocol.HttpRequest;
import protocol.Protocol;
import protocol.response.HttpResponseBuilder;
import protocol.response.IHttpResponse;
import userPlugin.PluginLoader;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by CJ on 2/7/2017.
 */
public class UserServlet implements IServlet {

    Map<Path, User> users;

    public UserServlet() {
        users = new Hashtable<Path, User>();
    }

    @Override
    public IHttpResponse handleDelete(HttpRequest request) {
        String body = new String(request.getBody());

        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            User user = users.get(relativePath);

            if(user == null)
                return build404Response();

            users.remove(relativePath);
            return build200Response("");
        } catch(JsonSyntaxException e) {
            // Throw 400 for invalid json
        }
        return null;
    }

    @Override
    public IHttpResponse handleGet(HttpRequest request) {
        String body = new String(request.getBody());

        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            User user = users.get(relativePath);

            if(user == null)
                return build404Response();
            Gson gson = new Gson();

            return build200Response(gson.toJson(user));
        } catch(JsonSyntaxException e) {
            // Throw 400 for invalid json
        }
        return null;
    }

    @Override
    public IHttpResponse handleHead(HttpRequest request) {
        String body = new String(request.getBody());

        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            User user = users.get(relativePath);

            if(user == null)
                return build404Response();
            return build200Response("");
        } catch(JsonSyntaxException e) {
            // Throw 400 for invalid json
        }
        return null;
    }

    @Override
    public IHttpResponse handlePost(HttpRequest request) {
        return handlePut(request);
    }

    @Override
    public IHttpResponse handlePut(HttpRequest request) {
        String body = new String(request.getBody());

        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            Gson gson = new Gson();
            User user = gson.fromJson(body, User.class);

            users.put(relativePath,user);

            return build200Response(gson.toJson(user));
        } catch(JsonSyntaxException e) {
            // Throw 400 for invalid json
        }
        return null;
    }

    private IHttpResponse build200Response(String body) {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        responseBuilder.setStatus(Protocol.OK_CODE);
        responseBuilder.setPhrase(Protocol.OK_TEXT);
        responseBuilder.setHeaders(new HashMap<String, String>());
        responseBuilder.setConnection(Protocol.CLOSE);
        responseBuilder.setBody(body);
        return responseBuilder.build();
    }

    private IHttpResponse build404Response() {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        responseBuilder.setStatus(Protocol.NOT_FOUND_CODE);
        responseBuilder.setPhrase(Protocol.NOT_FOUND_TEXT);
        responseBuilder.setHeaders(new HashMap<String, String>());
        responseBuilder.setConnection(Protocol.CLOSE);
        responseBuilder.setBody("File");
        return responseBuilder.build();
    }
}
