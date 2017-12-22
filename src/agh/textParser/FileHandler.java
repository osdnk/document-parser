package agh.textParser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class FileHandler {
    public Stream<String> readFile(String path)
            throws IOException
    {
        Stream<String> stream = Files.lines(Paths.get(path));
        return stream;
        }
    }
