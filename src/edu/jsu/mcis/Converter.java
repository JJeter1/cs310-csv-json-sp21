package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
    /*
    
        Consider the following CSV data:
        
        "ID","Total","Assignment 1","Assignment 2","Exam 1"
        "111278","611","146","128","337"
        "111352","867","227","228","412"
        "111373","461","96","90","275"
        "111305","835","220","217","398"
        "111399","898","226","229","443"
        "111160","454","77","125","252"
        "111276","579","130","111","338"
        "111241","973","236","237","500"
        
        The corresponding JSON data would be similar to the following (tabs and
        other whitespace have been added for clarity).  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings, and which values should be encoded as integers!
        
        {
            "colHeaders":["ID","Total","Assignment 1","Assignment 2","Exam 1"],
            "rowHeaders":["111278","111352","111373","111305","111399","111160",
            "111276","111241"],
            "data":[[611,146,128,337],
                    [867,227,228,412],
                    [461,96,90,275],
                    [835,220,217,398],
                    [898,226,229,443],
                    [454,77,125,252],
                    [579,130,111,338],
                    [973,236,237,500]
            ]
        }
    
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
    
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including example code.
    
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results = "";
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            // INSERT YOUR CODE HERE
            JSONObject convertedjsonObject = new JSONObject();
            
            String[] HD = iterator.next();
            JSONArray colHead = new JSONArray();
            JSONArray rowHead = new JSONArray();
            JSONArray dataRow = new JSONArray();
            
            for (int i = 0; i < HD.length; i++)
            {
                colHead.add(HD[i]);
            }
            
            while (iterator.hasNext())
            {
                String[] row = iterator.next();
                JSONArray array = new JSONArray();
                rowHead.add(row[0]);
                
                for (int i = 0; i < (row.length) - 1 ; i++)
                {
                    array.add(parseInt(row[i+1]));
                }
                dataRow.add(array);
            }
            
            convertedjsonObject.put("colHeaders", colHead);
            convertedjsonObject.put("rowHeaders", rowHead);
            convertedjsonObject.put("data", dataRow);
            
            results = JSONValue.toJSONString(convertedjsonObject);
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results = "";
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
            // INSERT YOUR CODE HERE
            JSONParser parser = new JSONParser();
            
            JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
            JSONArray array1 = (JSONArray) jsonObject.get("rowHeaders");
            JSONArray array2 = (JSONArray) jsonObject.get("data");
            JSONArray array3 = (JSONArray) jsonObject.get("columnHeaders");
            
            Iterator<String> ID = array1.iterator();
            Iterator<JSONArray> data = array2.iterator();
            Iterator<String> colHead = array3.iterator();
            
            ArrayList<String> IDS = new ArrayList<String>();
            ArrayList<JSONArray> Data = new ArrayList<JSONArray>();
            ArrayList<String> Colhead = new ArrayList<String>();
            
            while(ID.hasNext())
            {
                IDS.add(ID.next());
            }
            while(data.hasNext())
            {
                Data.add(data.next());
            }
            while(colHead.hasNext())
            {
                Colhead.add(colHead.next());
            }
            
            String[] cols = Colhead.toArray(new String[0]);
            csvWriter.writeNext(cols);
            ArrayList<String[]> datas = new ArrayList<String[]>();
            for (int i=0;i<Data.size();i++)
            { 
                String[] dat = Data.get(i).toString().split(",");
                dat[0] = dat [0].split("/[")[1];
                dat[dat.length-1] = dat[dat.length-1].split("]")[0];
                datas.add(dat);
            }
            for(int i = 0; i < datas.size(); i++)
            {
                String[] line = new String[datas.get(0).length+1];
                String[] dat = datas.get(i);
                line[0] = IDS.get(i);
                for(int j = 1; j < dat.length+1;j++)
                {
                    line[j] = dat[j-1];
                }
                csvWriter.writeNext(line);
            }
            results = writer.toString();
           
            
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}