package userPlugin;

import dynamic.IPluginLoader;
import dynamic.IServlet;
import dynamic.PluginRouter;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by CJ on 2/7/2017.
 */
public class PluginLoader implements IPluginLoader {

    public void init(PluginRouter router, String rootDirectory) {
        Path path = Paths.get("/UserID/");

        //TODO grab real servlet
        IServlet servlet = null;

        // TODO uncomment
        // router.addRoute(path, servlet);
    }
}
