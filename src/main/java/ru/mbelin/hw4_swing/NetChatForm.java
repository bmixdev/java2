package ru.mbelin.hw4_swing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetChatForm extends JFrame {
    private JPanel panel1;
    private JTextField textfieldMsg;
    private JButton buttonSend;
    private JList<String> listContacts;
    private JTextPane textArea;
    private JPanel panel3;
    private JPanel panel4;
    private JPanel panel2;
    private JScrollPane scrollpaneList;

    // Стили редактора
    private Style heading    = null; // стиль заголовка
    private Style normal     = null; // стиль текста

    private  final  String      STYLE_heading = "heading",
                                STYLE_normal  = "normal" ,
                                FONT_style    = "Times New Roman";

    private static int sizeWidth = 800;
    private static int sizeHeight = 600;
    private static int locationX;
    private static int locationY;

    private String selectedContact;

    public NetChatForm() {
        setTitle("Net Chat");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // по центру экрана
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        locationX = (screenSize.width - sizeWidth) / 2;
        locationY = (screenSize.height - sizeHeight) / 2;

        setBounds(new Rectangle(locationX, locationY, sizeWidth, sizeHeight));
        // padding 10 10 10 10
        setPadding(panel1, panel2, panel3);
        setContentPane(panel1);

        createStyles(textArea);
        //установка меню
        setJMenuBar(makeMenuBar());
        //устновка слушателей
        setListener();
        setVisible(true);
    }

    /**
     * Метод загрузки контактов для чата
     * @param contacts Списко контактов List<String>
     */
    public void loadListContacts(List<String> contacts) {
        for (String c: contacts) {

        }
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listContacts.setModel(listModel);
        contacts.forEach(item-> listModel.addElement(item));
    }

    private void setTextArea() {
        String s = textfieldMsg.getText();
        Pattern pattern = Pattern.compile("@.+?:");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            insertText(textArea, s.substring(matcher.start(), matcher.end()), heading);
            insertText(textArea, s.substring(matcher.end(), s.length()) +"\n", normal);
        }
        else
            insertText(textArea, textfieldMsg.getText() + "\n", normal);
        textfieldMsg.setText(null);
    }

    private void setListener() {
        buttonSend.addActionListener(e -> {
          setTextArea();
        });
        textfieldMsg.addActionListener(e ->  {
          setTextArea();
        });

        listContacts.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                selectedContact = listContacts.getSelectedValue();
                textfieldMsg.setText("@"+selectedContact+": ");
            }
        });
    }

    private void setPadding(JComponent... panels) {
        for (JComponent p: panels)
            p.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    /**
     * Метод создания менюшки
     */
    private JMenuBar makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menuFile = new JMenu("Файл");
        JMenuItem exitMenuItem = new JMenuItem("Выход");
        exitMenuItem.addActionListener((e) -> {this.dispose();});
        menuFile.add(exitMenuItem);
        menuBar.add(menuFile);
        return menuBar;
    }

    /**
     * Процедура формирования стилей редактора
     * @param editor редактор
     */
    private void createStyles(JTextPane editor)
    {
        editor.setEditable(false);
        // Создание стилей
        normal = editor.addStyle(STYLE_normal, null);
        StyleConstants.setFontFamily(normal, FONT_style);
        StyleConstants.setFontSize(normal, 12);
        // Наследуем свойство FontFamily
        heading = editor.addStyle(STYLE_heading, normal);
        StyleConstants.setFontSize(heading, 18);
        StyleConstants.setBold(heading, true);
        StyleConstants.setForeground(heading, Color.blue);
    }

    /**
     * Процедура добавления в редактор строки определенного стиля
     * @param editor редактор
     * @param string строка
     * @param style стиль
     */
    private void insertText(JTextPane editor, String string,
                            Style style)
    {
        try {
            Document doc = editor.getDocument();
            doc.insertString(doc.getLength(), string, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
