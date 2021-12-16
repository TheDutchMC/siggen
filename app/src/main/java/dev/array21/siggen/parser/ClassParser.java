package dev.array21.siggen.parser;

import dev.array21.siggen.models.MethodDescriptor;
import dev.array21.siggen.models.ParameterDescriptor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class ClassParser {

    private static final Logger LOGGER = LogManager.getLogger(ClassParser.class);

    public static MethodDescriptor[] getMethods(Class<?> clazz) {
        LOGGER.debug(String.format("Building Method Descriptors for %s", clazz.getName()));

        MethodDescriptor[] methodDescriptors = new MethodDescriptor[clazz.getDeclaredMethods().length];
        for (int i = 0; i < methodDescriptors.length; i++) {
            Method m = clazz.getDeclaredMethods()[i];
            String name = m.getName();

            if(m.getDeclaringClass().equals(Object.class) || m.getDeclaringClass().equals(Enum.class)) {
                continue;
            }

            LOGGER.trace(String.format("Building method descriptor for %s:%s", clazz.getName(), name));

            boolean isStatic = Modifier.isStatic(m.getModifiers());
            ParameterDescriptor returnType = new ParameterDescriptor(m.getReturnType().getCanonicalName(), m.getReturnType().isArray());

            ParameterDescriptor[] parameterDescriptors = new ParameterDescriptor[m.getParameterCount()];
            for (int j = 0; j < parameterDescriptors.length; j++) {
                Parameter param = m.getParameters()[j];
                Class<?> pClass = param.getType();
                parameterDescriptors[j] = new ParameterDescriptor(param.getName(), pClass.getCanonicalName(), pClass.isArray());
            }

            String declaringInterface = null;
            for (Class<?> iface : clazz.getInterfaces()) {
                // Preferably we do not want to use try/catch in our logic
                // However the Reflection API exposes no easy and performant way without
                try {
                    iface.getDeclaredMethod(m.getName(), m.getParameterTypes());

                    declaringInterface = iface.getCanonicalName();
                } catch (NoSuchMethodException ignored) {

                }
            }

            if(declaringInterface != null ) {
                methodDescriptors[i] = new MethodDescriptor(name, returnType, isStatic, declaringInterface, parameterDescriptors);
            } else {
                methodDescriptors[i] = new MethodDescriptor(name, returnType, isStatic, parameterDescriptors);
            }
        }

        MethodDescriptor[] filteredMethods = Stream.of(methodDescriptors)
                        .filter(Objects::nonNull)
                        .toList()
                        .toArray(new MethodDescriptor[0]);
        LOGGER.trace(String.format("Found %d methods", filteredMethods.length));
        return filteredMethods;
    }
}
