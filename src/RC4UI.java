import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RC4UI extends Application {
    final int len = 256;
  
    public char[] sbox;
    public char[] key;//1~256位均可
    public char[] tempSbox;
    public char[] tempKey;//256位密钥


    public static void main(String[] args) {
        launch(args);
    }


    public void start(Stage primaryStage) {
        sbox = new char[len];
        tempSbox = new char[len];
        key = new char[len];//1~256位均可
        tempKey = new char[len];//256位密钥
     
        generateRandomKey();

        GridPane pane = new GridPane();
        Scene scene = new Scene(pane, 750, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("RC4加密/解密工具");

        Label leftLabel = new Label("要加/解密的内容:");
        TextArea leftText = new TextArea();
        Label rightLabel = new Label("处理后的信息:");
        TextArea rightText = new TextArea();
        Button encBtn = new Button();
        //Button decBtn=new Button();
//        encBtn.setText("\n加密\n或\n解密\n");
//        encBtn.setMinSize(50, 70);
        encBtn.setText("加密\n解密");
        //decBtn.setText("解密");
        encBtn.setMinSize(50, 70);
        //decBtn.setMinSize(50, 20);

        Button leftPaste = new Button();
        leftPaste.setText("粘贴到此处");
        leftPaste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                leftText.clear();
                leftText.paste();
            }
        });
        Button leftCopy = new Button();
        leftCopy.setText("复制此处内容");
        leftCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                leftText.selectAll();
                leftText.copy();
            }
        });

        Button rightPaste = new Button();
        rightPaste.setText("粘贴到此处");
        rightPaste.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rightText.clear();
                rightText.paste();
            }
        });
        Button rightCopy = new Button();
        rightCopy.setText("复制此处内容");
        rightCopy.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                rightText.selectAll();
                rightText.copy();
            }
        });

        GridPane leftPane = new GridPane();
        leftPane.setHgap(20);
        GridPane leftBottomPane = new GridPane();
        leftPane.setPadding(new Insets(20, 10, 20, 5));
        leftBottomPane.setHgap(20);
        leftBottomPane.add(leftPaste, 0, 0);
        leftBottomPane.add(leftCopy, 1, 0);
        leftPane.add(leftLabel, 0, 0);
        leftPane.add(leftText, 0, 1);
        leftPane.add(leftBottomPane, 0, 2);


        GridPane centerPane = new GridPane();
        centerPane.setPadding(new Insets(30, 5, 30, 5));
        centerPane.setVgap(20);


        centerPane.add(encBtn, 0, 0);
        //centerPane.add(decBtn, 0, 1);


        GridPane rightPane = new GridPane();
        rightPane.setHgap(20);
        rightPane.setPadding(new Insets(20, 5, 20, 10));
        GridPane rightBottomPane = new GridPane();
        rightBottomPane.setHgap(20);
        rightBottomPane.add(rightPaste, 0, 0);
        rightBottomPane.add(rightCopy, 1, 0);
        rightPane.add(rightLabel, 0, 0);
        rightPane.add(rightText, 0, 1);
        rightPane.add(rightBottomPane, 0, 2);


        Label keyLabel = new Label("密钥:");
        TextField keyText = new TextField();
        keyText.setText(String.valueOf(key));
        Button keyButton = new Button("生成随机密钥");
        keyText.setPrefWidth(200);
        GridPane cpPane = new GridPane();
        cpPane.setMinWidth(300);
        cpPane.setPadding(new Insets(5, 5, 5, 5));
        cpPane.setHgap(10);
        cpPane.add(keyLabel,0,0);
        cpPane.add(keyText, 1, 0);
        cpPane.add(keyButton,2,0);

        pane.add(cpPane,0,0);
        pane.add(leftPane, 0, 1);
        pane.add(centerPane, 1, 1);
        pane.add(rightPane, 2, 1);

        keyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                generateRandomKey();
                keyText.setText(String.valueOf(key));
            }
        });

        leftBottomPane.setAlignment(Pos.CENTER);
        rightBottomPane.setAlignment(Pos.CENTER);

        encBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                key = keyText.getText().toCharArray();
                //System.out.println(key);
                if(key!=null) {
                generateCustomKey();
                //System.out.println(leftText.getText());
                rightText.setText(crypt(leftText.getText()));
                //rightText.setText(rc4(leftText.getText()));
                }
            }
        });
        
    }
   

    void generateRandomKey() {
    	String WordList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    	int keylen = (int)(Math.random() * 255);//随机获取密钥长度
    	int index=0;
    	key=new char[len];
        //生成密钥
        for (int i = 0; i < keylen; i++) {
        	index = (int)(Math.random() * 62);
            key[i] = WordList.charAt(index);
        }
//    	for (int i = 0; i < key.length; i++) {
//            key[i] = (char) (int) (Math.random() * 1000);
//        }
       
        initSbox();
    }

    void generateCustomKey() {
        initSbox();
    }

    void initSbox() {
        //初始化sbox & 256位的密钥tempKey
        for (int i = 0; i < sbox.length; i++) {
            sbox[i] = (char) i;
            if(key.length!=0) {
            tempKey[i] = key[i % key.length];}
        }
       

        //置换sbox
        int j = 0;
        for (int i = 0; i < sbox.length; i++) {
            j = (sbox[i] + tempKey[i] + j) % len;

            //swap
            char temp = sbox[i];
            sbox[i] = sbox[j];
            sbox[j] = temp;
        }
    
    }

    //加解密过程都是与密钥流作异或
    String crypt(String input) {
        String output = "";

        
        tempSbox = String.valueOf(sbox).toCharArray();

        if (input == "") {
            System.out.println("null input!");
        } else {
        	//生成密钥流
            int i = 0, j = 0;
         
            for (int k = 0; k < input.length(); k++) {
                i = (i + 1) % len;
                j = (j + tempSbox[i]) % len;

                //swap
                char temp = tempSbox[i];
                tempSbox[i] = tempSbox[j];
                tempSbox[j] = temp;

                int tempIndex = (tempSbox[i] + tempSbox[j]) % len;
                //output +=  Integer.toHexString((char) (input.charAt(k) ^ tempSbox[tempIndex]));
                output +=  (char) (input.charAt(k) ^ tempSbox[tempIndex]);
            }
            
        }
//            for (int k = 0; k < srtbyte.length; k++) {
//              i = (i + 1) % len;
//              j = (j + tempSbox[i]) % len;
//
//              //swap
//              char temp = tempSbox[i];
//              tempSbox[i] = tempSbox[j];
//              tempSbox[j] = temp;
//
//              int tempIndex = (tempSbox[i] + tempSbox[j]) % len;
//              output += (char) (srtbyte[k] ^ tempSbox[tempIndex]);
//          }
//        }
        return output;
    }
    
String decry(String input) {
	String input_str="";
	String output="";
	if (input == "") {
        System.out.println("null input!");
    } else {
    	//先将16进制转变成String
    		 StringBuilder sb = new StringBuilder();
    		  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
    		  for( int i=0; i<input.length()-1; i+=2 ){   	 
    		      //grab the hex in pairs
    		      String sub = input.substring(i, (i + 2));
    		      //convert hex to decimal
    		      int decimal = Integer.parseInt(sub, 16);
    		      //convert the decimal to character
    		      sb.append((char)decimal);
    	 
    		  }
    		  input_str=sb.toString();
    		  int i = 0, j = 0;
           
              for (int k = 0; k < input.length(); k++) {
                  i = (i + 1) % len;
                  j = (j + tempSbox[i]) % len;

                  //swap
                  char temp = tempSbox[i];
                  tempSbox[i] = tempSbox[j];
                  tempSbox[j] = temp;

                  int tempIndex = (tempSbox[i] + tempSbox[j]) % len;
                  output += (char) (input_str.charAt(k) ^ tempSbox[tempIndex]);
              }
 
    	 }
    
	return output;
}
    
}
