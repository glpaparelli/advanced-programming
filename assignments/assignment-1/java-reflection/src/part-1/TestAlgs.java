package ex2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class TestAlgs {
    
    protected final KeyRegistry registry;
    protected final List<String[]> keys; 
    protected final List<String> secrets;

    public static void main(String[] args) 
        throws 
            IOException, 
            InstantiationException, 
            IllegalAccessException, 
            IllegalArgumentException, 
            InvocationTargetException, 
            SecurityException 
    {    
        if(args.length != 1){
            System.out.println("error: need path of input's parent folder ");
            return;
        }

        String path = args[0];

        TestAlgs testAlgs = new TestAlgs(path);
        //start the analysis of the classes
        testAlgs.analysis();
    }

    public TestAlgs(String path) throws IOException{
        this.keys = Files.lines(Paths.get(path + "/input/keys.list")).map(x -> x.split(" ")).collect(Collectors.toList());
        this.secrets = Files.lines(Paths.get(path + "/input/secret.list")).collect(Collectors.toList());
        this.registry = this.buildKeyRegistry(path, keys);
    }

    protected void analysis() 
        throws 
            InstantiationException, 
            IllegalAccessException, 
            IllegalArgumentException, 
            InvocationTargetException, 
            SecurityException
    {
        for(Class<?> cls : this.registry.getKeys()){
            String clsName = cls.getSimpleName();

            if(!this.hasPublicConstructor(cls)){
                System.out.println("> WARNING: " + clsName + " NO PUBLIC CONSTRUCTOR");
                continue;
            }

            Method encrypt = this.getMethodByPrefix(cls, "enc");
            Method decrypt = getMethodByPrefix(cls, "dec");

            if(encrypt == null || decrypt == null){
                System.out.println(">WARNING: " + clsName + " ENCRYPTION-DECRYPTION METHODS NOT FOUND");
                continue;
            }
            
            //we know there is the construcotr from the previous ifs
            Object algorithm = cls.getConstructors()[0].newInstance(this.registry.get(cls));
            this.testAlgorithm(cls, algorithm, encrypt, decrypt);
        }
    }

    /**
     * @param cls algorithm class
     * @param algorithm instance of the algorithm
     * @param encrypt encryption method
     * @param decrypt decryption method
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    protected void testAlgorithm(Class<?> cls, Object algorithm, Method encrypt, Method decrypt) 
        throws 
            IllegalAccessException, 
            IllegalArgumentException, 
            InvocationTargetException
    {
        for(String wrd : this.secrets){
            String encrypted = (String) encrypt.invoke(algorithm, wrd);
            String decrypted = (String) decrypt.invoke(algorithm, wrd);

            if(!decrypted.contains(wrd))
                System.out.printf(">%s: [KO] %s -> %s -> %s\n", cls.getSimpleName(), wrd, encrypted, decrypted);
        }
    }

    protected KeyRegistry buildKeyRegistry(String path, List<String[]> keys) 
        throws 
            MalformedURLException
    {
        KeyRegistry registry = new KeyRegistry();

        File file = new File(path);
        URL[] urls = {file.toURI().toURL()};

        ClassLoader loader = new URLClassLoader(urls);

        for(String[] key : keys){
            try {
                registry.add(loader.loadClass(key[0]), key[1]);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return registry;
    }

    protected boolean hasPublicConstructor(Class<?> cls){
        Constructor<?>[] constructors = cls.getConstructors();
        if(constructors.length == 0)
            return false;

        for(Constructor<?> ctr : constructors){
            //among constructors there is a constructor with 1 String parameter
            if(ctr.getParameterCount() == 1 && ctr.getParameterTypes()[0].equals(String.class))
                return true;
        }
        
        return false;
    }

    protected Method getMethodByPrefix(Class<?> cls, String prefix){
        Method[] methods = cls.getMethods();

        for(Method mtd : methods){
            if(mtd.getName().startsWith(prefix) && mtd.getParameterCount() == 1 && 
               mtd.getParameterTypes()[0].equals(String.class))
                return mtd;
        }

        return null;
    }
}
