package iQRGenuine.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;


/**
 * 封装同RSA非对称加密算法有关的方法，可用于数字签名，RSA加密解密
 */

public class RSATool2
{
    private static Base64.Encoder _base64Encoder = Base64.getEncoder();
    private static Base64.Decoder _base64Decoder = Base64.getDecoder();
    public static String base64Encode(byte[] bytes)
    {
        return _base64Encoder.encodeToString(bytes);
    }

    public static byte[] base64Decode(String str)
    {
        return _base64Decoder.decode(str);
    }

    private KeyFactory _keyFactory;
    private KeyPairGenerator _keyPairGenerator;
    private PublicKey publicKey;
    private PrivateKey privateKey;

    public RSATool2()
    {
        try
        {
            _keyFactory = KeyFactory.getInstance("RSA");
            _keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            random.setSeed(System.currentTimeMillis());
            _keyPairGenerator.initialize(2048, random);
            this.generateNewKey();
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 获取公钥的Base64字符串
     *
     * @return 公钥的Base64字符串
     */
    public String getPublicKey()
    {
        return base64Encode(publicKey.getEncoded());
    }

    /**
     * 获取私钥的Base64字符串
     *
     * @return 私钥的Base64字符串
     */
    public String getPrivateKey()
    {

        return base64Encode(privateKey.getEncoded());
    }

    public void generateNewKey()
    {
        KeyPair newKeyPair = _keyPairGenerator.generateKeyPair();
        publicKey=newKeyPair.getPublic();
        privateKey=newKeyPair.getPrivate();
    }

    /**
     * RSA加密
     *
     * @param plain_text 明文
     * @return base64格式加密数据
     */
    public String encrypt(String plain_text) throws Exception
    {
        Cipher cipher = Cipher.getInstance(_keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] plain_bytes = plain_text.getBytes(StandardCharsets.UTF_8);
        byte[] cipher_bytes = cipher.doFinal(plain_bytes);
        return base64Encode(cipher_bytes);
    }

    /**
     * RSA解密
     *
     * @param cipher_text 密文
     * @return 解密数据
     */
    public String decrypt(String cipher_text) throws Exception
    {
        Cipher cipher = Cipher.getInstance(_keyFactory.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] cipher_bytes = cipher_text.getBytes(StandardCharsets.UTF_8);
        byte[] plain_bytes = cipher.doFinal(cipher_bytes);
        return base64Encode(plain_bytes);
    }
}
