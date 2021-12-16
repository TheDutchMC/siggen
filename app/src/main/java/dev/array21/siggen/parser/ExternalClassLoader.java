package dev.array21.siggen.parser;

import java.net.URL;
import java.net.URLClassLoader;

public class ExternalClassLoader extends URLClassLoader {

    public ExternalClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
