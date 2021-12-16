package dev.array21.siggen.args;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Pattern;

public class ArgumentParser {

    private static final Logger LOGGER = LogManager.getLogger(ArgumentParser.class);

    public static ProgramArguments parse(String[] args) {
        LOGGER.debug("Parsing command line arguments");

        CommandLineParser parser = new DefaultParser();
        CommandLine line;
        try {
            line = parser.parse(getOptions(), args);
        } catch(ParseException e) {
            LOGGER.error("Failed to parse command line arguments", e);
            System.exit(1);
            return null;
        }

        String[] dependencies = (line.getOptionValue("dependencies") != null) ? line.getOptionValue("dependencies").split(Pattern.quote(",")) : new String[0];

        return new ProgramArguments(
                line.getOptionValue("jarfile"),
                line.getOptionValue("output"),
                line.getOptionValue("rootPackage"),
                dependencies
        );
    }

    private static Options getOptions() {
        Options options = new Options();

        options.addOption(Option.builder("jarfile")
                .argName("jarfile")
                .hasArg()
                .required()
                .build());

        options.addOption(Option.builder("output")
                .argName("output")
                .hasArg()
                .required()
                .build());

        options.addOption(Option.builder("rootPackage")
                .argName("rootPackage")
                .hasArg()
                .required()
                .build());

        options.addOption(Option.builder("dependencies")
                .argName("dependencies")
                .hasArg()
                .build());

        return options;
    }
}
