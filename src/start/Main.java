package start;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;


public class Main {
    private JFrame frame;
    private Student student;
    private static List<Student> STUDENTS = new ArrayList<Student>();
    private JPanel insertPanel;
    private JScrollPane showPanel;
    private List<Student> tableList;

    Main() {
        frame = new JFrame("学生管理系统");
        student = new Student();
        insertPanel = new JPanel();
        showPanel = new JScrollPane();
        tableList = STUDENTS;
        setInsertPanel(insertPanel);
        setShowPanel(showPanel);
        frame.setLayout(new GridLayout(2, 1, 1, 1));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.add(insertPanel);
        frame.add(showPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    public void setShowPanel(JScrollPane panel) {
        panel = panel == null ? new JScrollPane() : panel;
        panel.getViewport().setVisible(false);
        panel.getViewport().removeAll();
        panel.getViewport().setVisible(true);
        String[] tableHeader = {"编号", "姓名", "性别", "年龄", "学号", "学校", "班级"};
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        for (String val : tableHeader) {
            model.addColumn(val);
        }
        int i = 0;
        for (Student student : tableList) {
            i++;
            model.addRow(new Object[]{
                    i + " /" + tableList.size(),
                    student.getName(),
                    student.getGender(),
                    student.getAge(),
                    student.getId(),
                    student.getSchool(),
                    student.getStudentClass(),
            });
        }
        ListSelectionModel selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//
        selectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                System.out.println("选中！行： " + table.getSelectedRow());
                System.out.println("选中！列： " + table.getSelectedColumn());
                int row = table.getSelectedRow();
                student = tableList.get(row);
                setInsertPanel(insertPanel);
            }
        });
        panel.getViewport().add(table);
    }

    public void setInsertPanel(JPanel panel) {
        panel = panel == null ? new JPanel() : panel;
        panel.setVisible(false);
        panel.removeAll();
        panel.setVisible(true);
        JButton buttonInsert;
        JButton buttonSave;
        JButton buttonDelete;
        JButton buttonSave2File;
        JButton buttonOpenFromFile;
        JButton buttonSearch = new JButton("查询");
        JButton buttonShowAll = new JButton("显示全部");
        JLabel nameLabel = new JLabel("姓名:");
        JLabel genderLabel = new JLabel("性别:");
        JLabel birthLabel = new JLabel("出生日期:");
        JLabel yearLabel = new JLabel("年");
        JLabel monthLabel = new JLabel("月");
        JLabel dayLabel = new JLabel("日");
        JLabel idLabel = new JLabel("学号：");
        JLabel classLabel = new JLabel("班级：");
        JLabel schoolLabel = new JLabel("学校：");
        JComboBox genderBox = new JComboBox();
        JComboBox ageYearBox = new JComboBox();
        JComboBox monthBox = new JComboBox();
        JComboBox dayBox = new JComboBox();
        JTextField nameTextField = new JTextField(8);
        JTextField idTextField = new JTextField(10);
        JTextField classTextField = new JTextField(10);
        JTextField schoolTextField = new JTextField(15);
        JPanel birthPanel = new JPanel();
        JLabel searchIdLabel = new JLabel("学号:");
        JTextField searchIdTextField = new JTextField(18);

        GridBagLayout bagLayout = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        panel.setLayout(bagLayout);
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.0;

        birthPanel.setLayout(bagLayout);

        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        panel.add(nameLabel, constraints);

        constraints.gridx = 6;
        constraints.gridwidth = 9;
        panel.add(nameTextField, constraints);

        constraints.gridx = 0;
        constraints.gridwidth = 4;
        birthPanel.add(birthLabel, constraints);

        for (int i = Calendar.getInstance().get(Calendar.YEAR); i > 1900; i--) {
            ageYearBox.addItem(i);
        }
        ageYearBox.setSelectedItem(2000);
        constraints.gridx = 5;
        constraints.gridwidth = 4;
        birthPanel.add(ageYearBox, constraints);

        constraints.gridx = 10;
        constraints.gridwidth = 4;
        birthPanel.add(yearLabel, constraints);

        for (int i = 1; i < 13; i++) {
            monthBox.addItem(i);
        }
        monthBox.setSelectedItem(1);
        constraints.gridx = 15;
        constraints.gridwidth = 2;
        birthPanel.add(monthBox, constraints);

        constraints.gridx = 18;
        constraints.gridwidth = 4;
        birthPanel.add(monthLabel, constraints);

        for (int i = 1; i < 32; i++) {
            dayBox.addItem(i);
        }
        dayBox.setSelectedItem(1);
        constraints.gridx = 23;
        constraints.gridwidth = 2;
        birthPanel.add(dayBox, constraints);

        constraints.gridx = 26;
        constraints.gridy = 0;
        constraints.gridwidth = 0;
        birthPanel.add(dayLabel, constraints);

        constraints.gridx = 15;
        constraints.gridy = 0;
        constraints.gridwidth = 29;
        panel.add(birthPanel, constraints);

        constraints.gridx = 45;
        constraints.gridwidth = 1;
        panel.add(new JPanel(), constraints);

        constraints.gridx = 46;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        panel.add(genderLabel, constraints);

        genderBox.addItem("男");
        genderBox.addItem("女");
        genderBox.setSelectedItem("男");
        constraints.gridx = 50;
        constraints.gridy = 0;
        constraints.gridwidth = 0;
        panel.add(genderBox, constraints);

        constraints.gridx = 2;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        panel.add(schoolLabel, constraints);

        constraints.gridx = 6;
        constraints.gridwidth = 15;
        panel.add(schoolTextField, constraints);

        constraints.gridx = 22;
        constraints.gridwidth = 3;
        panel.add(classLabel, constraints);

        constraints.gridx = 26;
        constraints.gridwidth = 15;
        panel.add(classTextField, constraints);

        constraints.gridx = 42;
        panel.add(idLabel, constraints);

        constraints.gridx = 50;
        panel.add(idTextField, constraints);


        constraints.gridy = 1;
        constraints.gridheight = 1;
        panel.add(new JPanel(), constraints);
        constraints.gridy = 3;
        panel.add(new JPanel(), constraints);

        constraints.gridy = 9;
        buttonInsert = new JButton("插入");
        buttonInsert.setBackground(Color.white);
        buttonSave = new JButton("保存");
        buttonSave.setBackground(Color.white);
        buttonDelete = new JButton("删除");
        buttonDelete.setBackground(Color.white);
        buttonSave2File = new JButton("保存到文件");
        buttonSave2File.setBackground(Color.white);
        buttonOpenFromFile = new JButton("从文件打开");
        buttonOpenFromFile.setBackground(Color.white);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(buttonInsert);
        buttonPanel.add(buttonSave);
        buttonPanel.add(buttonDelete);
        buttonPanel.add(buttonSave2File);
        buttonPanel.add(buttonOpenFromFile);

        constraints.gridy = 5;
        constraints.gridx = 2;
        panel.add(buttonPanel, constraints);

        constraints.gridy = 7;
        panel.add(new JPanel(), constraints);
        constraints.gridx = 2;
        constraints.gridy = 8;
        constraints.gridwidth = 3;
        panel.add(searchIdLabel, constraints);

        constraints.gridx = 6;
        constraints.gridwidth = 9;
        panel.add(searchIdTextField, constraints);


        constraints.gridx = 16;
        constraints.gridwidth = 0;
        buttonSearch.setBackground(Color.lightGray);
        buttonShowAll.setBackground(Color.lightGray);
        JPanel searchPanel = new JPanel();
        searchPanel.add(buttonSearch);
        searchPanel.add(buttonShowAll);
        panel.add(searchPanel, constraints);

        buttonDelete.setVisible(false);
        buttonSave.setVisible(false);

        nameTextField.setText("");
        genderBox.setSelectedItem("");
        schoolTextField.setText("");
        classTextField.setText("");
        idTextField.setText("");

        if (student.getNullItem().length() == 0) {
            nameTextField.setText(student.getName());
            genderBox.setSelectedItem(student.getGender());
            schoolTextField.setText(student.getSchool());
            classTextField.setText(student.getStudentClass());
            idTextField.setText(student.getId());
            ageYearBox.setSelectedItem(student.getBirthDate().getY());
            monthBox.setSelectedItem(student.getBirthDate().getM());
            dayBox.setSelectedItem(student.getBirthDate().getD());
            buttonDelete.setVisible(true);
            buttonSave.setVisible(true);
            buttonInsert.setVisible(false);
        }

        class boxActionListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int m = (int) monthBox.getSelectedItem();
                int y = (int) ageYearBox.getSelectedItem();
                Calendar calendar = Calendar.getInstance();
                calendar.set(y - 1900, m - 1, 1);
                calendar.roll(Calendar.DATE, -1);
                int max = calendar.get(Calendar.DATE);
                dayBox.removeAllItems();
                for (int i = 1; i <= max; i++) {
                    dayBox.addItem(i);
                }
            }
        }
        ageYearBox.addActionListener(new boxActionListener());
        monthBox.addActionListener(new boxActionListener());

        buttonInsert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.setName(nameTextField.getText());
                student.setBirth((int) ageYearBox.getSelectedItem(), (int) monthBox.getSelectedItem(), (int) dayBox.getSelectedItem());
                student.setId(idTextField.getText());
                student.setGender((String) genderBox.getSelectedItem());
                student.setSchool(schoolTextField.getText());
                student.setStudentClass(classTextField.getText());
                student.setSchool(schoolTextField.getText());
                student.setNumLock(STUDENTS.size() + 1);
                if (student.getNullItem().length() != 0) {
                    JOptionPane.showMessageDialog(null, "请输入：" + student.getNullItem() + "字段", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (getStudentByID(student.getId()) != null) {
                    JOptionPane.showMessageDialog(null, "ID 已经存在！", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                STUDENTS.add(student);
                student = new Student();
                setInsertPanel(insertPanel);
                setShowPanel(showPanel);
                System.out.println(STUDENTS.toString());
            }
        });

        buttonSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.setName(nameTextField.getText());
                student.setBirth((int) ageYearBox.getSelectedItem(), (int) monthBox.getSelectedItem(), (int) dayBox.getSelectedItem());
                student.setId(idTextField.getText());
                student.setGender((String) genderBox.getSelectedItem());
                student.setSchool(schoolTextField.getText());
                student.setStudentClass(classTextField.getText());
                student.setSchool(schoolTextField.getText());
                if (student.getNullItem().length() != 0) {
                    JOptionPane.showMessageDialog(null, "请输入：" + student.getNullItem() + "字段", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Student flag = getStudentByID(student.getId());
                if (flag != null && flag.getNumLock() != student.getNumLock()) {
                    JOptionPane.showMessageDialog(null, "ID 已经存在！", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                updateStudentList(student);
                student = new Student();
                setInsertPanel(insertPanel);
                setShowPanel(showPanel);
                System.out.println(STUDENTS.toString());
            }
        });

        buttonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                student.setId(idTextField.getText());
                int r = JOptionPane.showConfirmDialog(null, "确认删除？" + student.getId(), "警告", JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.NO_OPTION) {
                    return;
                }
                Student flag = getStudentByID(student.getId());
                if (flag != null && flag.getNumLock() != student.getNumLock()) {
                    JOptionPane.showMessageDialog(null, "ID 已经存在！", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                deleteStudentList(1);
                student = new Student();
                setInsertPanel(insertPanel);
                setShowPanel(showPanel);
                System.out.println(STUDENTS.toString());
            }
        });

        buttonOpenFromFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFromFile();
            }
        });

        buttonSave2File.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                save2file();
            }
        });

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableList = searchByID(searchIdTextField.getText());
                setShowPanel(showPanel);
            }
        });

        buttonShowAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tableList = STUDENTS;
                setShowPanel(showPanel);
            }
        });
    }

    public List<Student> searchByID(String id) {
        List<Student> list = new ArrayList<Student>();
        if (id.length() == 0) {
            return list;
        }
        for (Student val : STUDENTS) {
            if (id.equals(val.getId())) {
                list.add(val);
            }
        }
        return list;
    }

    public void openFromFile() {
        String filePath = chooseFile();
        if (filePath.length() == 0) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {

            JOptionPane.showMessageDialog(null, "文件不存在或无权限！", "ERROR", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            StringBuffer XMLBuffer = new StringBuffer();
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                XMLBuffer.append(input.nextLine());
            }
            input.close();
            STUDENTS = XML2list(XMLBuffer.toString());
            tableList = STUDENTS;
            setShowPanel(showPanel);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void save2file() {
        String filePath = chooseFile();
        if (filePath.length() == 0) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                System.out.println(filePath);
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "文件创建失败！", "ERROR", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        String writeString = list2XML(STUDENTS);
        if (file.canWrite()) {
            OutputStream stream = null;
            try {
                stream = new FileOutputStream(file);
                stream.write(writeString.getBytes("UTF8"));
                JOptionPane.showMessageDialog(null, "写入成功！", "SUCCESS", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "写入失败！", "ERROR", JOptionPane.ERROR_MESSAGE);
            } finally {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    private String chooseFile() {
        FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT文件", "txt", "TXT");
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(filter);
        fc.setMultiSelectionEnabled(false);
        int result = fc.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            return file.getPath();
        }
        JOptionPane.showMessageDialog(null, "取消！", "失败", JOptionPane.ERROR_MESSAGE);
        return "";
    }

    public void deleteStudentList(int numLock) {
        STUDENTS.removeIf(s -> s.getNumLock() == numLock);
    }

    public void updateStudentList(Student student) {
        Student temp = null;
        for (Student val : STUDENTS) {
            if (val.getNumLock() == student.getNumLock()) {
                temp = val;
            }
        }
        if (temp != null) {
            temp = new Student(student);
            temp.setNumLock(student.getNumLock());
        }
    }

    public Student getStudentByID(String id) {
        if (id.length() == 0) {
            return null;
        }
        for (Student val : STUDENTS) {
            if (val.getId().equals(id)) {
                return val;
            }
        }
        return null;
    }

    public static String list2XML(List<Student> list) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        org.w3c.dom.Document document = builder.newDocument();
        document.setXmlStandalone(true);
        Element student = document.createElement("student");
        for (Student val : list) {
            Element id = document.createElement("studentID");
            id.setAttribute("id", val.getId());
            Element name = document.createElement("name");
            name.setTextContent(val.getName());
            id.appendChild(name);
            Element gender = document.createElement("gender");
            gender.setTextContent(val.getGender());
            id.appendChild(gender);
            Element school = document.createElement("school");
            school.setTextContent(val.getSchool());
            id.appendChild(school);
            Element birth = document.createElement("birth");
            birth.setTextContent(val.getBirth());
            id.appendChild(birth);
            Element studentClass = document.createElement("class");
            studentClass.setTextContent(val.getStudentClass());
            id.appendChild(studentClass);
            student.appendChild(id);
        }
        document.appendChild(student);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(new DOMSource(document), new StreamResult(outStream));
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        System.out.println(outStream.toString());
        return outStream.toString();
    }

    public static List<Student> XML2list(String bf) {
        List<Student> list = new ArrayList<Student>();
        Document document = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            StringBuffer sb = new StringBuffer();
            sb.append(bf);
            InputStream inputStream = new ByteArrayInputStream(bf.getBytes("UTF8"));
            document = documentBuilder.parse(inputStream);
            NodeList students = document.getElementsByTagName("studentID");
            for (int i = 0; i < students.getLength(); i++) {
                Node node = students.item(i);
                System.out.println("find ID:" + node.getAttributes().getNamedItem("id").getNodeValue());
                Student retStudent = new Student();
                retStudent.setId(node.getAttributes().getNamedItem("id").getNodeValue());
                NodeList childNodes = node.getChildNodes();
                for (int j = 0; j < childNodes.getLength(); j++) {
                    String name = "";
                    String nodeVal = "";
                    if (childNodes.item(j).hasChildNodes()) {
                        name = childNodes.item(j).getNodeName();
                        nodeVal = childNodes.item(j).getFirstChild().getNodeValue();
                    }
                    switch (name) {
                        case "name":
                            retStudent.setName(nodeVal);
                            break;
                        case "gender":
                            retStudent.setGender(nodeVal);
                            break;
                        case "school":
                            retStudent.setSchool(nodeVal);
                            break;
                        case "birth":
                            retStudent.setBirth(nodeVal);
                            break;
                        case "class":
                            retStudent.setStudentClass(nodeVal);
                            break;
                        default:
                            System.out.println("unExpect value:" + name);
                    }

                }
                list.add(retStudent);
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void main(String[] args) {
        Main gui = new Main();
    }
}


class Student {
    private MyDate birth;
    private String name;
    private String id;
    private String gender;
    private String school;
    private String studentClass;
    private int numLock;

    public Student() {
    }

    public Student(Student student) {
        this.birth = new MyDate(student.getBirthDate());
        this.name = new String(student.getName());
        this.id = new String(student.getId());
        this.gender = new String(student.getGender());
        this.school = new String(student.getSchool());
        this.studentClass = new String(student.getStudentClass());
        this.numLock = student.getNumLock();
    }

    public String getNullItem() {
        if (this.getGender() == null || this.getGender().replace(" ", "").length() == 0) {
            return "性别";
        } else if (this.getSchool() == null || this.getSchool().replace(" ", "").length() == 0) {
            return "学校";
        } else if (this.getName() == null || this.getName().replace(" ", "").length() == 0) {
            return "姓名";
        } else if (this.getId() == null || this.getId().replace(" ", "").length() == 0) {
            return "学号";
        } else {
            return "";
        }
    }

    public void setNumLock(int numLock) {
        this.numLock = numLock;
    }

    public int getNumLock() {
        return this.numLock;
    }

    public String getStudentClass() {
        return studentClass;
    }

    public String getSchool() {
        return school;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getBirth() {
        return this.birth == null ? "null" : this.birth.toString();
    }

    public MyDate getBirthDate() {
        return this.birth;
    }

    public void setBirth(String s) {
        int yIndex = s.indexOf("y=");
        int mIndex = s.indexOf("m=");
        int dIndex = s.indexOf("d=");
        int rIndex = s.indexOf('}');
        try {
            this.birth = new MyDate(
                    Integer.valueOf(s.substring(yIndex + 2, mIndex - 2)),
                    Integer.valueOf(s.substring(mIndex + 2, dIndex - 2)),
                    Integer.valueOf(s.substring(dIndex + 2, rIndex))
            );
        } catch (Exception e) {
            System.out.println("生日构造失败：" + s + "\n" + e.getMessage());
        }

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStudentClass(String studentClass) {
        this.studentClass = studentClass;
    }

    public void setBirth(int y, int m, int d) {
        this.birth = new MyDate(y, m, d);
    }


    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        return (this.birth.m - m < 0) ? (y - this.birth.y - 1) : y - this.birth.y;
    }

    @Override
    public String toString() {
        return "Student{" +
                "birth=" + birth +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", gender='" + gender + '\'' +
                ", school='" + school + '\'' +
                ", studentClass='" + studentClass + '\'' +
                ", numLock=" + numLock +
                '}';
    }
}

class MyDate {
    int y;
    int m;
    int d;

    MyDate(MyDate birthDate) {
        this(1970, 1, 1);
    }

    MyDate(int y, int m, int d) {
        this.y = y;
        this.m = m;
        this.d = d;
    }

    @Override
    public String toString() {
        return "MyDate{" +
                "y=" + y +
                ", m=" + m +
                ", d=" + d +
                '}';
    }

    public int getD() {
        return d;
    }

    public int getM() {
        return m;
    }

    public int getY() {
        return y;
    }
}

