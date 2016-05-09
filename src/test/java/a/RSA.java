package a;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.security.crypto.codec.Hex;

public class RSA {

    private static final String hexBytesString = "0123456789abcdef";

    public static void main(String[] args)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        Random random = new Random(0);
        BigInteger a = new BigInteger(1024, random);
        BigInteger b = new BigInteger(1024, random);

        a = nextPrime(a);
        b = nextPrime(b);
        System.out.println(a);
        System.out.println(b);
        System.out.println(a.multiply(b));
        System.out.println(a.isProbablePrime(Integer.MAX_VALUE));
        System.out.println(b.isProbablePrime(Integer.MAX_VALUE));
        System.out.println(a.multiply(b).isProbablePrime(Integer.MAX_VALUE));

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        KeyPair genKeyPair = kpg.genKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) genKeyPair.getPublic();
        System.out.println(publicKey);
        RSAPrivateCrtKey privateKey = (RSAPrivateCrtKey) genKeyPair.getPrivate();
        System.out.println(privateKey.getPublicExponent());

        StringBuilder sb = new StringBuilder(10000 * 4);

        for (int i = 0; i < 10000; i++) {
            sb.append(i);
        }
        Cipher rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.DECRYPT_MODE, publicKey);
        String hexBytes1 = hexBytes(rsa.doFinal(sb.toString().getBytes()));
        System.out.println(hexBytes1);
    }

    private static BigInteger nextPrime(BigInteger a) {
        BigInteger tmp = a;
        while (!tmp.isProbablePrime(Integer.MAX_VALUE)) {
            tmp = tmp.nextProbablePrime();
        }
        return tmp;
    }

    private static String hexBytes(byte[] bytes) {
        return new String(Hex.encode(bytes));
    }

}
