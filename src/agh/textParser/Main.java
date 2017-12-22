package agh.textParser;


import java.io.IOException;
import java.util.stream.Stream;
import org.apache.commons.cli.*;


public class Main {

    public static void main(String[] args) {
        Options options = new Options();

        Option f = new Option("f", "file",  true, "file location");
        f.setRequired(true);
        options.addOption(f);

        OptionGroup group = new OptionGroup();

        Option w = new Option("w", "whole",  false, "print whole text");
        w.setRequired(false);
        options.addOption(w);

        Option a = new Option("a", "article", true, "article to be shown");
        a.setRequired(false);
        options.addOption(a);

        Option s = new Option("s", "section",  true, "print specific section");
        s.setRequired(false);
        options.addOption(s);

        Option m = new Option("m", "mode", true, "mode of working. Show a `content` or `table` of content");
        m.setRequired(true);
        options.addOption(m);



        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
            if (!(cmd.hasOption("w") ^ cmd.hasOption("s") ^ cmd.hasOption("a")))
                throw new ParseException("Require -a or -s or -w, but not any both of them!");
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.out.println("Let you learn how to use it: \uD83D\uDE20" +
                    "\n 1. Make sure you have installed apache.commons.cli " +
                    "\n 2.Open terminal" +
                    "\n 3. Type name of bin" +
                    "\n 4. Add argument -f or --file and file location " +
                    "\n 5. Add argument -m or --mode and and type `table` if you wish to see table of content or `content` if you wish to see the content" +
                    "\n 6. To see some section (Dział), chapter (Rozdział) or chapter nested in section type -s `Rozdział I` or -s `DZIAŁ I`" +
                    "\n 7. To see some articles or its range type -a and `Art. 1.` or `Art 1.-Art 2.`. Be careful of dots and whitespaces. If you wish to see some nested structure: e.g.`-a \"Art. 2., 2., 2), a)\"` " +
                    "\n 8. To white whole text or table of content divided on sections/chapters/header, add `-w`" +
                    "\n 9. Remember about dots and arabic/roman numbers. Must be exactly the same as in file");
            formatter.printHelp("utility-name", options);
            System.exit(0);
            return;
        }

        Stream<String> unformattedText = null;

        String file = cmd.getOptionValue("file");
        System.out.println("\nloading file:" + file);

        try {
            FileHandler fileHandler = new FileHandler();
            unformattedText = fileHandler.readFile(file);
        } catch (IOException o) {
            System.out.println("Error reading file");
            System.exit(1);
        }

        Document document = new Document(unformattedText);
        Printer printer = new Printer(document);
        printer.print(cmd);

    }
}
