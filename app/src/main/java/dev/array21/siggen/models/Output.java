package dev.array21.siggen.models;

public class Output {
    private final String[] packages;
    private final ClassDescriptor[] classes;
    private final ClassDescriptor[] interfaces;

    public Output(String[] packages, ClassDescriptor[] classes, ClassDescriptor[] interfaces) {
        this.packages = packages;
        this.classes = classes;
        this.interfaces = interfaces;
    }

    public int getPackageCount() {
        return this.packages.length;
    }

    public int getClassCount() {
        return this.classes.length;
    }

    public int getInterfaceCount() {
        return this.interfaces.length;
    }
}
