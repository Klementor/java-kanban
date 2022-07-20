package manager.implementation;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class WorkWithHeader {

    public static void addHeader(Path path) {
        try (Writer writer = new FileWriter(path.toString(), StandardCharsets.UTF_8, false)) {
            writer.write(getHeader() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getHeader() {
        StringBuilder str = new StringBuilder();
        NamesOfColumn[] columns = NamesOfColumn.values();
        for (NamesOfColumn column : columns) {
            str.append(column.name());
            str.append(",");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }
}
