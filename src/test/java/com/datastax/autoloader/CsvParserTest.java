package com.datastax.autoloader;

import com.datastax.autoloader.CsvParser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alexandergauthier on 1/4/18.
 */



public class CsvParserTest {

    public static final String PATH = "/Users/alexandergauthier/Downloads/redfin_2017-10-14-20-35-06.csv";
    public static final String DELIM = ",";

    @org.junit.Test
    public void test() throws Exception {
        List<String> columns = CsvParser.getColumns(PATH, DELIM);

        System.out.println(columns.size());
        Iterator<String> it = columns.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}