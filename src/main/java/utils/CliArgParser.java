package utils;

import org.apache.commons.cli.*;

import java.util.Iterator;
import java.util.Map;

public class CliArgParser {

    public static Map<String, String> parse(String[] args) {
        Map<String, String> parsedArgs = new java.util.HashMap<>();

        Options options = new Options();

        Option serverId = new Option("i", "serverid", true, "id of the server");
        serverId.setRequired(true);
        options.addOption(serverId);

        Option serversConf = new Option("o", "servers_conf", true, "path to a text file containing the configuration of servers");
        serversConf.setRequired(true);
        options.addOption(serversConf);

        Option socketTimeout = new Option("t", "socket_timeout", true, "socket timeout in milliseconds");
        socketTimeout.setRequired(false);
        options.addOption(socketTimeout);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
        }

        Iterator<Option> iterator = cmd.iterator();
        while (iterator.hasNext()) {
            Option option = iterator.next();
            parsedArgs.put(option.getLongOpt(), option.getValue());
        }

        return parsedArgs;
    }
}
