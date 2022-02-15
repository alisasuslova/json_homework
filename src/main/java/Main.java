
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";
        List<Employee> list = parseCSV(columnMapping, fileName);
        System.out.println(list);

        String outFileName = "data.json";
        writeToJson(list, outFileName);

        String fileName1 = "data.xml";
        List<Employee> list1 = parseXML(fileName1);
        System.out.println(list1);
        String outFileName1 = "data1.json";
        writeToJson(list1, outFileName1);

    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> list = null;

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            list = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<Employee> parseXML(String fileName1) throws IOException, SAXException, ParserConfigurationException {

        List<Employee> employeeList = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new File(fileName1));
        Node root = document.getDocumentElement();

        NodeList rootChilds = root.getChildNodes();

        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;

        for (int i = 0; i < rootChilds.getLength(); i++) {
            if (rootChilds.item(i).getNodeType() == Node.ELEMENT_NODE) {
                Node employee = rootChilds.item(i); // аналогично people
                NodeList employeeChilds = employee.getChildNodes();

                for (int j = 0; j < employeeChilds.getLength(); j++) {
                    if (employeeChilds.item(j).getNodeType() == Node.ELEMENT_NODE) {

                        switch (employeeChilds.item(j).getNodeName()) {
                            case "id": {
                                id = Long.parseLong(employeeChilds.item(j).getTextContent());
                                break;
                            }
                            case "firstName": {
                                firstName = employeeChilds.item(j).getTextContent();
                                break;
                            }
                            case "lastName": {
                                lastName = employeeChilds.item(j).getTextContent();
                                break;
                            }
                            case "country": {
                                country = employeeChilds.item(j).getTextContent();
                                break;
                            }
                            case "age": {
                                age = Integer.parseInt(employeeChilds.item(j).getTextContent());
                                break;
                            }
                        }
                    }
                }
                Employee employee1 = new Employee(id, firstName, lastName, country, age);
                employeeList.add(employee1);
            }
        }
        return employeeList;
    }


    public static String writeToJson(List<Employee> list, String outFileName) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {
        }.getType();
        String json = gson.toJson(list, listType);

        try (FileWriter fileWriter = new FileWriter(outFileName)) {
            fileWriter.write(json);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return json;
    }
}
