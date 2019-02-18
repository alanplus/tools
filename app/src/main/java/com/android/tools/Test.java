package com.android.tools;

import com.android.tools.tools.AES128Encrypt;

/**
 * Created by Mouse on 2019/1/15.
 */
public class Test {

    public static void main(String[] args) {

        String string = "59c85825c2f52aad94c339ec094d56141be97098fe53f41c16975a1df0eac38426755f035093119908b768b2b2fbe34e79b6b0a2dfe74bb7281199904e32e7c6d03d53f3bd63cb38acf42199b8f548a5184808aaa60476314d37a8e857cfc6853e45390041a7eaa234b0250122ac7b5092e6b91165dec9ddeef8071e2d1972f50c3b14bbf4651329c7dee19a53775bcac24fe54c87351961551d192812649c787d3a35de8de5bc833b1ff420b787d694d1f4068eb1215efea8127deb33ecc342bac7a4f323c19304b6e966ecfbfc134be5647c97380c63d66c869c9f4b1546d78ce143e2ed9d8014d6213db95492bbda79c4950f2c27c0f84cf2bde32a88497c10aefcf07b7251165f9a75800e31728d5f4503ffdcb20c98a187bf395ad0a72afd131c3333bd3462aa51c5291d289e580cf8a7f7bf7d2ae2c33c0b4502f928f4";

        try {
            String s = AES128Encrypt.Decrypt(string, "ac14c13680bdf7a0");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //        try {
//            String s = AES128Encrypt.Encrypt(string, "ac14c13680bdf7a0");
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void testLeci(){

        String string = "中国人abc";
//        LCHMacUtils.


    }
}
