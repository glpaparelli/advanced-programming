package framework;

public class Scheduler<K,V> {
    
    private AStrategy<K,V> strategy; 
    
    public void setStrategy(AStrategy<K,V> strategy){
        this.strategy = strategy;
    }
    
    public Scheduler(AStrategy<K,V> strategy){
        this.strategy = strategy;
    }
    
    public void start(){
        if(strategy == null)
            throw new NullPointerException("strategy is null");
        
        strategy.output(strategy.collect(strategy.compute(strategy.emit())));
    }
}
