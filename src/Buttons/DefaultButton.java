package Buttons;

import javax.swing.*;
import java.awt.*;

public class DefaultButton extends JButton {
    public DefaultButton(String text) {
        super(text);
        this.setFont(new Font("Times New Roman", Font.BOLD, 35));
        this.setFocusable(false);
        this.setEnabled(false);

    }
}