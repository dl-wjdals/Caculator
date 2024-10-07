/* 2-A반 2022103062학번 이정민
 * 
 * [디자인 개선]
 * 1. 계산기 버튼 색상 및 테두리 추가 : 색의 진하기를 조절하여 버튼 색상을 다르게 하고 테두리를 추가하여 연산자 버튼, 숫자 버튼, 그 외 버튼이 시각적으로 구분되도록 디자인 개선
 * 2. 계산기 폰트 디자인 개선 : 계산기 안에 작성된 글자의 모든 폰트를 'PLAIN'로 통일하였음
 * 3. 메뉴바 색상 변경 및 테두리 추가 : 흰색으로 변경하고 테두리를 추가하여 라벨 창과 구분이 되도록 개선
 * 4. 계산기 버튼 글씨 색상 변경 : 테마를 변경함에 따라 버튼의 글씨의 색상도 변경되도록 개선
 * 
 * [추가한 기능]
 * 1. 루트(√) 연산 기능 추가
 * 2. 파이(π) 연산 기능 추가
 * 3. 로그 및 제곱 연산 기능 추가
 * 4. 백스페이스(←) 및 올클리어(C)기능 추가
 * 5. 삼각함수(sin,cos,tan) 연산 기능 추가
 * 6. 소수점(.) 및 퍼센트(%) 연산 기능 추가
 * 7. 단위 변환 기능 추가 : inch에서 cm로 변환, cm에서 inch로 변환
 * 8. 다양한 테마 변경 기능 추가 :  블루, 핑크, 그린, 베이지 중 선택할 수 있도록 하는 테마 변경 기능이 추가
 * 9. 정보 다이얼로그 추가: "도움말" 메뉴에 "정보" 항목이 추가되어, 사용자가 프로그램에 대한 정보를 확인할 수 있도록 하는 기능 추가
 * 10. 계산 결과에서 불필요한 소수점 및 0 제거 기능 추가: 계산 결과가 정수인 경우 소수점 이하 값이 0이면 제거하는 기능 추가
 * 11. 이전 계산 결과 및 연산자 저장 기능 추가: 연속된 연산을 수행할 경우 이전 결과와 연산자를 저장하여 계속해서 연산을 수행할 수 있도록 하는 기능 추가
 * 12. 연속된 연산 수행 기능 추가
 * 13. 히스토리 라벨 추가 : 식과 결과를 상단에 작은 글씨로 출력하는 레이블 추가
 * 14. 테마 변경 버튼 추가 : 계산기 테마가 적용되었는지 안되었는지 시각적으로 보이게 하기 위해 라디오 버튼을 추가
 * 15. 새로운 식을 시작하거나 현재 결과가 0일 경우 초기화
 * 
 * [시도했으나 실패한 기능(주석처리)]
 * 1. 배열을 이용한 연산자 우선순위 고려 기능 : 이 기능을 추가하니까 원래 있던 곱하기와 나누기 연산이 되지 않고 이 오류에 대한 원인을 찾지 못하였음(199, 239~262)
 * 2. 연산자 교체 기능 : 숫자와 연산자 버튼만 눌린 상태에서 연달아 연산자 버튼을 누르면 2번째로 눌린 연산자가 적용되도록 하는 기능을 
 * 추가하려고 했는데 교체는 안되고 나누기 버튼을 누르니까 에러가 뜨고 어떻게 해야할지 방법을 못찾았음(377~388)
 * 3. 수학적 오류 발생 시 화면에 오류 출력 기능 : try-catch문을 이용하여 오류 처리 하려다가 안돼서
 * ÷부분에 if-else문으로 오류 처리하려다 안되고 에러가 생겨서 테마 적용 기능처럼 배열로 어떻게 해보려다가 더 이상 방법이 생각 안나서 추가하지 못했음(339, 363~368 / 321~334)
 */

