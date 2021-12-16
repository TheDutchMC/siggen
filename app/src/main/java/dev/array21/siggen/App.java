package dev.array21.siggen;

import com.google.gson.Gson;
import dev.array21.siggen.args.ArgumentParser;
import dev.array21.siggen.args.ProgramArguments;
import dev.array21.siggen.models.Output;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

public class App {
    private static final Logger LOGGER = LogManager.getLogger(App.class);

    public static void main(String[] strArgs) {
        LOGGER.info("Starting application");
        ProgramArguments args = ArgumentParser.parse(strArgs);

        LOGGER.debug("Checking if jarfile path exists");
        if (!pathExists(args.jarfile())) {
            LOGGER.error(String.format("Jarfile path '%s' does not exist", args.jarfile()));
            System.exit(1);
            return;
        }

        LOGGER.debug("Checking if output directory exists");
        if (!pathExists(args.output())) {
            LOGGER.error(String.format("Output directory '%s' does not exist", args.output()));
            System.exit(1);
            return;
        }

        LOGGER.debug("Checking if output directory is actually a directory, and not a file");
        if (new File(args.output()).isFile()) {
            LOGGER.error(String.format("Output directory '%s' is a file", args.output()));
            System.exit(1);
            return;
        }

        LOGGER.debug("Checking that all dependencies exist");
        for (String dep : args.dependencies()) {
            if(!pathExists(dep)) {
                LOGGER.error(String.format("Dependency '%s' does not exist", dep));
                System.exit(1);
                return;
            }

            LOGGER.debug(String.format("Dependency '%s' is valid and will be loaded", dep));
        }

        LOGGER.debug("Program arguments are valid");
        Output output = OutputBuilder.buildOutput(args);
        LOGGER.debug(String.format("Found %d packages, %d classes and %d interfaces", output.getPackageCount(), output.getClassCount(), output.getInterfaceCount()));

        try {
            File outputFile = new File(Path.of(args.output(), "output.json").toString());
            BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));

            LOGGER.debug(String.format("Serializing to JSON and writing to '%s'", outputFile.getAbsolutePath()));
            final Gson gson = new Gson();
            gson.toJson(output, bw);

            bw.flush();
            bw.close();
        } catch (IOException e) {
            LOGGER.error("Failed to write output to file", e);
            System.exit(1);
            return;
        }

        LOGGER.info("Done");
    }

    private static boolean pathExists(String path) {
        File f = new File(path);
        return f.exists();
    }
}
