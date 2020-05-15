package org.access.installer;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class TextFieldCustom extends JTextPane {


    // Стили редактора
    private Style heading = null; // стиль заголовка
    private Style normal = null; // стиль текста

    private final String STYLE_heading = "heading",
            STYLE_normal = "normal",
            FONT_style = "Times New Roman";
    private String[][] TEXT;

    public TextFieldCustom(String[][] textAndFonts) {
        // Создание редактора
        // Текстовый редактор
        TEXT = textAndFonts;
        setEditable(false);
        setFocusable(false);
        // Определение стилей редактора
        createStyles(this);
        // Загрузка документа
        loadText(this);
        changeDocumentStyle(this);
    }

    /**
     * Процедура формирования стилей редактора
     *
     * @param editor редактор
     */
    private void createStyles(JTextPane editor) {
        // Создание стилей
        normal = editor.addStyle(STYLE_normal, null);
        StyleConstants.setFontFamily(normal, FONT_style);
        StyleConstants.setFontSize(normal, 16);
        // Наследуем свойство FontFamily
        heading = editor.addStyle(STYLE_heading, normal);
        StyleConstants.setFontSize(heading, 24);
        StyleConstants.setBold(heading, true);
    }

    /**
     * Процедура загрузки текста в редактор
     *
     * @param editor редактор
     */
    private void loadText(JTextPane editor) {
        // Загружаем в документ содержимое
        for (String[] strings : TEXT) {
            Style style = (strings[1].equals(STYLE_heading)) ?
                    heading : normal;
            insertText(editor, strings[0], style);
        }
    }

    /**
     * Процедура изменения стиля документа
     *
     * @param editor редактор
     */
    private void changeDocumentStyle(JTextPane editor) {
        // Изменение стиля части текста
        SimpleAttributeSet blue = new SimpleAttributeSet();
        StyleConstants.setForeground(blue, Color.blue);
        StyledDocument doc = editor.getStyledDocument();
        doc.setCharacterAttributes(10, 9, blue, false);
    }

    /**
     * Процедура добавления в редактор строки определенного стиля
     *
     * @param editor редактор
     * @param string строка
     * @param style  стиль
     */
    private void insertText(JTextPane editor, String string,
                            Style style) {
        try {
            Document doc = editor.getDocument();
            doc.insertString(doc.getLength(), string, style);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
