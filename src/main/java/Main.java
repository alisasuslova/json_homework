
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.*;
import com.opencsv.bean.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TransformerException {

                String[] employee = "1,John,Smith,USA,25".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv"))) {
            writer.writeNext(employee);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] employee4 = "2,Inav,Petrov,RU,23".split(",");
        try (CSVWriter writer = new CSVWriter(new FileWriter("data.csv", true))) {
            writer.writeNext(employee4);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String fileName = "data.csv";

        List<Employee> list = parseCSV(columnMapping, fileName);
        String fileName4 = "data.json";
        String json = listToJson(list, fileName4);
        System.out.println(json); //data.json работает!



        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();
        Element root = document.createElement("staff");
        document.appendChild(root);

        Element employee1 = document.createElement("employee");
        root.appendChild(employee1);

        Element id1 = document.createElement("id");
        id1.appendChild(document.createTextNode("1"));
        employee1.appendChild(id1);

        Element firstName1 = document.createElement("firstName");
        firstName1.appendChild(document.createTextNode("John"));
        employee1.appendChild(firstName1);

        Element lastName1 = document.createElement("lastName");
        lastName1.appendChild(document.createTextNode("Smith"));
        employee1.appendChild(lastName1);

        Element country1 = document.createElement("country");
        country1.appendChild(document.createTextNode("USA"));
        employee1.appendChild(country1);

        Element age1 = document.createElement("age");
        age1.appendChild(document.createTextNode("25"));
        employee1.appendChild(age1);


        Element employee2 = document.createElement("employee");
        root.appendChild(employee2);

        Element id2 = document.createElement("id");
        id2.appendChild(document.createTextNode("2"));
        employee2.appendChild(id2);

        Element firstName2 = document.createElement("firstName");
        firstName2.appendChild(document.createTextNode("Inav"));
        employee2.appendChild(firstName2);

        Element lastName2 = document.createElement("lastName");
        lastName2.appendChild(document.createTextNode("Petrov"));
        employee2.appendChild(lastName2);

        Element country2 = document.createElement("country");
        country2.appendChild(document.createTextNode("RU"));
        employee2.appendChild(country2);

        Element age2 = document.createElement("age");
        age2.appendChild(document.createTextNode("23"));
        employee2.appendChild(age2);

        //ЗАПИСЬ ФАЙЛА XML
        DOMSource domSource = new DOMSource(document);  //делаем из документа поток данных
        StreamResult streamResult = new StreamResult(new File("data.xml")); //этот выходной поток записываем в новый файл
        TransformerFactory transformerFactory = TransformerFactory.newInstance(); // создаем еще одну фабрику для трансформера по аналогии с документами
        Transformer transformer = transformerFactory.newTransformer(); // создаем сам трансформер
        transformer.transform(domSource, streamResult);


        String fileName1 = "data.xml";
        String fileName2 = "data2.json";
        parseXML(fileName1, fileName2);


    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) throws FileNotFoundException {

        List<Employee> employeeListlist = null;

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            employeeListlist = csv.parse();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return employeeListlist;
    }


    public static String  listToJson(List<Employee> list, String fileName4) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();

        Type listType = new TypeToken<List<Employee>>() {}.getType();

        String json = gson.toJson(list, listType);

        try (FileWriter fileWriter = new FileWriter(fileName4)) {
            fileWriter.write(json);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return json;
    }


    public static List<Employee> parseXML(String fileName1, String fileName2) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        List<Employee> listEmployee = new LinkedList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(fileName1));
        Node root = doc.getDocumentElement();
        //read(root);

        DOMSource domSource = new DOMSource(doc);
        StreamResult streamResult = new StreamResult(new File(fileName2));
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(domSource, streamResult);

        return listEmployee;

    }

    private static void read(Node node) {

        NodeList nodeList = node.getChildNodes(); //получаем список дочерних элементов через метод  node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) { //проходим по всем дочерним элиментам
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) { //проверряем является ли i-й элемент элементом(может быть текст, атрибут, данные, ссылки и т.д.)
                System.out.println("Текущий узел: " + node_.getNodeName()); //выводим имя
                Element element = (Element) node_; //приводим к типа Element
                NamedNodeMap map = element.getAttributes(); //получаем атрибуты
                for (int a = 0; a < map.getLength(); a++) { //обходим в цикле список атрибутов
                    String attrName = map.item(a).getNodeName(); //получаем имя атрибута
                    String attrValue = map.item(a).getNodeValue(); // получаем значение атрибута
                    System.out.println("Атрибут: " + attrName + "; значение: " + attrValue); // выводим на экран и то и другое
                }
                read(node_);
            }
        }
    }
}
