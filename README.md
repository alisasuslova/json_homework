# Задача 1: CSV - JSON парсер

## Входные данные:
Файл `data.csv` со следующим содержимым в корневой папке проекта:
```csv
1,John,Smith,USA,25
2,Inav,Petrov,RU,23
```
А также класс `Employee`, который будет содержать информацию о сотрудниках.
```java
public class Employee {
    public long id;
    public String firstName;
    public String lastName;
    public String country;
    public int age;

    public Employee(long id, String firstName, String lastName, String country, int age) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", country='" + country + '\'' +
                ", age=" + age +
                '}';
    }
}
``` 
## Выходные данные:
В резльтате работы программы в корне проекта появится файл `data.json` со следующим содержимым:
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "country": "USA",
    "age": 25
  },
  {
    "id": 2,
    "firstName": "Inav",
    "lastName": "Petrov",
    "country": "RU",
    "age": 23
  }
]
```
## Реализация:
1. В классе `Main` в методе `main()` создала массив строчек `columnMapping`, содержащий информацию о предназначении колонок в CVS файле:
```java
String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
```
2. Определила имя для считываемого CSV файла:
```java
String fileName = "data.csv";
```
3. Получила список сотрудников, вызвав метод `parseCSV()`:
```java
List<Employee> list = parseCSV(columnMapping, fileName);
```
4. Метод `parseCSV()`:
```java
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
```
5. Получила JSON с помощью метода `writeToJson()`:
```java
String outFileName1 = "data1.json";
        writeToJson(list1, outFileName1);
```
6. Метод `writeToJson()`:
```java
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
```  
# Задача 2: XML - JSON парсер

## Входные данные:
Файл data.xml в корне проекта, со следующим содержимым:
```csv
<staff>
    <employee>
        <id>1</id>
        <firstName>John</firstName>
        <lastName>Smith</lastName>
        <country>USA</country>
        <age>25</age>
    </employee>
    <employee>
        <id>2</id>
        <firstName>Inav</firstName>
        <lastName>Petrov</lastName>
        <country>RU</country>
        <age>23</age>
    </employee>
</staff>
```
## Выходныее данные:
В резльтате работы программы в корне проекта появится файл `data1.json` со следующим содержимым:
```json
[
  {
    "id": 1,
    "firstName": "John",
    "lastName": "Smith",
    "country": "USA",
    "age": 25
  },
  {
    "id": 2,
    "firstName": "Inav",
    "lastName": "Petrov",
    "country": "RU",
    "age": 23
  }
]
```
## Реализация:
1. Для получения списка сотрудников из XML документа использовала метод parseXML():
```java
List<Employee> list = parseXML("data.xml");
```
2. Метод `parseXML()`:
```java
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
```
3. С помощью ранее написанного метода `writeToJson()` получила файл в формате json:
```java
String outFileName1 = "data1.json";
        writeToJson(list1, outFileName1);
```
