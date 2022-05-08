package anagrams;

import framework.AJob;
import framework.AStrategy;
import framework.Pair;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class Strategy extends AStrategy<String, String>{

    private final String fPath;
    
    public Strategy(String fPath){
        this.fPath = fPath;
    }
    
    public String getFPath(){
        return this.fPath;
    }
    
    /**
     * 
     * @return a Stream by reading files 
     */
    @Override
    protected Stream<AJob<String, String>> emit() {
        try{
            return Files.walk(Path.of(this.fPath))
                            .filter(f -> f.toString().endsWith(".txt"))
                                .map(p -> new Job(p.toString()))
            ;
        } catch (IOException ex) {
            ex.printStackTrace();
            return Stream.empty();
        }
    }

    @Override
    protected void output(Stream<Pair<String, List<String>>> ctldStream) {        
        File fOut = new File("../../output/count_anagrams.txt");
        try {
            FileOutputStream fOS = new FileOutputStream(fOut);
            BufferedWriter bW = new BufferedWriter(new OutputStreamWriter(fOS));
            ctldStream.forEach(p -> {
                try {
                    bW.write((p.getKey()+","+(p.getValue()).size()));
                    bW.write("\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            bW.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    
}
