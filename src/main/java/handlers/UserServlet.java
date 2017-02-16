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

    Map<Integer, User> users;
    private int count = 0;

    public UserServlet() {
        users = new Hashtable<Integer, User>();
    }

    @Override
    public IHttpResponse handleDelete(HttpRequest request) {
        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            int index = Integer.parseInt(relativePath.toString());
            User user = users.get(index);

            if (user == null)
                return build404Response();

            users.remove(index);
            return build200Response("");
        } catch (JsonSyntaxException e) {
            // Throw 400 for invalid json
        } catch (NumberFormatException e) {
            return build404Response();
        }
        return null;
    }

    @Override
    public IHttpResponse handleGet(HttpRequest request) {
        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            if (requestPath.equals(PluginLoader.basePath)) {   // return all users
                if (users == null) {
                    return build404Response();
                }
                Gson gson = new Gson();
                return build200Response(gson.toJson(users.values()));
            }

            int index = Integer.parseInt(relativePath.toString());
            User user = users.get(index);

            if (user == null) {
                return build404Response();
            }
            Gson gson = new Gson();

            return build200Response(gson.toJson(user));
        } catch (JsonSyntaxException e) {
            // Throw 400 for invalid json
        } catch (NumberFormatException e) {
            return build404Response();
        }
        return null;
    }

    @Override
    public IHttpResponse handleHead(HttpRequest request) {
        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            if (requestPath.equals(PluginLoader.basePath)) {   // return all users
                // return all users
                if (users == null) {
                    return build404Response();
                }
                return build200Response("");
            }
            
            int index = Integer.parseInt(relativePath.toString());
            User user = users.get(index);

            if (user == null)
                return build404Response();
            return build200Response("");
        } catch (JsonSyntaxException e) {
            // Throw 400 for invalid json
        } catch (NumberFormatException e) {
            return build404Response();
        }
        
        return null;
    }

    @Override
    public IHttpResponse handlePost(HttpRequest request) {
        String body = new String(request.getBody());

        Path requestPath = Paths.get(request.getUri());

        if (!requestPath.equals(PluginLoader.basePath)) {
            // TODO: Return operation not allowed
            return null;
        }
        try {
            Gson gson = new Gson();
            User user = gson.fromJson(body, User.class);
            user.id = count;
            users.put(count++, user);

            return build200Response(gson.toJson(user));
        } catch (JsonSyntaxException e) {
            // Will Throw 400 for invalid json
        }
        return null;
    }

    @Override
    public IHttpResponse handlePut(HttpRequest request) {
        String body = new String(request.getBody());

        Path requestPath = Paths.get(request.getUri());
        Path relativePath = PluginLoader.basePath.relativize(requestPath);

        try {
            int index = Integer.parseInt(relativePath.toString());
            if (!users.containsKey(index)) {
                return build404Response();
            }

            Gson gson = new Gson();
            User user = gson.fromJson(body, User.class);
            user.id = index;
            users.put(index, user);

            return build200Response(gson.toJson(user));
        } catch (JsonSyntaxException e) {
            // Will Throw 400 for invalid json
        } catch (NumberFormatException e) {
            return build404Response();
        }
        return null;
    }

    private IHttpResponse build200Response(String body) {
        HttpResponseBuilder responseBuilder = new HttpResponseBuilder();
        responseBuilder.setStatus(Protocol.OK_CODE);
        responseBuilder.setPhrase(Protocol.OK_TEXT);

        HashMap headers = new HashMap<String, String>();
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "HEAD, GET, DELETE, POST, PUT");
        responseBuilder.setHeaders(headers);
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
