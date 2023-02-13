package net.mediaheap;

import com.google.common.util.concurrent.ServiceManager;
import com.google.gson.Gson;
import net.mediaheap.api.MediaheapAPIService;
import net.mediaheap.database.DatabaseConnection;
import net.mediaheap.importer.Importer;
import net.mediaheap.importer.MimeExtractor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static void serve(Namespace args) {
        var serviceManager = new ServiceManager(List.of(new MediaheapAPIService(8080)));
        serviceManager.startAsync();
        serviceManager.awaitStopped();
    }

    private static void importFile(Namespace args) throws SQLException, IOException {
        try (var db = DatabaseConnection.localConnection()) {
            var importer = new Importer(db);
            MimeExtractor.getGlobal().registerBuiltinExtractors();
            var path = args.getString("path");
            var file = importer.importFrom(path);
            var json = new Gson().toJson(file);
            System.out.println(json);
        }
    }

    public static void main(String[] args) {
        var parser = ArgumentParsers.newFor("mediaheap").build().defaultHelp(true);
        var subparsers = parser.addSubparsers().title("commands").dest("command");
        subparsers.addParser("serve");
        var importParser = subparsers.addParser("import");
        importParser.addArgument("path");
        try {
            var parsed = parser.parseArgs(args);
            switch (parsed.getString("command")) {
                case "serve" -> serve(parsed);
                case "import" -> importFile(parsed);
            }
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}