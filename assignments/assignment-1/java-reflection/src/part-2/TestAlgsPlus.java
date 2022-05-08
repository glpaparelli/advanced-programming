package ex3;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import ex2.*;

public class TestAlgsPlus extends TestAlgs {
    public TestAlgsPlus(String path) throws IOException {
        super(path);
    }

    public static void main(String[] args) 
        throws 
            IOException, 
            InstantiationException, 
            IllegalAccessException, IllegalArgumentException, 
            InvocationTargetException, 
            SecurityException 
    {
        if(args.length != 1){
            System.out.println("error: need path of input's parent folder ");
            return;
        }

        String path = args[0];
        TestAlgsPlus testAlgsPlus = new TestAlgsPlus(path);

        testAlgsPlus.analysis();
    }

    @Override
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

            //methods retrive by name or by annotations
            Method enc = this.getMethodByPrefix(cls, "enc");
            Method dec = this.getMethodByPrefix(cls, "dec");
            Method encAnnotated = this.getMethodByAnnotation(cls, Encrypt.class);
            Method decAnnotated = this.getMethodByAnnotation(cls, Decrypt.class);

            //if there is not enc/dec method both by name and annotation then this class in not relevant
            if((enc == null && encAnnotated == null) || (dec == null && decAnnotated == null)){
                System.out.println(">WARNING: " + clsName + " ENCRYPTION-DECRYPTION METHODS NOT FOUND");
                continue;
            }

            Method encrypt = (enc != null) ? enc : encAnnotated;
            Method decrypt = (dec != null) ? dec : decAnnotated;

            //we know there is the construcotr from the previous ifs
            Object algorithm = cls.getConstructors()[0].newInstance(registry.get(cls));
            this.testAlgorithm(cls, algorithm, encrypt, decrypt);
        }
    }

    private Method getMethodByAnnotation(Class<?> cls, Class<?> antn){
        String antnName = antn.getSimpleName();

        for(Method mtd : cls.getMethods()){
            Annotation[] mtdAnnotations = mtd.getAnnotations();
            if(mtdAnnotations.length == 1 && mtdAnnotations[0].annotationType().getSimpleName().equals(antnName)){
                if(mtd.getParameterCount() == 1 && mtd.getParameterTypes()[0].equals(String.class))
                    return mtd;

            }
        }
        return null;
    } 
    
}
