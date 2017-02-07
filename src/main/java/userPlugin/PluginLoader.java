package userPlugin;

import dynamic.IPluginLoader;
import dynamic.IServlet;
import dynamic.PluginRouter;
import handlers.UserServlet;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by CJ on 2/7/2017.
 */
public class PluginLoader implements IPluginLoader {

    public static final Path basePath = Paths.get("/UserID/");

    public void init(PluginRouter router, String rootDirectory) {
        IServlet servlet = new UserServlet();
        router.addRoute(basePath, servlet);
    }
}
