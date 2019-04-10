package com.lighthouse.aditum.service.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.ZonedDateTime;
import java.util.Locale;

/**
 * Utility class for generating random Strings.
 */
public final class RandomUtil {

    private static final int DEF_COUNT = 20;

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


    public static String formatMoneyString(String text) {
        double ammount = Double.parseDouble(text);
       return formatMoney(ammount);
    }


    public static String formatMoney(double ammount) {
        Locale locale = new Locale("es", "CR");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
        if(ammount==0){
            return "0";
        }else {
            if(ammount<0){
                String formatted = currencyFormatter.format(ammount).substring(2);
                return "- "+formatted.substring(0, formatted.length() - 4).replace(",", ".");
            }else{
                String formatted = currencyFormatter.format(ammount).substring(1);
                return formatted.substring(0, formatted.length() - 3).replace(",", ".");
            }
        }
    }

    public static ZonedDateTime formatDateTime(ZonedDateTime date) {
        ZonedDateTime n = ZonedDateTime.now();
        return date.withHour(n.getHour()).withMinute(n.getMinute()).withSecond(n.getSecond());
    }
}
