package org.access.installer;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {

    public Button(String text) {
        super(text);
        setForeground(Color.BLACK);
        setBackground(new Color(225, 225, 225));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(new Color(229, 241, 251));
                Border line = new LineBorder(new Color(0, 120, 215), 1);
                Border margin = new EmptyBorder(5, 15, 5, 15);
                Border compound = new CompoundBorder(line, margin);
                setBorder(compound);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBackground(new Color(225, 225, 225));
                Border line = new LineBorder(new Color(173, 173, 173), 1);
                Border margin = new EmptyBorder(5, 15, 5, 15);
                Border compound = new CompoundBorder(line, margin);
                setBorder(compound);
            }
        });


        setPreferredSize(new Dimension(75, 20));
        Border line = new LineBorder(new Color(173, 173, 173), 1);
        Border margin = new EmptyBorder(5, 15, 5, 15);
        Border compound = new CompoundBorder(line, margin);
        setBorder(compound);
    }


}