package org.zerock.myapp;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.math.BigDecimal;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public class Calculator implements ActionListener {
    JFrame frame;
    JPanel tPanel, bPanel;
    Font font;
    GridLayout grid;
    JLabel label, historyLabel;
    JButton[] btn;
    String[] btnText = {
            "√", "π", "←", "C",
            "sin", "cos", "tan", "÷",
            "7", "8", "9", "+",
            "4", "5", "6", "-",
            "1", "2", "3", "x",
            ".", "0", "%", "=",
            "log", "²√x", "inch", "cm"
    };
    int rows = 7; // 행
    int columns = 4; // 열
    BigDecimal previousResult = BigDecimal.ZERO; // 이전 결과를 저장하는 변수, 부동소수점
    String result = "0"; // 계산 결과를 저장하는 문자열 변수, 초기값 : "0"
    String previousOperator = ""; //이전 산술 연산자 저장
    boolean previousOperatorPressed  = true; // 이전에 사용된 연산자를 저장하는 변수
    boolean isNewExpression = true; // 현재 입력이 새로운 표현식인지를 나타내는 boolean 변수
    boolean decimalEntered = false; // 소수점이 입력되었는지를 나타내는 boolean 변수
    boolean continuousOperation = false; // 연속된 연산을 수행 중인지 여부를 나타내는 변수
    boolean isError = false; // 에러가 발생했는지 여부를 나타내는 변수
    
    // 테마 관련 추가 변수
    JMenuBar menuBar01;
    JMenu Menu01, Menu02;
    JMenuItem menuitem01;
    JToggleButton BlueButton, PinkButton, GreenButton, BeigeButton;

    Calculator() {
        frame = new JFrame("Calculator");
        tPanel = new JPanel();
        bPanel = new JPanel();
        grid = new GridLayout(rows, columns);
        label = new JLabel(result);
        historyLabel = new JLabel(" ");
        btn = new JButton[rows * columns];
        font = new Font("", Font.ITALIC, 20);

        // 버튼 및 레이아웃 설정
        for (int i = 0; i < rows * columns; i++) {
            btn[i] = new JButton();
            bPanel.add(btn[i]);
            btn[i].addActionListener(this);
            btn[i].setText(btnText[i]);
            btn[i].setFont(font);

            // 버튼 색상 설정
            	if (i >= 0 && i <= 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23) {
            		 btn[i].setBackground(new Color(46, 60, 42));
            	        btn[i].setForeground(new Color(240, 245, 239)); 
            	    } else if (i >= 4 && i <= 6 || i >= 24 && i <= 27) {
            	        btn[i].setBackground(new Color(69, 90, 63)); 
            	        btn[i].setForeground(new Color(240, 245, 239)); 
            	    } else {
            	        btn[i].setBackground(new Color(211, 225, 208)); 
            	        btn[i].setForeground(new Color(46, 60, 42)); 
            	        tPanel.setBackground(new Color(240, 245, 239)); 
            	        label.setForeground(new Color(32, 47, 29)); 
            	        historyLabel.setForeground(new Color(32, 47, 29)); 
            	    }
            	    btn[i].setBorder(BorderFactory.createLineBorder(new Color(240, 245, 239)));
        }

        // 레이아웃 설정
        tPanel.add(label);
        tPanel.setBounds(0, 0, 400, 150);
        tPanel.setBackground(new Color(240, 245, 239));
        tPanel.setLayout(null);
        tPanel.add(historyLabel);

        bPanel.setBounds(0, 150, 400, 560);
        bPanel.setBackground(new Color(240, 245, 239));
        bPanel.setLayout(grid);

        label.setBounds(20, 20, 340, 30);
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setBackground(Color.WHITE);
        label.setForeground(new Color(32, 47, 29));
        label.setFont(new Font("", Font.PLAIN, 30));
        
        // 식과 결과를 상단에 작은 글씨로 출력할 레이블 추가
        historyLabel.setBounds(20, 0, 340, 20);
        historyLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        historyLabel.setBackground(Color.WHITE);
        historyLabel.setForeground(new Color(32, 47, 29));
        historyLabel.setFont(new Font("", Font.PLAIN, 14));

        // 프레임 설정
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(415, 765);
        frame.setResizable(false);

        // 테마 관련 설정
        menuBar01 = new JMenuBar();
        Menu01 = new JMenu("테마 변경");
        
        //정보 관련 설정
        Menu02 = new JMenu("도움말");
        menuitem01 = new JMenuItem("정보");
        
        // Calculator 클래스 생성자에서 "정보" 메뉴 아이템을 설정
        menuitem01.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // "정보" 메뉴 아이템을 클릭했을 때 정보 다이얼로그를 표시
                showInformationDialog();
            }
        });
        
        // 라디오 버튼 그룹 생성
        ButtonGroup themeGroup = new ButtonGroup();
        
        BlueButton = new JRadioButton("Blue");
        PinkButton = new JRadioButton("Pink");
        GreenButton = new JRadioButton("Green");
        BeigeButton = new JRadioButton("Beige");

        // 기본 테마 설정
        GreenButton.setSelected(true);

        // 테마 변경 리스너 등록
        BlueButton.addItemListener(new ThemeChangeListener());
        PinkButton.addItemListener(new ThemeChangeListener());
        GreenButton.addItemListener(new ThemeChangeListener());
        BeigeButton.addItemListener(new ThemeChangeListener());

        // 라디오 버튼 그룹에 추가
        themeGroup.add(BlueButton);
        themeGroup.add(PinkButton);
        themeGroup.add(GreenButton);
        themeGroup.add(BeigeButton); 
        
    	// BlueButton, PinkButton, GreenButton, BeigeButton에 대한 폰트 및 크기 변경
        BlueButton.setFont(new Font("", Font.PLAIN, 12));
        PinkButton.setFont(new Font("", Font.PLAIN, 12));        
        GreenButton.setFont(new Font("", Font.PLAIN, 12));
        BeigeButton.setFont(new Font("", Font.PLAIN, 12));
  
        // 테마 변경 메뉴 구성 - 
        Menu01.add(BlueButton);
        Menu01.add(PinkButton);
        Menu01.add(GreenButton);
        Menu01.add(BeigeButton);
        
        //도움말 메뉴 안에 정보 메뉴 추가
        Menu02.add(menuitem01);
        
        // 메뉴의 폰트 및 색상 변경
        Menu01.setFont(new Font("고딕", Font.BOLD, 12));
        Menu02.setFont(new Font("고딕", Font.BOLD, 12));

        menuBar01.add(Menu01);
        menuBar01.add(Menu02);
        
        //메뉴바 색상 변경 밎 테두리 추가
        menuBar01.setBackground(Color.WHITE);
        menuBar01.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(200, 200, 200)));

        //프레임에 메뉴바 추가
        frame.setJMenuBar(menuBar01);

        frame.add(tPanel);
        frame.add(bPanel);

        frame.setVisible(true);
    }


    public static void main(String[] args) {
        new Calculator();
    }

    // 연산 수행 메소드
    void Calc() {
    		String num[] = result.split("[+]|[-]|[x]|[÷]");
    	    /*int priority[] = {1, 1, 2, 2};*/
    		String op[] = new String[num.length - 1];
    		int  j = 0;
    		for(int i = 0; i < result.length(); i++) {
    			if (result.charAt(i) == '+' || result.charAt(i) == '-' || result.charAt(i) == 'x' || result.charAt(i) == '÷') {     
    				op[j] = String.valueOf(result.charAt(i));
    				j++;
    			}
    		}
    		
    		double calResult = 0;
    		for(int i = 0; i < num.length-1; i++) {
    			if(op[i].equals("x")) {
    				calResult = Double.parseDouble(num[i]) * Double.parseDouble(num[i+1]);
    				num[i+1] = Double.toString(calResult);
    			} else if(op[i].equals("÷")) {
    				calResult = Double.parseDouble(num[i]) / Double.parseDouble(num[i+1]);
    				num[i+1] = Double.toString(calResult);
    			}
    		}
    		for(int i = 0; i < num.length-1; i++) {
    			if(op[i].equals("+")) {
    				calResult = Double.parseDouble(num[i]) + Double.parseDouble(num[i+1]);
    				num[i+1] = Double.toString(calResult);
    			}else if(op[i].equals("-")) {
    				calResult = Double.parseDouble(num[i]) - Double.parseDouble(num[i+1]);
    				num[i+1] = Double.toString(calResult);
    			}
    		}
    		// 최종 결과를 문자열로 저장
            result = Double.toString(calResult);
            
            // 정수인 경우 소수점 이하 값이 0이면 소수점 이하 생략
            if (result.endsWith(".0")) {
            	result = result.substring(0, result.length() - 2);
            }
            // 사용자가 다른 버튼과 동일하게 소수점 버튼을 계속 사용할 수 있도록 소수점 입력 상태 초기화
            decimalEntered = false;
        }

    	/*// 연산자의 우선순위를 반환하는 메소드
    	private int getPriority(String operator) {
    	    switch (operator) {
    	        case "+":
    	        case "-":
    	            return 1;
    	        case "x":
    	        case "÷":
    	            return 2;
    	        default:
    	            return 0; // 기본값
    	    }
    	}

    	// 배열을 업데이트하는 메소드
    	private void updateArrays(String[] num, String[] op, int index, double calResult) {
    	    num[index + 1] = Double.toString(calResult);

    	    // 사용된 연산자와 그 다음 인덱스 삭제
    	    for (int i = index; i < op.length - 1; i++) {
    	        op[i] = op[i + 1];
    	    }
    	    op[op.length - 1] = null;
    	}*/
    
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < rows * columns; i++) {
            if (e.getSource() == btn[i]) {
                // 연속된 연산을 수행할 경우 이전 결과와 연산자 저장
                if (continuousOperation) {
                    previousResult = new BigDecimal(result);
                    previousOperator = btnText[i];
                    continuousOperation = false;
                }

                // 새로운 식을 시작하거나 현재 결과가 0일 경우 초기화
                if (result.equals("0") || isNewExpression) {
                    result = "";
                    isNewExpression = false;
                }
                
                // 연산 수행 결과가 "Error"인 경우, 오류 표시 후 종료
                if (result.equals("÷")) {
                    isError = true;
                    label.setText("Error");
                    return;
                }
                
                /*// 연산 수행 결과가 "Error"인 경우, 오류 표시 후 종료
                if (result.equals("÷")) {
                	if (i == 8 || i == 9 || i == 10 || i == 12 || i == 13 || i == 14 || i == 16 || i == 17 || i == 18 || i == 21) {
                    // 0으로 나누는 경우 오류 처리
                	double currentOperand = Double.parseDouble(result);
					if (currentOperand != 0) {
                        result = Double.toString(0 / currentOperand);
                    } else {
                        // 0으로 나누는 경우 오류 처리
                        result = "오류";
                        isError = true;
                    	}
                	 }
                  }*/
                
                // 등호(=)를 누를 때 연산 수행
                if (i == 23) {
                 /*try {*/
                    if (result.matches("\\d+([+]|[-]|[x]|[÷])\\d+")) {
                        Calc();
                        continuousOperation = true; // 연속된 연산을 허용
                        isNewExpression = false; // 새로운 표현식으로 초기화
                        label.setText(result);  // 결과를 표시
                        historyLabel.setText(result); // 위에 작게 결과 표시
                    } else if (result.matches(".*[+\\-x÷].*")) {
                        Calc();
                        continuousOperation = true; // 연속된 연산을 허용
                        isNewExpression = false; // 새로운 표현식으로 초기화
                        label.setText(result); // 결과를 표시
                        historyLabel.setText(result); // 위에 작게 결과 표시
                    } else if (result.matches("(?<=\\d)(?=[+\\-*/])|(?<=[+\\-*/])(?=\\d)")) {
                        Calc();
                        continuousOperation = true; // 연속된 연산을 허용
                        isNewExpression = false; // 새로운 표현식으로 초기화
                        label.setText(result); // 결과를 표시
                        historyLabel.setText(result); // 위에 작게 결과 표시
                    } else {
                        // 여기서도 Calc() 메소드 호출 추가
                        Calc();
                        label.setText(result); // 결과를 표시
                        historyLabel.setText(result); // 위에 작게 결과 표시
                 
                        /*}catch (ArithmeticException ex) {
                        // 0으로 나누거나 정수를 0으로 나눌 때의 예외 처리
                        result = "오류";
                        isError = true;
                        label.setText(result);
                        historyLabel.setText(result);*/
                        
                    }     
                } else if (i == 0) { // 루트 계산
                    if (!result.isEmpty()) {
                        double value = Double.parseDouble(result);
                        value = Math.sqrt(value);
                        result = String.valueOf(value);
                    }
                } else if (i == 1) { // 원주율(π) 추가
                    result += String.valueOf(Math.PI);
                } else if (i == 2) { // 백스페이스 기능
                    if (!result.equals("0") && result.length() > 1) {
                        result = result.substring(0, result.length() - 1);
                    } else {
                        result = "0";
                    }
                } else if (i == 3) { // 초기화(Clear) 기능
                    result = "0";
                } else if (i == 4 || i == 5 || i == 6 || i == 22) { // 삼각함수 및 퍼센트 계산
                    double value = Double.parseDouble(result);
                    if (i == 4) {
                        result = String.valueOf(Math.sin(value));
                    } else if (i == 5) {
                        result = String.valueOf(Math.cos(value));
                    } else if (i == 6) {
                        result = String.valueOf(Math.tan(value));
                    } else if (i == 22) {
                        result = String.valueOf(value / 100);
                    }
                } else if (i == 7 || i == 11 || i == 15 || i == 19) { // 숫자 및 기본 연산자 입력
                        if (result.length() > 0 && Character.isDigit(result.charAt(result.length() - 1))) {
                            result += btnText[i];
                       
                            /*// 현재 버튼이 연산자이고, 이전 연산자가 존재하면
                        if (isOperatorButton(i) && !previousOperator.isEmpty()) {
                            // 2번째로 눌린 연산자로 교체
                            previousOperator = btnText[i];
                            // 이미 입력된 연산자 삭제
                            result = result.substring(0, result.length() - 1);
                        } else {
                            // 이전 연산자 갱신
                            previousOperator = btnText[i];
                        }

                        result += btnText[i];*/
                            
                    	}
                                 
                } else if (i == 20) { // 소수점 입력
                    if (!decimalEntered) {
                        result += ".";
                        decimalEntered = true;
                    }
                } else if (i == 24) { // 로그 계산
                    double value = Double.parseDouble(result);
                    result = String.valueOf(Math.log10(value));
                } else if (i == 25) { // 제곱 계산
                    double value = Double.parseDouble(result);
                    result = String.valueOf(value * value);
                } else if (i == 26 || i == 27) { // 단위 변환 - inch to cm, cm to inch
                    double value = Double.parseDouble(result);
                    if (i == 26) {
                        result = String.valueOf(value * 2.54);
                    } else if (i == 27) {
                        result = String.valueOf(value / 2.54);
                    }
                } else {
                    // 그 외의 버튼은 숫자나 연산자를 그대로 추가
                    result += btnText[i];
                }
            }
        }
        label.setText(result);
        historyLabel.setText(result);
}  


