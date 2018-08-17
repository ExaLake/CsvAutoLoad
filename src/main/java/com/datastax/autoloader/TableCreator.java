package com.datastax.autoloader;

import com.datastax.driver.core.CodecRegistry;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.AlreadyExistsException;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by alexandergauthier on 1/4/18.
 */
public class TableCreator {

    public TableCreator() {
    }

    public String create(Session session, String tableSpace, String tableName, List<String> columns) throws Exception {
        Iterator<String> it = columns.iterator();

        String strColumns = new String();
        while (it.hasNext()) {
            strColumns += String.format("%s varchar, \n", it.next());
        }

        String primaryKey = columns.get(0);
        String statement = String.format("create table if not exists %s.%s (\n%sprimary key (%s));", tableSpace, tableName, strColumns, primaryKey);

        try {
            if (session != null) {
                session.execute(statement);
            }
        } catch (AlreadyExistsException e) {
            e.getMessage();
        }

        return statement;
    }
}
