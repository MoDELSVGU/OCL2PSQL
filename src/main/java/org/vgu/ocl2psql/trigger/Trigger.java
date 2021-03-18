/**************************************************************************
Copyright 2019 Vietnamese-German-University

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

@author: thian
***************************************************************************/

package org.vgu.ocl2psql.trigger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Set;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.vgu.dm2schema.dm.DataModel;
import org.vgu.dm2schema.dm.Invariant;
import org.vgu.dm2schema.dm.Invariants;
import org.vgu.ocl2psql.main.OCL2PSQL_2;
import org.vgu.ocl2psql.ocl.roberts.exception.OclParseException;

public class Trigger {
    
    public static void main(String[] args) throws FileNotFoundException,
            IOException, ParseException, Exception {
        
        String propertiesFile = "src/main/resources/db.properties";
        File model = new File("src/main/resources/genSQL/uni_pof_dm.json");
        DataModel dm = new DataModel(new JSONParser()
                .parse(new FileReader(model.getAbsolutePath())));
        
        String SQL_STATEMENT = "INSERT INTO Exam (module_period, date, starts, ends, deadline) " + 
                "VALUES  (1, \"20191227\", \"15:00:00\", \"16:30:00\", \"20191224\");";
        
        runTriggers(SQL_STATEMENT, dm, propertiesFile);
        
    }

    public static void runTriggers(String sqlStatement, DataModel dm,
            String propertiesFile) {
        
        Connection conn = getConnection(propertiesFile); 
        
        try (PreparedStatement ps = conn.prepareStatement(sqlStatement)) {
            conn.setAutoCommit(false);
            ps.execute();
            

            boolean isDbAllRight = validateInvariants(conn, dm);
            
//            conn.commit();
            
            if (isDbAllRight) {
                conn.commit();
                System.out.println("OK: " + isDbAllRight);
            } else {
                conn.rollback();
            }
            
            conn.setAutoCommit(true);
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
    }
    
    private static Connection getConnection(String propertiesFile) {
        
        Properties prop = loadProperties(propertiesFile);
       
        String dbUrl = "jdbc:mysql://" + 
                prop.getProperty("db.host") + "/" + 
                prop.getProperty("db.name");
        
//        System.out.println(dbUrl);

        String dbUser = prop.getProperty("db.user");
        String dbPassword = prop.getProperty("db.password");
        
        try{
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
                
        } catch (SQLException e) {
            e.printStackTrace();
            
            return null;
        }
        
    }
    
    private static Properties loadProperties(String propertiesFile) {
        Properties prop = new Properties();
        
        try(InputStream input = new FileInputStream(propertiesFile)){
            prop.load(input);
            
//            System.out.println(prop);
            
            return prop;
        } catch (IOException e) {
            e.printStackTrace();
            
            return null;
        }
    }
    
    private static boolean validateInvariants(Connection conn, DataModel dm) {

        Set<Invariants> invSet = dm.getInvariants();
        
        if (invSet.size() == 0) {
            return true;
        }

        OCL2PSQL_2 ocl2psql = new OCL2PSQL_2();
        ocl2psql.setDataModel(dm);
        ocl2psql.setDescriptionMode(true);

        for (Invariants invs : invSet) {
            for (Invariant inv : invs) {
                try {
                    String sqlInv = ocl2psql.mapOCLStringToSQLString(inv.getOcl());
                    Statement st = conn.createStatement();
                    ResultSet rs = st.executeQuery(sqlInv);
                    
                    while (rs.next()) {
//                        System.out.println("\n=======\n" + 
//                                inv.getLabel() + "\n" 
//                                + inv.getOcl() + "\n\n" + sqlInv + "\n\n" + 
//                                "---> result:" + rs.getInt("res") + "\n=======\n");
                        if (rs.getInt("res") == 0) {
                            return false;
                        }
                    }

                } catch (IOException|ParseException|OclParseException|SQLException e) {
//                    System.out.println("\n=======\n" + 
//                            inv.getLabel() + "\n" 
//                            + inv.getOcl() + "\n\n" );
                    e.printStackTrace();
                    return false;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }
        
        return true;
    }
}
