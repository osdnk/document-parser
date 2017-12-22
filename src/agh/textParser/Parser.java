package agh.textParser;
;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Parser {
    private NodeSectionType type;
    private List<String> text;
    private String id;
    private List<String> raw;
    private String title;

    public Parser(NodeSectionType type, List<String> text) {
        this.type = type;
        this.text = text;
        if (type==NodeSectionType.articleParagraphPointLetterRoot) {
            removeImproperReferences();
        }
        this.splitTextIntoParts();
    }

    public String getId() {
        return this.id;
    }

    public List<String> getRaw() {
        return this.raw;
    }

    public String getTitle() {
        return this.title;
    }

    private void splitTextIntoParts() {
        if (this.type == NodeSectionType.section ||
                this.type == NodeSectionType.chapter) {
            this.raw = this.text.subList(2, this.text.size());
            this.id = this.text.get(0);
            this.title = this.text.get(1);
        }
        if (this.type == NodeSectionType.title) {
            this.title = this.text.get(0);
            this.raw = this.text.subList(1, this.text.size());
            this.id = "";
        }
        if (this.type == NodeSectionType.sectionChapterTitleRoot || this.type == NodeSectionType.articleParagraphPointLetterRoot) {
            this.id = "";
            this.title = "";
            this.raw = this.text;
        }
        if (this.type == NodeSectionType.article) {
            String [] line = this.text.get(0).split(" ");
            this.id = line[0] + " " + line[1];
            this.title = "";
            this.raw = new ArrayList<>();
            if (line.length > 2) {
                this.raw.add(this.text.get(0).substring(this.id.length()+1));
            }
            this.raw.addAll(this.text.subList(1, this.text.size()));
        }
        if (this.type == NodeSectionType.paragraph || this.type == NodeSectionType.point || this.type == NodeSectionType.letter) {
            String [] line = this.text.get(0).split(" ");
            this.id = line[0];
            this.title = "";
            this.raw = new ArrayList<>();
            this.raw.add(this.text.get(0).substring(line[0].length() + 1));
            this.raw.addAll(this.text.subList(1, this.text.size()));
        }
    }

    private void removeImproperReferences() {
        this.text = new ArrayList<>(this.text);
        this.text.removeIf(line ->
                    Pattern.compile(NodeSectionType.section.getRepresentationalMatcher()).matcher(line).matches() ||
                            Pattern.compile(NodeSectionType.chapter.getRepresentationalMatcher()).matcher(line).matches() ||
                            Pattern.compile(NodeSectionType.title.getRepresentationalMatcher()).matcher(line).matches()

        );
    }


    public List<NodeComponent> splitIntoChildren() {
        NodeSectionType regexType = this.type.getDeeperNode();
        List<NodeComponent> children = new ArrayList<>();
        if (regexType == null)
            return children;
        int lastFound = -1;
        for (int i = 0; i < this.raw.size(); i++) {
            if (Pattern.compile(regexType.getRepresentationalMatcher()).matcher(this.raw.get(i)).matches()) {
                if (lastFound != -1) {
                    children.add(new NodeComponent(regexType, this.raw.subList(lastFound, i)));
                }
                lastFound = i;
            }
        }
        if (lastFound != -1) {
            children.add(new NodeComponent(regexType, this.raw.subList(lastFound, this.raw.size())));
        }

        if (children.size() == 0 && regexType.getDeeperNode() != null) {
            this.type = regexType;
            children = this.splitIntoChildren();
        }

        return (children);
    }
}
