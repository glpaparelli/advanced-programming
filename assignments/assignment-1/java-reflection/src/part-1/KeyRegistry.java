package ex2;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class KeyRegistry{

    private Map<Class<?>, String> registry;

    public KeyRegistry(){
        this.registry = new HashMap<>();
    }

    public void add(Class<?> value, String key){
        this.registry.put(value, key);
    }

    public String get(Class<?> key){
        return this.registry.get(key);
    } 

    public Set<Class<?>> getKeys(){
        return this.registry.keySet();
    }



}