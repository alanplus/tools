package com.android.tools;

import com.android.tools.tools.AES128Encrypt;

/**
 * Created by Mouse on 2019/1/15.
 */
public class Test {

    public static void main(String[] args) {
//
//        String string = "39dc58d2ff5b95860e6748fc4bf456ec08994be053cae6a04ca2d6eb2afcf4be70608a7e1464f5e85ccef93140f5631129631766b0229ae28c2d86bbd58a5513ea4cf8e83330cfa4996268d37e5355304f031cbff19220c548b8e4b43444a2d3eec409dcf76277e3c0b1e05eafbb4990a5a200ca52d8fa6ed3f7ad41b97fcdd34f9e4ccd2f8268a04483279957f3ec647be5fbf9152b40c5c55d42c7db0913583938f81f2c4c032a04e604b5bb7725ebc1ac7a22b35b69d5b20de6f59a87d6eac68fbe02fb9d7eceb2b106186b7014";
//
//        try {
//            String s = AES128Encrypt.Decrypt(string, "ac14c13680bdf7a0");
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        String string_abc = "know_well=0&is_wifi=1&app_version=300&user_code=15013345678&learn=0&bound_id=00000000-4144-f3ef-0000-0000126c12cf&session=02e873af509543bc9284915e04b711e4&use_day=0&app_id=8&device=0&platform=1";

        try {
            String s = AES128Encrypt.Encrypt(string_abc, "ac14c13680bdf7a0");
//            s = "34831d3743086a752b3c3a1ef9478bd4b21e6f4b13a95ed3a024ae2d7834df3303d6a73ada04accda4187f5432782eece7582744d683524ba24c5ba493cdc8590d2e839c2a2c85ab250a9a30a86f326f20142a672e8cb038135174ce3a04d6ccb97397dfe2e4d9b4847071f83934b1e4633aa9aa8258193156c984df7aa93b7e42852e99054a924e61c60795fc28af8a457d22fea239f5e8425be0d5ac6b1ff1b311e91ee017fa348b9d8b049ed4bccff3214d13b9a51637bdc4abdea13cc576a9f8369c7a7ce861962dd77a6a258918";
            System.out.println(s);
            System.out.println(s.length());
            String s2 = AES128Encrypt.Decrypt(s, "ac14c13680bdf7a0");
            System.out.println(s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testLeci() {

        String string = "中国人abc";
//        LCHMacUtils.


    }
}
