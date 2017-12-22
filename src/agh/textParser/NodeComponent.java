package agh.textParser;

import java.util.Collections;
import java.util.List;

public class NodeComponent {
    private final NodeSectionType type;
    private final String title;
    private final String id;
    private final List<String> raw;
    private final List<NodeComponent> children;


    public void print (int depthLevel, boolean withContent) {
        String spaces = String.join("", Collections.nCopies(depthLevel, "    "));
        if (!withContent) {
            System.out.println(spaces + this.id + " " + this.title);
        }
        if (!withContent) {
            for (NodeComponent child : this.children) {
                child.print(depthLevel + 1, withContent);
            }
        } else {
            for (String line : this.raw) {
                System.out.println(spaces + "    " + line);
            }
        }
    }

    public void printSpecific(String path, boolean withContent) {
        String [] ids = path.split(" ");
        String currentId = ids[0];
        if ((this.type == NodeSectionType.articleParagraphPointLetterRoot ||
                this.type == NodeSectionType.sectionChapterTitleRoot ||
                this.type == NodeSectionType.section) && ids.length>1

                ) {
            currentId += " " + ids[1];
        }
        boolean found = false;
        for (NodeComponent child : this.children) {
            if(child.id.equals(currentId)) {
                if (path.substring(currentId.length(), path.length()).length()>0) {
                    child.printSpecific(path.substring(currentId.length()+1, path.length()), withContent);
                } else {
                    child.print(0, withContent);
                }
                found = true;
            }
        }
        if (!found) {
            System.out.println("Something is wrong with this path 🔥");
        }
    }

    public void printRangeById(String from, String to) {
        boolean printMode = false;
        boolean found = false;

        for (NodeComponent child : this.children) {
            if (child.id.equals(from)) {
                printMode = true;
                found = true;
            }
            if (child.id.equals(to)) {
                printMode = false;
            }
        }

        if (found && !printMode) {
            for (NodeComponent child : this.children) {
                if (child.id.equals(from)) {
                    printMode = true;
                }
                if (printMode) {
                    child.print(0, true);
                }
                if (child.id.equals(to)) {
                    printMode = false;
                }
            }
        } else {
            System.out.println("Something is wrong with this range \uD83C\uDF85");
        }
    }

    public NodeComponent (NodeSectionType majorType, List<String> text) {
        this.type = majorType;
        Parser parser = new Parser(majorType, text);
        this.raw = parser.getRaw();
        this.children= parser.splitIntoChildren();

        this.id = parser.getId();
        this.title = parser.getTitle();
    }
}
