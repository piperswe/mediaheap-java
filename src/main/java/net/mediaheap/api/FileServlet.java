package net.mediaheap.api;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import net.mediaheap.database.DatabaseConnection;
import net.mediaheap.model.MediaHeapFile;
import net.mediaheap.model.MediaHeapTag;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

public class FileServlet extends HttpServlet {
    private final Gson gson = new Gson();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try (var db = DatabaseConnection.localConnection()) {
            response.setCharacterEncoding("utf-8");
            var id = request.getParameter("id");
            if (id == null) {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Missing ID");
                return;
            }
            var idInt = Integer.parseInt(id);
            var file = db.getFiles().getFile(idInt);
            if (file == null) {
                response.setContentType("text/plain");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println("No such file");
                return;
            }
            var tags = db.getTags().getTagsForFile(file);
            var json = gson.toJson(new GetFileResponse(file, MediaHeapTag.toNamespaceKeyValueMap(tags)));
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().print(json);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private record GetFileResponse(@NonNull MediaHeapFile file,
                                   @NonNull Map<@NonNull String, @NonNull Map<String, String>> tags) {
    }
}
