package dev.array21.siggen.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class JarParser {

    private static final Logger LOGGER = LogManager.getLogger(JarParser.class);

    public static ExternalClassLoader getClassLoader(String path, String... dependencies) {
        try {
            File f = new File(path);
            URL jarUrl = f.toURI().toURL();
            URL[] urls = new URL[dependencies.length + 1];
            urls[0] = jarUrl;

            for (int i = 0; i < dependencies.length; i++) {
                File depF = new File(dependencies[i]);
                URL depUrl = depF.toURI().toURL();
                urls[i + 1] = depUrl;
            }

            LOGGER.debug(String.format("Adding %d JAR(s) to the ClassLoader", urls.length));
            ExternalClassLoader externalClassLoader = new ExternalClassLoader(urls, Thread.currentThread().getContextClassLoader());
            return externalClassLoader;
        } catch (MalformedURLException e) {
            LOGGER.error("Failed to convert URI to URL", e);
            return null;
        }
    }

    public static String[] getPackages(String path) {
        try {
            URI uri = new URL(String.format("jar:file:%s!/", path)).toURI();
            FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());

            List<String> packages = new ArrayList<>();
            for(Path rootDir : fs.getRootDirectories()) {
                List<String> files = Files.walk(rootDir)
                        .filter(Files::isDirectory)
                        .map(Path::toString)
                        .map(f -> f.replaceAll(Pattern.quote("/"), "."))
                        .toList();
                String rootDirName = rootDir
                        .toString()
                        .replaceAll(Pattern.quote("/"), ".");
                packages.add(rootDirName);
                packages.addAll(files);
            }

            fs.close();
            return packages.toArray(new String[0]);
        } catch (IOException e) {
            LOGGER.error("Failed to parse all packages from Jar file", e);
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] getClasses(String path, String rootPackage) {
        try {
            LOGGER.debug(String.format("Opening jarfile at '%s' to get contained classes", path));
            URI uri = new URL(String.format("jar:file:%s!/", path)).toURI();
            FileSystem fs = FileSystems.newFileSystem(uri, Collections.<String, Object>emptyMap());

            List<String> classes = new ArrayList<>();
            for(Path rootDir : fs.getRootDirectories()) {
                List<String> files = Files.walk(rootDir)
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(f -> f.endsWith(".class"))
                        .map(f -> f.replaceFirst(Pattern.quote("/"), ""))
                        .map(f -> f.replace('/', '.'))
                        .filter(f -> f.startsWith(rootPackage))
                        .filter(f -> !f.contains("module-info"))
                        .filter(f -> !f.contains("$"))
                        .toList();
                classes.addAll(files);
            }

            fs.close();
            return classes.toArray(new String[0]);
        } catch (IOException e) {
            LOGGER.error("Failed to parse all classes from Jar file", e);
            return null;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
