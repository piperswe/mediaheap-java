package net.mediaheap.api;

import com.google.common.util.concurrent.AbstractIdleService;
import lombok.extern.flogger.Flogger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;

@Flogger
public class MediaheapAPIService extends AbstractIdleService implements AutoCloseable {
    private final int port;
    private Server server;
    private ServerConnector connector;
    private ServletHandler handler;

    public MediaheapAPIService(int port) {
        this.port = port;
    }

    private void setupServletHandler() {
        handler.addServletWithMapping(FileServlet.class, "/file");
    }

    @Override
    protected void startUp() throws Exception {
        server = new Server();
        connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[]{connector});
        handler = new ServletHandler();
        server.setHandler(handler);
        setupServletHandler();
        server.start();
        log.atInfo().log("Listening on port %d", port);
    }

    @Override
    protected void shutDown() throws Exception {
        server.stop();
        connector.close();
        handler.stop();
    }

    @Override
    public void close() throws Exception {
        shutDown();
    }
}
