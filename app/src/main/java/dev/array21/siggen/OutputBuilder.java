package dev.array21.siggen;

import dev.array21.siggen.args.ProgramArguments;
import dev.array21.siggen.models.ClassDescriptor;
import dev.array21.siggen.models.MethodDescriptor;
import dev.array21.siggen.models.Output;
import dev.array21.siggen.parser.ClassParser;
import dev.array21.siggen.parser.ExternalClassLoader;
import dev.array21.siggen.parser.JarParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OutputBuilder {

    private static final Logger LOGGER = LogManager.getLogger(JarParser.class);

    public static Output buildOutput(ProgramArguments args) {
        String[] classes = JarParser.getClasses(args.jarfile(), args.rootPackage());
        ExternalClassLoader externalClassLoader = JarParser.getClassLoader(args.jarfile(), args.dependencies());

        ClassDescriptor[] classDescriptors = new ClassDescriptor[classes.length];
        ClassDescriptor[] interfaceDescriptors = new ClassDescriptor[classes.length];
        List<String> packages = new ArrayList<>();
        for(int i = 0; i < classDescriptors.length; i++) {
            String formattedName = classes[i]
                    .replace(".class", "");
            LOGGER.debug(String.format("Loading class '%s'", formattedName));

            Class<?> clazz;
            try {
                clazz = externalClassLoader.loadClass(formattedName);
            } catch(ClassNotFoundException e) {
                LOGGER.error(String.format("Failed to load class '%s'", formattedName), e);
                return null;
            }

            if(!packages.contains(clazz.getPackageName())) {
                packages.add(clazz.getPackageName());
            }

            MethodDescriptor[] methodDescriptors = ClassParser.getMethods(clazz);
            String[] interfaces = new String[clazz.getInterfaces().length];
            for(int j = 0; j < interfaces.length; j++) {
                Class<?> iface = clazz.getInterfaces()[j];
                interfaces[j] = iface.getName();
            }

            if(clazz.isInterface()) {
                interfaceDescriptors[i] = new ClassDescriptor(formattedName, methodDescriptors, interfaces);
            } else {
                classDescriptors[i] = new ClassDescriptor(formattedName, methodDescriptors, interfaces);
            }
        }

        ClassDescriptor[] filteredClasses = Stream.of(classDescriptors)
                .filter(Objects::nonNull)
                .toList()
                .toArray(new ClassDescriptor[0]);

        ClassDescriptor[] filteredInterfaces = Stream.of(interfaceDescriptors)
                .filter(Objects::nonNull)
                .toList()
                .toArray(new ClassDescriptor[0]);

        return new Output(packages.toArray(new String[0]), filteredClasses, filteredInterfaces);
    }
}
