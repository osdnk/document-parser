package agh.textParser;


public enum NodeSectionType {
    sectionChapterTitleRoot,
    section,
    chapter,
    title,

    articleParagraphPointLetterRoot,
    article,
    paragraph,
    point,
    letter;

    public NodeSectionType getDeeperNode() {
        switch (this) {
            case sectionChapterTitleRoot:
                return section;
            case section:
                return chapter;
            case chapter:
                return title;
            case articleParagraphPointLetterRoot:
                return article;
            case article:
                return paragraph;
            case paragraph:
                return point;
            case point:
                return letter;
            default:
                return null;
        }
    }

    public String getRepresentationalMatcher () {
        switch (this) {
            case section:
                return "DZIAŁ.*";
            case chapter:
                return  "Rozdział.*";
            case title:
                return "[A-ZĄĆĘŁŃÓŚŹŻ ]*";
            case article:
                return "Art\\. [0-9]*[a-z]?\\..*";
            case paragraph:
                return "[0-9]*\\. .*";
            case point:
                return "[0-9]*\\) .*";
            case letter:
                return "[a-z]*\\) .*";
            default:
                return null;
        }
    }
}
