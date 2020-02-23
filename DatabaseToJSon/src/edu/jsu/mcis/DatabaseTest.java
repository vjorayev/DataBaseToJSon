package edu.jsu.mcis;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.*;


public class DatabaseTest {

    public JSONArray getJsonData() {
        
            
        Connection connection = null;
        PreparedStatement pstmt = null;// pstUpdate = null;
        ResultSet resultset = null;
        ResultSetMetaData metadata = null;
      
        ArrayList<HashMap<String, String>> data = new ArrayList<>();
        JSONArray list = new JSONArray();
  
        String query, key, value;
        
        boolean hasresults;
        int resultCount, columnCount, updateCount = 0;
        
        try {
            
            /* Identify the Server */
            
            String server = ("jdbc:mysql://localhost/p2_test");
            String username = "root";
            String password = "CS325";
            System.out.println("Connecting to " + server + "...");
            
            /* Load the MySQL JDBC Driver */
            
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            /* Open Connection */

            connection = DriverManager.getConnection(server, username, password);

            /* Test Connection */
            
            if (connection.isValid(0)) {
                
                /* Connection Open! */
                
                System.out.println("Connected Successfully!");
  
                /* Prepare Select Query */
                
                query = "SELECT * FROM people";
                pstmt = connection.prepareStatement(query);
                
                /* Execute Select Query */
                
                System.out.println("Submitting Query ...");
                
                hasresults = pstmt.execute();                
                
                /* Get Results */
                
                System.out.println("Getting Results ...");
                
                while ( hasresults || pstmt.getUpdateCount() != -1 ) {

                    if ( hasresults ) {
                        
                        /* Get ResultSet Metadata */
                        
                        resultset = pstmt.getResultSet();
                        metadata = resultset.getMetaData();
                        columnCount = metadata.getColumnCount();

                        /* Get Data; Print as Table Rows */
                       
                        
                        while(resultset.next()) {
                            
                            /* Begin Next ResultSet Row */

                            HashMap<String,String> row = new LinkedHashMap();
                            /* Loop Through ResultSet Columns; Print Values */
                            
                           
                            
                           
                            for (int i = 2; i <= columnCount; i++) {
                                key = metadata.getColumnLabel(i);
                                value = (String) resultset.getString(i);
                              
                          
                                row.put(key , value);
                             data.add(row);
                              
                              
                               
                           
                            }
                            
                        }
                      
                     
                      for(HashMap<String, String> d :data){
                          if(list.contains(d)){
                            continue;
                          }
                          list.add(d);
                      }
                    }
                    
                    else {

                        resultCount = pstmt.getUpdateCount();  

                        if ( resultCount == -1 ) {
                            break;
                        }

                    }
                    
                    /* Check for More Data */

                    hasresults = pstmt.getMoreResults();

                }   
                
            }
            
            System.out.println();
            
            /* Close Database Connection */
            
            connection.close();
            
        }
        
        catch (Exception e) {
            System.err.println(e.toString());
        }
        
        /* Close Other Database Objects */
        
        finally {
            
            if (resultset != null) { try { resultset.close(); resultset = null; } catch (Exception e) {} }
            
            if (pstmt != null) { try { pstmt.close(); pstmt = null; } catch (Exception e) {} }
                       
        }
        return list;
    }
    
}