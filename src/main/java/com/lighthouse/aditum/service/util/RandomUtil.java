package com.lighthouse.aditum.service.util;

import com.google.api.client.util.Base64;
import com.lighthouse.aditum.domain.BitacoraAcciones;
import com.lighthouse.aditum.service.BitacoraAccionesService;
import com.lighthouse.aditum.service.dto.BitacoraAccionesDTO;
import io.grpc.netty.shaded.io.netty.util.internal.logging.AbstractInternalLogger;
import org.apache.commons.lang3.RandomStringUtils;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;

import static com.lowagie.text.xml.xmp.XmpWriter.UTF8;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;
    private static SecretKeySpec secretKey;
    private static byte[] key;

    private RandomUtil() {

    }

    /**
     * Generate a password.
     *
     * @return the generated password
     */
    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(DEF_COUNT);
    }

    /**
     * Generate an activation key.
     *
     * @return the generated activation key
     */
    public static String generateActivationKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }

    /**
     * Generate a reset key.
     *
     * @return the generated reset key
     */
    public static String generateResetKey() {
        return RandomStringUtils.randomNumeric(DEF_COUNT);
    }


    public static String formatMoneyString(String currency, String text) {
        double ammount = Double.parseDouble(text);
        return formatMoney(currency, ammount);
    }

    public static BitacoraAccionesDTO createBitacoraAcciones(String concept, int type, String urlState, String category, Long idReference, Long companyId, Long houseId) {
        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        BitacoraAccionesDTO bitacoraAccionesDTO = new BitacoraAccionesDTO();

        bitacoraAccionesDTO.setConcept(concept);
        bitacoraAccionesDTO.setType(type);
        bitacoraAccionesDTO.setUrlState(urlState);
        bitacoraAccionesDTO.setEjecutionDate(zonedDateTime);
        bitacoraAccionesDTO.setCategory(category);
        bitacoraAccionesDTO.setIdReference(idReference);
        bitacoraAccionesDTO.setCompanyId(companyId);
        bitacoraAccionesDTO.setHouseId(houseId);
        return bitacoraAccionesDTO;
    }


    public static String formatMoney(String currency, double ammount) {
        String formatedMoney = "";
        NumberFormat currencyFormatter = null;
        Locale locale = null;
        switch (currency) {
            case "₡":
                locale = new Locale("es", "CR");
                currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                if (ammount == 0) {
                    formatedMoney = "0.0";
                } else {
                    if (ammount < 0) {
                        String formatted = currencyFormatter.format(ammount).substring(2);
                        formatedMoney = " - " + formatted.substring(0, formatted.length() - 4).replace(",", ".");
                    } else {
                        String formatted = currencyFormatter.format(ammount).substring(1);
                        formatedMoney = formatted;
                    }
                }
                break;
            case "$":
                locale = new Locale("en", "US");
                currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                if (ammount == 0) {
                    formatedMoney = "$ 0.0";
                } else {
                    if (ammount < 0) {
                        String formatted = currencyFormatter.format(ammount).substring(2);
                        formatedMoney = "- $ " + formatted.substring(0, formatted.length() - 1);
                    } else {
                        String formatted = currencyFormatter.format(ammount).substring(1);
                        formatedMoney = "$ " + formatted;
                    }
                }
                break;
        }
        return formatedMoney;
    }

    public static ZonedDateTime formatDateTime(ZonedDateTime date) {
        ZonedDateTime n = ZonedDateTime.now();
        return date.withHour(n.getHour()).withMinute(n.getMinute()).withSecond(n.getSecond());
    }


    public static void setKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    public static String decrypt(String content) {
        if(content.length()<16){
            return null;
        }else{
            String psk= "0060606060606060";
            String iv = "0060606060606060";
            String a = content.replace("%2F","/").replace(" ","+");
            int b= a.length();
            byte[] cipherText = Base64.decodeBase64(a);
            String encryptionKey = psk;
            final Cipher cipher;
            try {
                cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE");
                final SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(UTF8), "AES");
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv.getBytes(UTF8)));
                return new String(cipher.doFinal(cipherText), UTF8);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (NoSuchProviderException e) {
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (InvalidAlgorithmParameterException e) {
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                e.printStackTrace();
            } catch (BadPaddingException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
