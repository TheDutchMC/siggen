package dev.array21.siggen.models;

public class ClassDescriptor {
    private final String name;
    private final MethodDescriptor[] methods;
    private final String[] implementing;

    public ClassDescriptor(String name, MethodDescriptor[] methods, String... implementing) {
        this.name = name;
        this.methods = methods;
        this.implementing = implementing;
    }

}
