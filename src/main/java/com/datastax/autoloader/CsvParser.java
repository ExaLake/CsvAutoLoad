package com.datastax.autoloader;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import static java.io.FileDescriptor.in;

/**
 * Created by alexandergauthier on 1/4/18.
 */
public class CsvParser {
    public static List<String> getColumns(String path, String delimiter) {
        ArrayList columns = new ArrayList();

        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            String header;
            header = br.readLine();

            StringTokenizer st = new StringTokenizer(header, delimiter);
            while (st.hasMoreTokens()) {
                String column = st.nextToken();

                if (!StringUtils.isAlphanumeric(column)) {
                    column = column.replace(" ", "");
                    column = column.replace("/", "");
                    column = column.replace("_", "");
                    column = column.replace("$", "");
                    column = column.replace("#", "");
                }
                columns.add(column);
            }

        } catch (Exception e) {//Catch exception if any
            System.err.println("Error: " + e.getMessage());
        } finally {
            return columns;
        }
    }
}


