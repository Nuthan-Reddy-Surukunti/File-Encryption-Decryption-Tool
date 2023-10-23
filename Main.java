import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println("Usage: java FileEncryptionDecryption <mode> <input_file> <output_file> <encryption_key>");
            System.out.println("<mode> should be 'encrypt' or 'decrypt'");
            return;
        }

        String mode = args[0];
        String inputFile = args[1];
        String outputFile = args[2];
        String encryptionKey = args[3];

        try {
            SecretKey secretKey = generateSecretKey(encryptionKey);

            if ("encrypt".equalsIgnoreCase(mode)) {
                encryptFile(inputFile, outputFile, secretKey);
                System.out.println("File encrypted successfully.");
            } else if ("decrypt".equalsIgnoreCase(mode)) {
                decryptFile(inputFile, outputFile, secretKey);
                System.out.println("File decrypted successfully.");
            } else {
                System.out.println("Invalid mode. Use 'encrypt' or 'decrypt'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SecretKey generateSecretKey(String key) throws NoSuchAlgorithmException {
        // You can use other key generation methods as needed
        byte[] keyBytes = key.getBytes();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private static void encryptFile(String inputFile, String outputFile, SecretKey secretKey)
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, cipher);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            cipherOutputStream.write(buffer, 0, bytesRead);
        }

        cipherOutputStream.close();
        outputStream.close();
        inputStream.close();
    }

    private static void decryptFile(String inputFile, String outputFile, SecretKey secretKey)
            throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        CipherInputStream cipherInputStream = new CipherInputStream(inputStream, cipher);

        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = cipherInputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }

        outputStream.close();
        cipherInputStream.close();
        inputStream.close();
    }
}

