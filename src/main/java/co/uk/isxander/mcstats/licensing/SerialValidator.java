package co.uk.isxander.mcstats.licensing;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.regex.Pattern;

public class SerialValidator {

    public static final Pattern serialPattern = Pattern.compile("^[0-9]{6}-[1-9]{17}-[0-9]{4}$");

    // Date - MOD7 - Type
    public static License getLicense(String serial) {
        String formatted = serial.trim();
        if (serialPattern.matcher(formatted).matches()) {
            try {
                String[] split = formatted.split("-");

                long unixDate =
                        LocalDate.of(
                                Integer.parseInt("20" + new StringBuilder(split[0].substring(2, 4)).reverse().toString()),
                                Integer.parseInt(split[0].substring(0, 2)),
                                Integer.parseInt(new StringBuilder(split[0].substring(4, 6)).reverse().toString())
                        ).atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

                int mod7Sum = split[1]
                        .chars()
                        .map(Character::getNumericValue)
                        .sum();

                boolean valid = mod7Sum % 7 == 0;

                String typeStr = split[2];
                int typeSum = typeStr
                        .chars()
                        .map(Character::getNumericValue)
                        .sum();

                int type = typeSum % 3;
                if (type > 2) {
                    System.out.println("Fourth");
                    return null;
                }
                License.LicenseLength length = License.LicenseLength.values()[type];

                return new License(formatted, unixDate, valid, length);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        } else {
            System.out.println("Zero");
            return null;
        }
    }

}
