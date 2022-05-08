package anagrams;

import framework.*;

public class Main {
    
    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("needs folder path");
            return;
        }
        
        String path = args[0];
        Strategy strategy = new Strategy(path);
        Scheduler<String, String> scheduler = new Scheduler<>(strategy);
        scheduler.start();
    }
    
}
