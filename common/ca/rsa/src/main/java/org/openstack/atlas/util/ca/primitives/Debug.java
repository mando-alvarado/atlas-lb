
package org.openstack.atlas.util.ca.primitives;

import java.net.URL;
import org.openstack.atlas.util.ca.StringUtils;

public class Debug {

    public static String classLoaderInfo(Class<?> cls){
        ClassLoader cl = cls.getClassLoader();
        int hc = cl.hashCode();
        String cp = findClassPath(cls);
        String loaderName = cl.getClass().getName();
        String info = cl.toString();
        String fmt = "{hash=\"%d\", classLoader=\"%s\", classPath=\"%s\", info=\"%s\"}";
        String out = String.format(fmt,hc,loaderName,cp,info);
        return out;
    }

    public static String findClassPath(Class<?> cls){
        try{
        String className   = cls.getName();
        String mangledName = "/" + className.replace(".","/") + ".class";
        URL loc = cls.getResource(mangledName);
        String classPath = loc.getPath();
        return classPath;
        }catch(Exception ex){
            String st = StringUtils.getExtendedStackTrace(ex);
            return st;
        }
    }
}