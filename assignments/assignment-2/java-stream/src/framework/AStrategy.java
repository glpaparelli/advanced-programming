/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package framework;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AStrategy<K,V> {

    /**
     * @return a stream of AJob
     */
    protected abstract Stream<AJob<K,V>> emit();
    
    
    /**
     * @param jStream Stream of AJob
     * @return a Stream obtained by concatenating the execution of every job in stream
     */
    protected Stream<Pair<K,V>> compute(Stream<AJob<K,V>> jStream){
        /**
         * flatMap takes an element of the stream and 
         * applies the function, returning a "new" stream
         */
        return jStream.flatMap(x -> x.execute());
    }
    
    /**
     * @param cmptStream Stream of Pair<K,V> 
     * @return a Stream of Pair<K, List<K,V>>
     */
    protected Stream<Pair<K, List<V>>> collect(Stream<Pair<K,V>> cmptStream){
        var map = cmptStream.collect(
                Collectors.groupingBy(
                        Pair::getKey, 
                        Collectors.mapping(
                                Pair::getValue, 
                                Collectors.toList()
                        )
                )
        );
       
        return map.entrySet().stream().map(w -> new Pair<K,List<V>>(w.getKey(), w.getValue()));
    }
    
    /**
     * show the resulted stream
     * @param cltdStream a Stream of Pair<K, List<V>>
     */
    protected abstract void output(Stream<Pair<K, List<V>>> cltdStream);
    
    
}