// 정보 다이얼로그를 표시
private void showInformationDialog() {
		// 표시할 정보 내용입니다.
		String information = "반: 2-A\n학번: 2022103062\n이름: 이정민\n2023-12-05";
		
		// 정보 다이얼로그를 표시합니다.
		JOptionPane.showMessageDialog(frame, information, "정보", JOptionPane.INFORMATION_MESSAGE);
		/*JOptionPane.showMessageDialog(frame, information, "정보", JOptionPane.PLAIN_MESSAGE);*/
	}
    
// 테마 변경 이벤트 처리를 위한 내부 클래스
class ThemeChangeListener implements ItemListener {
	@Override
	public void itemStateChanged(ItemEvent e) {
		for (int i = 0; i < rows * columns; i++) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				//블루
                if (e.getItem() == BlueButton) {
                	if (i >= 0 && i <= 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23) {
                		 btn[i].setBackground(new Color(6, 49, 70)); 
                	        btn[i].setForeground(new Color (233, 245, 251));
                	    } else if (i >= 4 && i <= 6 || i >= 24 && i <= 27) {
                	        btn[i].setBackground(new Color(30, 126, 174)); 
                	        btn[i].setForeground(new Color (233, 245, 251)); 
                	    } else {
                	        btn[i].setBackground(new Color(173, 216, 230)); 
                	        btn[i].setForeground(new Color (6, 49, 70)); 
                	        tPanel.setBackground(new Color(240, 248, 255)); 
                	        label.setForeground(new Color(70, 130, 180)); 
                	        historyLabel.setForeground(new Color(70, 130, 180)); 
                	    }
                	    btn[i].setBorder(BorderFactory.createLineBorder(new Color(240, 248, 255))); 
                } 
                //핑크
                else if (e.getItem() == PinkButton) {
                	if (i >= 0 && i <= 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23) {
                		btn[i].setBackground(new Color(219, 112, 147)); 
                		btn[i].setForeground(new Color(128, 0, 32));
                    } else if (i >= 4 && i <= 6 || i >= 24 && i <= 27) {
                    	 btn[i].setBackground(new Color(230, 126, 141));
                         btn[i].setForeground(new Color(255, 228, 225));
                    } else {
                    	 btn[i].setBackground(new Color(252, 190, 200)); 
                    	 btn[i].setForeground(new Color(128, 0, 32));
                         tPanel.setBackground(new Color(255, 228, 225)); 
                         label.setForeground(new Color(128, 0, 32));
                         historyLabel.setForeground(new Color(128, 0, 32));
                    }
                    btn[i].setBorder(BorderFactory.createLineBorder(new Color(255, 228, 225)));
                }
                //그레이
                else if (e.getItem() == GreenButton) {
                	if (i >= 0 && i <= 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23) {
                		 btn[i].setBackground(new Color(46, 60, 42)); 
                	        btn[i].setForeground(new Color(240, 245, 239)); 
                	    } else if (i >= 4 && i <= 6 || i >= 24 && i <= 27) {
                	        btn[i].setBackground(new Color(69, 90, 63));
                	        btn[i].setForeground(new Color(240, 245, 239));
                	    } else {
                	        btn[i].setBackground(new Color(211, 225, 208));
                	        btn[i].setForeground(new Color(46, 60, 42));
                	        tPanel.setBackground(new Color(240, 245, 239));
                	        label.setForeground(new Color(32, 47, 29));
                	        historyLabel.setForeground(new Color(32, 47, 29));
                	    }
                	    btn[i].setBorder(BorderFactory.createLineBorder(new Color(240, 245, 239)));
                	}
                //베이지
                else if (e.getItem() == BeigeButton) {
                	if (i >= 0 && i <= 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23) {
                		btn[i].setBackground(new Color(140, 75, 25));
                        btn[i].setForeground(new Color(245, 245, 235));
                    } else if (i >= 4 && i <= 6 || i >= 24 && i <= 27) {
                    	btn[i].setBackground(new Color(210, 180, 145));
                        btn[i].setForeground(new Color(140, 75, 25));
                    } else {
                    	 btn[i].setBackground(new Color(222, 184, 135)); 
                         btn[i].setForeground(new Color(77, 36, 40));
                         tPanel.setBackground(new Color(245, 245, 235)); 
                         label.setForeground(new Color(77, 36, 40)); 
                         historyLabel.setForeground(new Color(77, 36, 40));
                	}
                    btn[i].setBorder(BorderFactory.createLineBorder(new Color(245, 245, 235)));
                	}
                }
        	}
        }
    }
}

