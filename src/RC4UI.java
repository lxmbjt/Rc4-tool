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
	 //public char[] key;//1~256浣嶅潎鍙�
	public String key;
	 final int len = 256;
	 public static void main(String[] args) {
	        launch(args);
	    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//key = new char[len];//1~256位均可
		key="";
		generateRandomKey();

        GridPane pane = new GridPane();
        Scene scene = new Scene(pane, 750, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setTitle("RC4加密/解密工具");

        Label leftLabel = new Label("明文：");
        TextArea leftText = new TextArea();
        Label rightLabel = new Label("密文：");
        TextArea rightText = new TextArea();
        Button encBtn = new Button();
        Button decBtn=new Button();
//        encBtn.setText("\n加密\n或\n解密\n");
//        encBtn.setMinSize(50, 70);
        encBtn.setText("加密→");
        decBtn.setText("←解密");
        encBtn.setMinSize(70, 20);
        decBtn.setMinSize(70, 20);

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
        centerPane.add(decBtn, 0, 1);


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
                key = keyText.getText();
                //System.out.println(key);
                if(key!=null) {
                generateCustomKey();
                //String keyy="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxACCESSKEY";
                //System.out.println(leftText.getText());
                //rightText.setText(crypt(leftText.getText()));
                //rightText.setText(rc4(leftText.getText()));
                rightText.setText(encrypt(leftText.getText(),key));
                }
            }
        });
        decBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                key = keyText.getText();
                //System.out.println(key);
                if(key!=null) {
               //String keyy="xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxACCESSKEY";
                //generateCustomKey();
                //System.out.println(leftText.getText());
                //rightText.setText(crypt(leftText.getText()));
                //rightText.setText(rc4(leftText.getText()));
                leftText.setText(decrypt(rightText.getText(),key));
                }
            }
        });
        
    }
   

    void generateRandomKey() {
    	String WordList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    	int keylen = (int)(Math.random() * 255);//随机获取密钥长度
    	int index=0;
    	key="";
    	char[] key_char=new char[len];
        //生成密钥
        for (int i = 0; i < keylen; i++) {
        	index = (int)(Math.random() * 62);
            key_char[i]= WordList.charAt(index);
        }
        key=String.valueOf(key_char);
//    	for (int i = 0; i < key.length; i++) {
//            key[i] = (char) (int) (Math.random() * 1000);
//        }
       
    
    }

    void generateCustomKey() {
     
    }


/** 加密 **/
public static String encrypt(String data, String key) {
    if (data == null || key == null) {
        return null;
    }
    return toHexString(asString(encrypt_byte(data, key)));
}
/** 解密 **/
public static String decrypt(String data, String key) {
    if (data == null || key == null) {
        return null;
    }
    return new String(RC4Base(HexString2Bytes(data), key));
}
/** 加密字节码 **/
public static byte[] encrypt_byte(String data, String key) {
    if (data == null || key == null) {
        return null;
    }
    byte b_data[] = data.getBytes();
    return RC4Base(b_data, key);
}
private static String asString(byte[] buf) {
    StringBuffer strbuf = new StringBuffer(buf.length);
    for (int i = 0; i < buf.length; i++) {
        strbuf.append((char) buf[i]);
    }
    return strbuf.toString();
}
private static byte[] initKey(String aKey) {
    byte[] b_key = aKey.getBytes();
    byte state[] = new byte[256];

    for (int i = 0; i < 256; i++) {
        state[i] = (byte) i;
    }
    int index1 = 0;
    int index2 = 0;
    if (b_key == null || b_key.length == 0) {
        return null;
    }
    for (int i = 0; i < 256; i++) {
        index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
        byte tmp = state[i];
        state[i] = state[index2];
        state[index2] = tmp;
        index1 = (index1 + 1) % b_key.length;
    }
    return state;
}
private static String toHexString(String s) {
    String str = "";
    for (int i = 0; i < s.length(); i++) {
        int ch = (int) s.charAt(i);
        String s4 = Integer.toHexString(ch & 0xFF);
        if (s4.length() == 1) {
            s4 = '0' + s4;
        }
        str = str + s4;
    }
    return str;// 0x表示十六进制
}
private static byte[] HexString2Bytes(String src) {
    int size = src.length();
    byte[] ret = new byte[size / 2];
    byte[] tmp = src.getBytes();
    for (int i = 0; i < size / 2; i++) {
        ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
    }
    return ret;
}
private static byte uniteBytes(byte src0, byte src1) {
    char _b0 = (char) Byte.decode("0x" + new String(new byte[] { src0 })).byteValue();
    _b0 = (char) (_b0 << 4);
    char _b1 = (char) Byte.decode("0x" + new String(new byte[] { src1 })).byteValue();
    byte ret = (byte) (_b0 ^ _b1);
    return ret;
}
private static byte[] RC4Base(byte[] input, String mKkey) {
    int x = 0;
    int y = 0;
    byte key[] = initKey(mKkey);
    int xorIndex;
    byte[] result = new byte[input.length];

    for (int i = 0; i < input.length; i++) {
        x = (x + 1) & 0xff;
        y = ((key[x] & 0xff) + y) & 0xff;
        byte tmp = key[x];
        key[x] = key[y];
        key[y] = tmp;
        xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
        result[i] = (byte) (input[i] ^ key[xorIndex]);
    }
    return result;
}
/**
 * 字符串转换成十六进制字符串
 */
public static String str2HexStr(String str) {
    char[] chars = "0123456789ABCDEF".toCharArray();
    StringBuilder sb = new StringBuilder("");
    byte[] bs = str.getBytes();
    int bit;
    for (int i = 0; i < bs.length; i++) {
        bit = (bs[i] & 0x0f0) >> 4;
        sb.append(chars[bit]);
        bit = bs[i] & 0x0f;
        sb.append(chars[bit]);
    }
    return sb.toString();
}

/**
 * 
 * 十六进制转换字符串
 * 
 * @throws UnsupportedEncodingException
 */

public static String hexStr2Str(String hexStr) {
    String str = "0123456789ABCDEF";
    char[] hexs = hexStr.toCharArray();
    byte[] bytes = new byte[hexStr.length() / 2];
    int n;
    for (int i = 0; i < bytes.length; i++) {
        n = str.indexOf(hexs[2 * i]) * 16;
        n += str.indexOf(hexs[2 * i + 1]);
        bytes[i] = (byte) (n & 0xff);
    }
    return new String(bytes);
}
}
