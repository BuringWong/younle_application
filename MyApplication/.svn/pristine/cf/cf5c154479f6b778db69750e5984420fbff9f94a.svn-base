package com.younle.younle624.myapplication.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2016/11/30.
 */
public class KeyUtils {
    /**
     * 获取签名公钥
     * @param mContext
     * @return
     */
    public static String getSignInfo(Context mContext) {
        String signcode = "";
        try {
//            mContext.getPackageManager().getPackageInfo(mContext.getPackageName())
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    mContext.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            signcode = parseSignature(sign.toByteArray());
            signcode = signcode.toLowerCase();
        } catch (Exception e) {
        }
        return signcode;
    }

    protected static String parseSignature(byte[] signature) {
        String sign = "";
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String ss = subString(pubKey);
            ss = ss.replace(",", "");
            ss = ss.toLowerCase();
            int aa = ss.indexOf("modulus");
            int bb = ss.indexOf("publicexponent");
            sign = ss.substring(aa + 8, bb);
        } catch (CertificateException e) {
            LogUtils.Log("e=="+e.toString());
        }
        return sign;
    }

    public static String subString(String sub) {
        Pattern pp = Pattern.compile("\\s*|\t|\r|\n");
        Matcher mm = pp.matcher(sub);
        return mm.replaceAll("");
    }
    /*public static String getPublicKey(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String publickey = cert.getPublicKey().toString();
            //start,end
            publickey = publickey.substring(publickey.indexOf("modulus=") + 9,publickey.indexOf(',', publickey.indexOf("modulus=")));
            LogUtils.Log("publickey==" + publickey);
            return publickey;
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] getSign(Context context) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> apps = pm
                .getInstalledPackages(PackageManager.GET_SIGNATURES);
        Iterator<PackageInfo> iter = apps.iterator();

        while (iter.hasNext()) {
            PackageInfo info = iter.next();
            String packageName = info.packageName;
            //按包名 取签名
            if (packageName.equals("com.younle.younle624.myapplication")) {
                return info.signatures[0].toByteArray();
            }
        }
        return null;
    }
    *//**
     * 获取签名公钥
     * @param mContext
     * @return
     *//*
    protected static String getSignInfo(Context mContext) {
    *//*    String signcode = "";
        try {
            PackageInfo packageInfo = mContext.getPackageManager().getPackageInfo(
                    GetAppInfo.getPackageName(mContext), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];

            signcode = parseSignature(sign.toByteArray());
            signcode = signcode.toLowerCase();
        } catch (Exception e) {
        }
        return signcode;*//*
        return  null;
    }

    public static String parseSignature(byte[] signature) {
        String sign = "";
        try {
            CertificateFactory certFactory = CertificateFactory
                    .getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory
                    .generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();

            LogUtils.Log("pubkey==" + pubKey);


//            String ss = subString(pubKey);
            *//*ss = ss.replace(",", "");
            ss = ss.toLowerCase();
            int aa = ss.indexOf("modulus");
            int bb = ss.indexOf("publicexponent");
            sign = ss.substring(aa + 8, bb);*//*
        } catch (CertificateException e) {
        }
        return sign;
    }

    public static String subString(Context context) {
        *//*Pattern pp = Pattern.compile("\\s*|\t|\r|\n");
        Matcher mm = pp.matcher(sub);
        return mm.replaceAll("");*//*
        try {
            AssetManager.AssetInputStream fis = (AssetManager.AssetInputStream) context.getAssets().open("letuiweiposkey.jks");
            KeyStore jks = KeyStore.getInstance("JKS");
            char[] kspwd = "younle0901".toCharArray(); // 证书库密码
            char[] keypwd = "younle0901".toCharArray(); // 证书密码
            jks.load(fis, kspwd); // 加载证书
            PrivateKey privateKey = (PrivateKey) jks.getKey("possignkey", keypwd); // 获取证书私钥
            PublicKey publicKey = jks.getCertificate("possignkey").getPublicKey();// 获取证书公钥
            fis.close();
            LogUtils.Log("publickenn=="+publicKey.toString());

        } catch (Exception e) {
            LogUtils.Log("E:"+e.toString());
            e.printStackTrace();
        }
        return null;
    }
    public static  void getKey1(){
        try {
            // 用证书的私钥解密 - 该私钥存在生成该证书的密钥库中
            FileInputStream fis2 = new FileInputStream("G:\\shanhytest.keystore");
            KeyStore ks = KeyStore.getInstance("JKS"); // 加载证书库
            char[] kspwd = "shanhytest".toCharArray(); // 证书库密码
            char[] keypwd = "shanhytest".toCharArray(); // 证书密码
            String alias = "shanhytest";// 别名
            ks.load(fis2, kspwd); // 加载证书
            PrivateKey privateKey = (PrivateKey) ks.getKey(alias, keypwd); // 获取证书私钥
            PublicKey publicKey = ks.getCertificate(alias).getPublicKey();// 获取证书公钥
            fis2.close();
*//*
            System.out.println("privateKey = " + getKeyString(privateKey));
            System.out.println("publicKey  = " + getKeyString(publicKey));*//*

            // 测试加密解密字符串
            String srcContent = "今天天气不错。";

            // 将字符串使用公钥加密后，再用私钥解密后，验证是否能正常还原。
            // 因为非对称加密算法适合对小数据量的数据进行加密和解密，而且性能比较差，所以在实际的操作过程中，我们通常采用的方式是：采用非对称加密算法管理对称算法的密钥，然后用对称加密算法加密数据，这样我们就集成了两类加密算法的优点，既实现了加密速度快的优点，又实现了安全方便管理密钥的优点。
           *//* byte[] d1 = crypt(publicKey, srcContent.getBytes(), Cipher.ENCRYPT_MODE);
            byte[] d2 = crypt(privateKey, d1, Cipher.DECRYPT_MODE);*//*
//            System.out.println(new String(d2));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}
