package anagrams;

import framework.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

/**
 *
 * @author giulio
 */
public class Job extends AJob<String, String> {
    
    private String fileName; 
    private static final int LENGTH = 4;
    
    public String getFileName(){
        return this.fileName;
    }
    
    public Job(String fileName){
        this.fileName = fileName;
    }
    
    /**
     * @param s string of which we want the ciao
     * @return the CIAO string
     */
    private String getCiao(String s){
        char array[] = s.toLowerCase().toCharArray();
        Arrays.sort(array);
        return new String(array).replaceAll("[^A-Za-z0-9]", "");
    }
    
    @Override
    public Stream<Pair<String, String>> execute(){
        
        try {
            return Files.lines(Paths.get(this.fileName), StandardCharsets.UTF_8)
                    .flatMap(f -> Arrays.stream(f.split("\\s").clone()))
                    .filter(x -> x.length() > Job.LENGTH && !x.matches("^.*[^a-zA-Z0-9 ].*$"))
                    .map(x -> new Pair<String, String>(this.getCiao(x), x.toLowerCase()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

