package com.datastax.autoloader;

import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

/**
 * Created by alexandergauthier on 1/5/18.
 */
public class CsvAutoLoaderTest {
    public static final String PATH = "/Users/cleaner/Downloads/Crime.csv";
    public static final String DELIM = ",";
    public static final String ADDR = "10.200.177.140";
    public static final String KEYSPACE = "staging";
    public static final String TABLE = "crimes_2017_2";

    @Test
    public void main() throws Exception {

        //String tableName = String.format("t_%s",  UUID.randomUUID().toString()).replace("-","");
        String args[] = {"-f", PATH, "-d", DELIM, "-a", ADDR, "-k", KEYSPACE, "-t", TABLE};
        System.out.println(Arrays.toString(args));
        CsvAutoLoader.main(args);
    }
}