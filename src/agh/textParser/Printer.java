package agh.textParser;

import org.apache.commons.cli.CommandLine;

public class Printer {
    private Document document;
    public Printer(Document document) {
        this.document = document;
    }

    public void print(CommandLine cmd) {

        String mode = cmd.getOptionValue("m");
        boolean showContent;
        switch (mode) {
            case "content":
                    showContent = true;
                break;

            case "table":
                    showContent = false;
                break;

            default:
                System.out.println("Unsupported mode \uD83D\uDE46\u200D!\n");
                return;

        }

        Boolean whole = cmd.hasOption("w");
        if (whole) {
            System.out.println("\n \uD83D\uDC33 Whole text:");
            document.printWhole(showContent);
            return;
        }

        String specificArticle = cmd.getOptionValue("a");
        if (specificArticle!=null) {
            if (!showContent) {
                System.out.println("\uD83E\uDD14 unsupported mode!");
                return;
            }
            if (specificArticle.indexOf('-')==-1) {
                System.out.println("\n \uD83D\uDD7A Printing: " + specificArticle);
                document.printSpecificArticle(specificArticle.replace(",", ""));
            } else {
                System.out.println("\n \uD83D\uDC4D Printing range: " + specificArticle);
                String [] range = specificArticle.split("-");
                document.printArticles(range[0], range[1]);
            }
            return;
        }

        String specificSection = cmd.getOptionValue("s");
        if (specificSection!=null) {
            if (showContent) {
                System.out.println("\n \uD83E\uDDDE\u200D print content of : " + specificSection);
            } else {
                System.out.println("\n ☂️ print table of : " + specificSection);
            }
            document.printSpecificSection(specificSection.replace(",", ""), showContent);
            return;
        }
    }
}
