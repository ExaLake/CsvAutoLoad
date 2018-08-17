package com.datastax.autoloader;

import com.datastax.autoloader.CsvParser;
import com.datastax.autoloader.TableCreator;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by alexandergauthier on 1/4/18.
 */
public class TableCreatorTest {
    public static final String PATH = "/Users/alexandergauthier/Downloads/Crimes.csv";
    public static final String DELIM = ",";

    @Test
    public void create() throws Exception {

        String tableName = String.format("t_%s",  UUID.randomUUID().toString());
        tableName = tableName.replace("-","");

        TableCreator tableCreator = new TableCreator();
        String statement = tableCreator.create(null,"staging", tableName, CsvParser.getColumns(PATH, DELIM));

        System.out.println(statement);
    }
}