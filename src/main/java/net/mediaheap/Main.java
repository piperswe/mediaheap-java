package net.mediaheap;

import com.google.gson.Gson;
import net.mediaheap.database.DatabaseConnection;
import net.mediaheap.importer.Importer;
import net.mediaheap.importer.MimeExtractor;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import org.overviewproject.mime_types.GetBytesException;

import java.io.IOException;
import java.sql.SQLException;

public class Main {
    private static void importFile(Namespace args) throws SQLException, IOException, GetBytesException {
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
        var importParser = subparsers.addParser("import");
        importParser.addArgument("path");
        try {
            var parsed = parser.parseArgs(args);
            if (parsed.getString("command").equals("import")) {
                importFile(parsed);
            }
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        } catch (SQLException | IOException | GetBytesException e) {
            throw new RuntimeException(e);
        }
    }
}