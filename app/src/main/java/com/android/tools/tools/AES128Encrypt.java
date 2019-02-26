package com.android.tools.tools;




import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class AES128Encrypt {
	
	
    
    /**  
     * 加密 
     * @return 加密后的字符串
     */    
    public static String Encrypt(String src, String key) throws Exception {  
        // 判断密钥是否为空  
        if (key == null) {  
            System.out.print("密钥不能为空");  
            return null;  
        }  
          
        // 密钥补位  
        //int plus= 16-key.length();          
        byte[] data = key.getBytes("utf-8");  
        byte[] raw = new byte[16];  
       // byte[] plusbyte={ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};  
        for(int i=0;i<16;i++)  
        {  
            if (data.length > i)  
                raw[i] = data[i];  
            else  
                raw[i] =0x00;  
        }  
          
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding"); // 算法/模式/补码方式   
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);  
        byte[] encrypted = cipher.doFinal(src.getBytes("utf-8"));   
        return parseByte2HexStr(encrypted);
    }  
  
    /**  
     * 解密 
     * @return 解密后的字符串
     */    
    public static String Decrypt(String src, String key) throws Exception {  
        try {  
            // 判断Key是否正确  
            if (key == null) {  
                System.out.print("Key为空null");  
                return null;  
            }  
  
            // 密钥补位  
           // int plus= 16-key.length();              
            byte[] data = key.getBytes("utf-8");  
            byte[] raw = new byte[16];  
           // byte[] plusbyte={ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};  
            for(int i=0;i<16;i++)  
            {  
                if (data.length > i)  
                    raw[i] = data[i];  
                else  
                    raw[i] = 0x00;  
            }  
              
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");  
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");  
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);                
            
            byte[] encrypted1 = toByteArray(src);//十六进制             
            try {  
                byte[] original = cipher.doFinal(encrypted1);  
                String originalString = new String(original,"utf-8");  
                return originalString;  
            } catch (Exception e) {  
                System.out.println(e.toString());  
                return null;  
            }  
        } catch (Exception ex) {  
            System.out.println(ex.toString());  
            return null;  
        }  
    }  
      
    /**
     * 将二进制转换成16进制
     * @method parseByte2HexStr
     * @param buf
     * @return
     * @throws 
     * @since v1.0
     */
    public static String parseByte2HexStr(byte buf[]){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < buf.length; i++){
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toLowerCase());
        }
        return sb.toString();
    }
    
        
    /** 
     * 16进制的字符串表示转成字节数组 
     * 
     * @param hexString 16进制格式的字符串             
     * @return 转换后的字节数组 
     **/  
    public static byte[] toByteArray(String hexString) {  
        if (hexString.isEmpty())  
            throw new IllegalArgumentException("this hexString must not be empty");  
  
        hexString = hexString.toLowerCase();  
        final byte[] byteArray = new byte[hexString.length() / 2];  
        int k = 0;  
        for (int i = 0; i < byteArray.length; i++) {//因为是16进制，最多只会占用4位，转换成字节需要两个16进制的字符，高位在先  
            byte high = (byte) (Character.digit(hexString.charAt(k), 16) & 0xff);  
            byte low = (byte) (Character.digit(hexString.charAt(k + 1), 16) & 0xff);  
            byteArray[i] = (byte) (high << 4 | low);  
            k += 2;  
        }  
        return byteArray;  
    } 
 
    
    /**
     * 将16进制转换为二进制
     * @method parseHexStr2Byte
     * @param hexStr
     * @return
     * @throws 
     * @since v1.0
     */
    public static byte[] parseHexStr2Byte(String hexStr){
        if(hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length()/2];
        for (int i = 0;i< hexStr.length()/2; i++) {
            int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
            int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }	
}
