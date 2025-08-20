import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.border.LineBorder;

public class Calculadora 
{
    int boardWidth = 360;
    int boardHeight = 540;
    
    Color customGraniteGray = new Color(95, 99, 104);
    Color customOldSilver = new Color(128, 134, 139);
    Color customArsenic = new Color(60, 64, 67);
    Color customBlueberry = new Color(66, 133, 244);
    
    String[] buttonValues = 
    {
        "AC", "+/-", "%", "÷", 
        "7", "8", "9", "×", 
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };
    
    String[] rightSymbols = {"÷", "×", "-", "+", "=", "√"};
    String[] topSymbols = {"AC", "+/-", "%"};
    
    JFrame frame = new JFrame("Calculadora");
    JLabel displayLabel = new JLabel();
    JPanel panel = new JPanel();
    JPanel buttons = new JPanel();
    
    String A = "0";
    String B = null;
    String operator = null;
    boolean newNumber = true; // controlar entrada de novos números
    
    Calculadora() 
    {
        setupFrame();
        setupDisplay();
        setupButtons();
        frame.setVisible(true);
    }
    
    void setupFrame() 
    {
        frame.setResizable(false);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }
    
    void setupDisplay() 
    {
        displayLabel.setBackground(customArsenic);
        displayLabel.setForeground(Color.WHITE);
        displayLabel.setFont(new Font("Arial", Font.BOLD, 70));
        displayLabel.setHorizontalAlignment(JLabel.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);
        displayLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.setLayout(new BorderLayout());
        panel.add(displayLabel);
        frame.add(panel, BorderLayout.NORTH);
    }
    
    void setupButtons() 
    {
        buttons.setLayout(new GridLayout(5, 4));
        buttons.setBackground(customGraniteGray);
        frame.add(buttons, BorderLayout.CENTER);
        
        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];
            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customArsenic));
            
            // cores dos botões
            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customOldSilver);
                button.setForeground(Color.WHITE);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customBlueberry);
                button.setForeground(Color.WHITE);
            } else {
                button.setBackground(customGraniteGray);
                button.setForeground(Color.WHITE);
            }
            
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton clickedButton = (JButton) e.getSource();
                    String buttonValue = clickedButton.getText();
                    handleButtonClick(buttonValue);
                }
            });
            
            buttons.add(button);
        }
    }
    
    void handleButtonClick(String buttonValue) {
        try {
            if (buttonValue.equals("√")) {
                calcRaizQuadrada();
            } else if (buttonValue.equals("=")) {
                calculateResult();
            } else if ("+-×÷".contains(buttonValue)) {
                handleOperator(buttonValue);
            } else if (Arrays.asList(topSymbols).contains(buttonValue)) {
                handleTopSymbol(buttonValue);
            } else if (buttonValue.equals(".")) {
                handleDecimalPoint();
            } else if ("0123456789".contains(buttonValue)) {
                handleNumber(buttonValue);
            }
        } catch (Exception ex) {
            displayLabel.setText("Error");
            clearAll();
        }
    }
    
    void calcRaizQuadrada() {
        double currentValue = Double.parseDouble(displayLabel.getText());
        
        if (currentValue < 0) {
            displayLabel.setText("Error");
            return;
        }
        
        double result = Math.sqrt(currentValue);
        displayLabel.setText(removeZeroDecimal(result));
        
        // Resetar para permitir nova operação
        A = removeZeroDecimal(result);
        B = null;
        operator = null;
        newNumber = true;
    }
    
    void calculateResult() {
        if (A != null && operator != null && !displayLabel.getText().equals("Error")) {
            double numA = Double.parseDouble(A);
            double numB = Double.parseDouble(displayLabel.getText());
            double result = 0;
            
            switch (operator) {
                case "+":
                    result = numA + numB;
                    break;
                case "-":
                    result = numA - numB;
                    break;
                case "×":
                    result = numA * numB;
                    break;
                case "÷":
                    if (numB != 0) {
                        result = numA / numB;
                    } else {
                        displayLabel.setText("Error");
                        return;
                    }
                    break;
            }
            
            displayLabel.setText(removeZeroDecimal(result));
            A = removeZeroDecimal(result);
            B = null;
            operator = null;
            newNumber = true;
        }
    }
    
    void handleOperator(String op) 
    {
        if (operator != null && !newNumber) {
            calculateResult(); // calcular resultado anterior (se existir)
        }
        
        A = displayLabel.getText();
        operator = op;
        newNumber = true;
    }
    
    void handleTopSymbol(String symbol) {
        switch (symbol) {
            case "AC":
                clearAll();
                break;
            case "+/-":
                trocarSinal();
                break;
            case "%":
                calculatePercentage();
                break;
        }
    }
    
    void trocarSinal() {
        String currentText = displayLabel.getText();
        if (!currentText.equals("0") && !currentText.equals("Error")) {
            if (currentText.startsWith("-")) {
                displayLabel.setText(currentText.substring(1));
            } else {
                displayLabel.setText("-" + currentText);
            }
        }
    }
    
    void calculatePercentage() {
        if (!displayLabel.getText().equals("Error")) {
            double numDisplay = Double.parseDouble(displayLabel.getText());
            numDisplay /= 100;
            displayLabel.setText(removeZeroDecimal(numDisplay));
        }
    }
    
    void handleDecimalPoint() {
        if (newNumber) {
            displayLabel.setText("0.");
            newNumber = false;
        } else if (!displayLabel.getText().contains(".")) {
            displayLabel.setText(displayLabel.getText() + ".");
        }
    }
    
    void handleNumber(String number) {
        if (newNumber || displayLabel.getText().equals("0")) {
            displayLabel.setText(number);
            newNumber = false;
        } else {
            // limita o numero de digitos para evitar overflow no display
            if (displayLabel.getText().length() < 12) {
                displayLabel.setText(displayLabel.getText() + number);
            }
        }
    }
    
    void clearAll() 
    {
        A = "0";
        B = null;
        operator = null;
        displayLabel.setText("0");
        newNumber = true;
    }
    
    String removeZeroDecimal(double numDisplay) {
        if (numDisplay % 1 == 0) {
            return Integer.toString((int) numDisplay);
        } else {
            // limita as casas decimais
            return String.format("%.10f", numDisplay).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }
    
    public static void main(String[] args) {
        // look and feel do sistema
        try {
            UIManager.setLookAndFeel(UIManager.getLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new Calculadora();
        });
    }
}