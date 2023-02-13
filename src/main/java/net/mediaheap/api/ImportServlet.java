package net.mediaheap.api;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import net.mediaheap.database.DatabaseConnection;
import net.mediaheap.importer.Importer;
import net.mediaheap.model.MediaHeapTag;
import org.overviewproject.mime_types.GetBytesException;

import java.io.IOException;
import java.sql.SQLException;

@Singleton
public class ImportServlet extends HttpServlet {
    @NonNull
    private final Importer importer;
    @NonNull
    private final DatabaseConnection db;
    @NonNull
    private final Gson gson;

    @Inject
    ImportServlet(@NonNull Importer importer, @NonNull DatabaseConnection db, @NonNull Gson gson) {
        this.importer = importer;
        this.db = db;
        this.gson = gson;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setCharacterEncoding("utf-8");
            var path = request.getParameter("path");
            if (path == null) {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Missing path");
                return;
            }
            var file = importer.importFrom(path);
            var tags = db.getTags().getTagsForFile(file);
            var json = gson.toJson(new FileServlet.GetFileResponse(file, MediaHeapTag.toNamespaceKeyValueMap(tags)));
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(json);
        } catch (SQLException | GetBytesException e) {
            throw new RuntimeException(e);
        }
    }
}
