package dev.array21.siggen.models;

public class ParameterDescriptor {
    private final String name;
    private final String type;
    private final boolean isArray;

    public ParameterDescriptor(String name, String type, boolean isArray) {
        this.name = name;
        this.type = type;
        this.isArray = isArray;
    }

    public ParameterDescriptor(String type, boolean isArray) {
        this.name = null;
        this.type = type;
        this.isArray = isArray;
    }
}
