/**
 * Created by alexandergauthier on 1/4/18.
 */
package com.datastax.autoloader;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.querybuilder.Insert;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.apache.commons.cli.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.System.out;


public class CsvAutoLoader {

    public static int main(String[] args) {

        CommandLine cmdLine = CsvAutoLoader.generateCommandLine(CsvAutoLoader.generateOptions(), args);
        List<String> columns = CsvParser.getColumns(cmdLine.getOptionValue("f"), cmdLine.getOptionValue("d"));
        String[] addresses = {cmdLine.getOptionValue("a")};
        Session session = new ConnectionManager().getConnection(addresses, cmdLine.getOptionValue("k"));

        // create table
        TableCreator tableCreator = new TableCreator();

        try {
            // Create Table
            tableCreator.create(session, cmdLine.getOptionValue("k"), cmdLine.getOptionValue("t"), columns);

            BufferedReader br = new BufferedReader(new FileReader(cmdLine.getOptionValue("f")));
            BatchStatement batchStatement = new BatchStatement();
            List<String> row = null;


            File csvData = new File(cmdLine.getOptionValue("f"));
            CSVParser parser = CSVParser.parse(csvData, Charset.defaultCharset(), CSVFormat.DEFAULT);

            int numColumn = columns.size();

            for (CSVRecord csvRecord : parser) {
                List<String> values = new ArrayList<String>();

                for (int i = 0; i < numColumn; i++) {
                    String value = csvRecord.get(i);

                    if (value == null || value == "") {
                        values.add("<NULL>");
                    } else {
                        values.add(value);
                    }
                }

                Insert i = QueryBuilder.insertInto(cmdLine.getOptionValue("k"),
                        cmdLine.getOptionValue("t")).values(columns, Arrays.asList(values.toArray()));
                session.execute(i);
            }

        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
            return -1;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
            return -3;
        } finally {
            return 0;
        }
    }

    private static Options generateOptions() {
        final Option verboseOption = Option.builder("f")
                .required(true)
                .hasArg(true)
                .longOpt("file")
                .desc("path to csv file")
                .build();
        final Option fileOption = Option.builder("d")
                .required()
                .longOpt("delimiter")
                .hasArg()
                .desc("Delimiter")
                .build();
        final Option address = Option.builder("a")
                .required()
                .longOpt("addresss")
                .hasArg()
                .desc("address")
                .build();
        final Option keySpaceOption = Option.builder("k")
                .required()
                .longOpt("keyspace")
                .hasArg()
                .desc("key space")
                .build();
        final Option tableName = Option.builder("t")
                .required()
                .longOpt("tableName")
                .hasArg()
                .desc("table name")
                .build();

        final Options options = new Options();
        options.addOption(verboseOption);
        options.addOption(fileOption);
        options.addOption(address);
        options.addOption(keySpaceOption);
        options.addOption(tableName);
        return options;
    }

    private static CommandLine generateCommandLine(
            final Options options, final String[] commandLineArguments) {
        final CommandLineParser cmdLineParser = new DefaultParser();
        CommandLine commandLine = null;
        try {
            commandLine = cmdLineParser.parse(options, commandLineArguments);
        } catch (ParseException parseException) {
            out.println(
                    "ERROR: Unable to parse command-line arguments "
                            + Arrays.toString(commandLineArguments) + " due to: "
                            + parseException);
        }
        return commandLine;
    }
}
