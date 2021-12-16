package dev.array21.siggen.models;

import java.util.Optional;

public class MethodDescriptor {
    private final String name;
    private final ParameterDescriptor returnType;
    private final boolean isStatic;
    private final ParameterDescriptor[] parameters;
    private final String fromInterface;

    public MethodDescriptor(String name, ParameterDescriptor returnType, boolean isStatic, ParameterDescriptor... parameters) {
        this.name = name;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.parameters = parameters;
        this.fromInterface = null;
    }

    public MethodDescriptor(String name, ParameterDescriptor returnType, boolean isStatic, String fromInterface, ParameterDescriptor... parameters) {
        this.name = name;
        this.returnType = returnType;
        this.isStatic = isStatic;
        this.fromInterface = fromInterface;
        this.parameters = parameters;
    }
}
