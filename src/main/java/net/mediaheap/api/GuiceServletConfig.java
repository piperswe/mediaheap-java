package net.mediaheap.api;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import lombok.SneakyThrows;
import net.mediaheap.database.DatabaseConnection;
import net.mediaheap.importer.MimeExtractor;

public class GuiceServletConfig extends GuiceServletContextListener {
    @SneakyThrows
    @Override
    protected Injector getInjector() {
        MimeExtractor.getGlobal().registerBuiltinExtractors();
        return Guice.createInjector(DatabaseConnection.localConnection().getModule(), new ServletModule() {
            @Override
            protected void configureServlets() {
                serve("/file").with(FileServlet.class);
                serve("/import").with(ImportServlet.class);
            }
        });
    }
}
