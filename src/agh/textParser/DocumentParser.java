package agh.textParser;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentParser {
    private List<String> text;

    public DocumentParser (Stream<String> streamedText) {
        this.text = this.mergeLines(this.removeUselessLines(streamedText));
    }

    private Stream<String> removeUselessLines (Stream<String> text) {
        return text.filter( line ->
                !Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(line).matches() &&
                        !Pattern.compile("^©.*$").matcher(line).matches() &&
                        !Pattern.compile("^Dz\\.U\\. .*$").matcher(line).matches() &&
                        !Pattern.compile(".*?uchylon.*?").matcher(line).matches() &&
                        !Pattern.compile(".*?pominięt.*?").matcher(line).matches() &&
                        line.length()>2
        );
    }

    private List<String> mergeLines (Stream<String> lines) {
        List<String> aLines = lines.collect(Collectors.toList());
        for (int i = 0; i < aLines.size(); i++) {
            String line = aLines.get(i);
            if (line.lastIndexOf('-')==line.length()-1) {
                String lastWord = line.split(" ")[line.split(" ").length-1];
                aLines.set(i, line.substring(0, line.length()-lastWord.length()-1));
                aLines.set(i+1, lastWord.substring(0,lastWord.length()-1)+aLines.get(i+1));
            }
        }
        return  aLines;
    }

    public List<String> getRaw (){
        return this.text.subList(3, this.text.size());
    }

}
