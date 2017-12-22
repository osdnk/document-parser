package agh.textParser;

import java.util.List;
import java.util.stream.Stream;

class Document {

    private List<String> raw;
    private NodeComponent sectionChapterTitleTree;
    private NodeComponent articleParagraphPointLetterTree;

    public void printArticles(String from, String to) {
        this.articleParagraphPointLetterTree.printRangeById(from, to);
    }

    public void printWhole(boolean withContent) {
        this.sectionChapterTitleTree.print(0, withContent);
    }

    public void printTableOfContent () {
        this.sectionChapterTitleTree.print(0, false);
    }

    public void printSpecificArticle(String path) {
        this.articleParagraphPointLetterTree.printSpecific(path, true);
    }

    public void printSpecificSection(String path, Boolean withContent) {
        this.sectionChapterTitleTree.printSpecific(path, withContent);
    }




    public Document(Stream<String> streamedText) {
        try {
            DocumentParser documentParser = new DocumentParser(streamedText);
            this.raw = documentParser.getRaw();
            this.sectionChapterTitleTree = new NodeComponent(NodeSectionType.sectionChapterTitleRoot, this.raw);
            this.articleParagraphPointLetterTree = new NodeComponent(NodeSectionType.articleParagraphPointLetterRoot, this.raw);
        } catch (NullPointerException | StringIndexOutOfBoundsException w) {
            System.out.println("Oh, something is wrong with this file\uD83D\uDCA3. Meh, I won't handle it, give me another!");
            System.exit(0);
        }


    }
}
