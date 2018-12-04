package iQRGenuine.util;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;


/**
 * 封装同RSA非对称加密算法有关的方法，可用于数字签名，RSA加密解密
 */

public class RSATool
{
    private static KeyFactory _keyFactory;
    private static KeyPairGenerator _keyPairGenerator;
    private static Signature _signature;
    private static Base64.Encoder _base64Encoder = Base64.getEncoder();
    private static Base64.Decoder _base64Decoder = Base64.getDecoder();

    static
    {
        try
        {
            _keyFactory = KeyFactory.getInstance("RSA");
            _signature = Signature.getInstance("SHA1withRSA");
            _keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(System.currentTimeMillis());
            _keyPairGenerator.initialize(1024, random);
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
    }


    public static String base64Encode(byte[] bytes)
    {
        return _base64Encoder.encodeToString(bytes);
    }

    public static byte[] base64Decode(String str)
    {
        return _base64Decoder.decode(str);
    }

    /**
     * 建立新的密钥对，返回打包的byte[]形式私钥和公钥
     *
     * @return 包含打包成byte[]形式的私钥和公钥的object[], 其中，object[0]为私钥byte[],object[1]为公钥byte[]
     */
    public static byte[][] getKeyPairBytes()
    {
        KeyPair newKeyPair = _keyPairGenerator.generateKeyPair();
        //byte[] b_prv = newKeyPair.getPrivate().getEncoded();
        //byte[] b_pub = newKeyPair.getPublic().getEncoded();
        byte[] b_pub = newKeyPair.getPrivate().getEncoded();
        byte[] b_prv = newKeyPair.getPublic().getEncoded();
        byte[][] re = new byte[2][];
        re[0] = b_prv;
        re[1] = b_pub;
        return re;
    }

    /**
     * RSA私钥加密
     *
     * @param plain_text    明文
     * @param privKeyInByte 密钥
     * @return 加密数据
     */
    public static byte[] encrypt(byte[] privKeyInByte, byte[] plain_text) throws Exception
    {
        //PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(privKeyInByte);
        //PrivateKey privKey = _keyFactory.generatePrivate(priv_spec);
        X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(privKeyInByte);
        PublicKey privKey = _keyFactory.generatePublic(pub_spec);
        Cipher cipher = Cipher.getInstance(_keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privKey);
        return cipher.doFinal(plain_text);
    }

    /**
     * RSA公钥解密
     *
     * @param cipher_text  密文
     * @param pubKeyInByte 公钥
     * @return 解密数据
     */
    public static byte[] decrypt(byte[] pubKeyInByte, byte[] cipher_text) throws Exception
    {
        //X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(pubKeyInByte);
        //PublicKey pubKey = _keyFactory.generatePublic(pub_spec);
        PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(pubKeyInByte);
        PrivateKey pubKey = _keyFactory.generatePrivate(priv_spec);
        Cipher cipher = Cipher.getInstance(_keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, pubKey);
        return cipher.doFinal(cipher_text);
    }


    /**
     * 使用私钥加密数据
     * 用一个已打包成byte[]形式的私钥加密数据，即数字签名
     *
     * @param keyInByte 打包成byte[]的私钥
     * @param source    要签名的数据，一般应是数字摘要
     * @return 签名 byte[]
     */
    public static byte[] sign(byte[] keyInByte, byte[] source) throws Exception
    {
        PKCS8EncodedKeySpec priv_spec = new PKCS8EncodedKeySpec(keyInByte);
        PrivateKey privKey = _keyFactory.generatePrivate(priv_spec);
        _signature.initSign(privKey);
        _signature.update(source);
        return _signature.sign();
    }

    /**
     * 验证数字签名
     *
     * @param keyInByte 打包成byte[]形式的公钥
     * @param source    原文的数字摘要
     * @param sign      签名（对原文的数字摘要的签名）
     * @return 是否证实 boolean
     */
    public static boolean verify(byte[] keyInByte, byte[] source, byte[] sign) throws Exception
    {
        X509EncodedKeySpec pub_spec = new X509EncodedKeySpec(keyInByte);
        PublicKey pubKey = _keyFactory.generatePublic(pub_spec);
        _signature.initVerify(pubKey);
        _signature.update(source);
        return _signature.verify(sign);
    }
}
