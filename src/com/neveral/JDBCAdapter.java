package com.neveral;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.table.AbstractTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Neveral on 18.11.14.
 */
public class JDBCAdapter extends AbstractTableModel {
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    // здесь будут содержаться названия таблиц БД
    public  String[] columnNames = {};
    // здесь будут  храниться все данные таблицы БД
    private Vector<Vector> rows = new Vector();
    private ResultSetMetaData metaData;
    public String tableName="";
    private DocumentBuilder builder;

    public JDBCAdapter()
    {
        try
        {
            connection = getConnection();
            statement = connection.createStatement();
        }
        catch (IOException ex)
        {
            System.err.println
                    ("Cannot find the database properties file.");
            System.err.println(ex);
        }
        catch (SQLException ex)
        {
            System.err.println
                    ("Cannot connect to this database.");
            System.err.println(ex);
        }
    }

    public static Connection getConnection() throws SQLException, IOException {
        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))) {
            props.load(in);
        }

        String drivers = props.getProperty("jdbs.drivers");
        if(drivers != null) System.setProperty("jdbc.drivers", drivers);
        String url = props.getProperty("jdbc.url");
        String username = props.getProperty("jdbc.username");
        String password = props.getProperty("jdbc.password");

        props.put("user", username);
        props.put("password", password);
        props.put("useUnicode", "true");
        props.put("characterEncoding", "utf8");
        props.put("yearIsDateType", "false"); //выводит MySQL тип Year как Short// а не как Date
        System.out.println(props);
        return DriverManager.getConnection(url, props);
    }

    public void executeQuery(String query)
    {
        if (connection == null || statement == null)
        {
            System.err.println
                    ("There is no database to execute the query.");
            return;
        }
        try
        {
            resultSet = statement.executeQuery(query);
            metaData = resultSet.getMetaData();
            // получим количество полей исследуемой таблицы БД
            int numberOfColumns =  metaData.getColumnCount();
            columnNames = new String[numberOfColumns];
            // получим названия этих полей и присвоим их нашему
            // полю columnNames
            for(int column = 0; column < numberOfColumns; column++)
            {
                columnNames[column] =
                        metaData.getColumnLabel(column+1);
            }
            // Начнем получать данные из БД
            rows = new Vector();
            while (resultSet.next()) {
                Vector<String> newRow = new Vector();
                for (int i = 1; i <= getColumnCount(); i++) {
                    newRow.addElement(resultSet.getObject(i).toString());
                }
                // прибавим новую  запись
                rows.addElement(newRow);
            }
            // оповестим listeners о том, что таблица изменена
            fireTableChanged(null);
        }
        catch (SQLException ex)
        {
            System.err.println(ex);
        }
    }

    public void addData(String query) {
        if (connection == null || statement == null)
        {
            System.err.println
                    ("There is no database to execute the query.");
            return;
        }
        try {
            statement.executeUpdate(query);
        }
        catch (SQLException ex)
        {
            System.err.println(ex);
        }
    }

    public void executePreparedQuery(String query, int statementNum, String[] statementValue)
    {
        if (connection == null)
        {
            System.err.println
                    ("There is no database to execute the query.");
            return;
        }
        try
        {
            preparedStatement = connection.prepareStatement(query);
            for(int i=0; i<statementNum; ++i) {
                preparedStatement.setString(i + 1, statementValue[i]);
                //System.out.println(statementValue[i]);
            }

            //System.out.println(preparedStatement);
            resultSet = preparedStatement.executeQuery();
            metaData = resultSet.getMetaData();
            // получим количество полей исследуемой таблицы БД
            int numberOfColumns =  metaData.getColumnCount();
            columnNames = new String[numberOfColumns];
            // получим названия этих полей и присвоим их нашему
            // полю columnNames
            for(int column = 0; column < numberOfColumns; column++)
            {
                columnNames[column] =
                        metaData.getColumnLabel(column+1);
            }
            // Начнем получать данные из БД
            rows = new Vector();
            while (resultSet.next()) {
                Vector newRow = new Vector();
                for (int i = 1; i <= getColumnCount(); i++) {
                    newRow.addElement(resultSet.getObject(i));
                }
                // прибавим новую  запись
                rows.addElement(newRow);
            }
            tableName = metaData.getTableName(1);
            // оповестим listeners о том, что таблица изменена
            fireTableChanged(null);
        }
        catch (SQLException ex)
        {
            System.err.println(ex);
        }
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public int getRowCount()
    {
        return rows.size();
    }

    public Object getValueAt(int aRow, int aColumn)
    {
        Vector row = (Vector)rows.elementAt(aRow);
        return row.elementAt(aColumn);
    }

    public String getColumnName(int num) {
        return columnNames[num];
    }



    //==========================================================
    public boolean isCellEditable(int row, int column)
    {
        try
        {
            return metaData.isWritable(column+1);
        }
        catch (SQLException e)
        {
            return false;
        }
        // или просто, если не заботиться о безопасности
        // return true;
    }

    public void setValueAt(Object value, int row, int column)
    {
        try
        {
            // Эта функция
            // возвращает название поля по его номеру
            String columnName = getColumnName(column);
            if(!checkValue(columnName, value))
                throw new checkValueException("Данные введены некорректно!");

            tableName = metaData.getTableName(column+1);

            String query =
                    "UPDATE "+tableName+
                            " SET "+columnName+" = \""+
                            dbRepresentation(column, value)+
                            "\" WHERE ";

            // метод  dbRepresentation(column, value)
            // преобразует значения в строку,
            // для некоторых типов
            // обычное toString() не подойдет

            // т.к.  мы  не знаем, какое поле
            // таблицы является ключевым,
            // мы будем осуществлять поиск по всем
            // полям  одновременно.
            // Создадим строку, которую будем
            // использовать в запросе SQL

            for(int col = 0; col < getColumnCount();col++)
            {
                String colName = getColumnName(col);
                if (colName.equals(""))
                {
                    continue;
                }
                if (col != 0)
                {
                    query = query + " and ";
                }
                query = query + colName +" = \""+
                        dbRepresentation(col, getValueAt(row, col)) + "\"";
            }
            //System.out.println(query);
            // запрос готов, выполним изменения
            PreparedStatement pstmt =
                    connection.prepareStatement(query);
            pstmt.executeUpdate();  // выполнить изменение
            // теперь осталось, изменить значение вектора  rows

            Object val;
            // без этой функции работать не  будет,
            // если в setElementAt() вставлять непосредственно
            // value а не val
            //val = tbRepresentation(column,value);
            Vector dataRow = (Vector)rows.elementAt(row);
            dataRow.setElementAt(value, column); //если поставить value то все работает РАЗОБРАТЬСЯ"!!
        }
        catch (Exception ex)
        {
            System.err.println(ex);
            AdminFrame.setStatusLabelText(ex.getMessage());
        }
    }

    /*public Object tbRepresentation(int column, Object value)
            throws NumberFormatException {
        Object val;
        int type;
        if (value == null) {
            return "null";
        }
        try {
            type = metaData.getColumnType(column + 1);
        } catch (SQLException e) {
            return value.toString();
        }
        switch (type) {
            case Types.BIGINT:
                return val = new Long(value.toString());
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
                return val = new Integer(value.toString());
            case Types.REAL:
            case Types.FLOAT:
            case Types.DOUBLE:
                return val = new Double(value.toString());
            default: return val = new Object();
            //  и  т.д.  для  всех типов SQL
        }
    }*/

    public String dbRepresentation(int column, Object value) {
        String val;
        int type;
        if (value == null) {
            return "null";
        }
        try {
            type = metaData.getColumnType(column + 1);
        } catch (SQLException e) {
            return value.toString();
        }
        switch (type) {

            default:
                return val = new String(value.toString());
            //  и  т.д.  для  всех типов SQL
        }
    }

    public boolean checkValue(String columnName, Object value){
        switch(columnName){
            case "amount":
            case "manufact_year":
                if(value.toString().matches("[0-9]+"))
                    return true;
                else
                    return false;
            case "driving_permit":
                if(value.toString().matches("[0-9]{10}"))
                    return true;
                else
                    return false;
            case "brand":
            case "model":
            case "first_name":
            case "last_name":
                if(value.toString().matches("[a-zA-Zа-яА-Я0-9]+"))
                    return true;
                else
                    return false;
            case "birth_date":
                if(value.toString().matches("\\d{4}-\\d{2}-\\d{2}"))
                    return true;
                else
                    return false;
            case "date_and_time":
                if(value.toString().matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}"))
                    return true;
                else
                    return false;

        }
        return false;
    }

    public void clearAll() {
        tableName = "";
        rows.clear();
        for(int i=0; i<columnNames.length; ++i)
            columnNames[i] = "";
        fireTableChanged(null);
    }

    public void createXML() throws TransformerException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
        }
        catch (ParserConfigurationException e) { e.printStackTrace(); }

        Document doc = builder.newDocument();
        Element RootElement=doc.createElement(tableName+"s");

        Vector<Element> tagsVector = new Vector();
        Vector<Element> group = new Vector();
        for (int i=0; i<rows.size(); ++i) {
            group.add(doc.createElement(tableName));
            for (int j=0; j<rows.get(0).size(); ++j) {
                tagsVector.add(doc.createElement(columnNames[j]));
                tagsVector.get(j).appendChild(doc.createTextNode(rows.get(i).get(j).toString()));
                group.get(i).appendChild(tagsVector.get(j));
            }
            tagsVector.clear();
            RootElement.appendChild(group.get(i));
        }

        doc.appendChild(RootElement);

        Transformer t= TransformerFactory.newInstance().newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.transform(new DOMSource(doc), new StreamResult(new FileOutputStream(tableName+".xml")));

    }

    class checkValueException extends Exception {
        public checkValueException(String errMsg) {
            super(errMsg);
        }
    }
}