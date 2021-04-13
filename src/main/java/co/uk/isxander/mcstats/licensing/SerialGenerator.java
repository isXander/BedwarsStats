package co.uk.isxander.mcstats.licensing;

import co.uk.isxander.mcstats.utils.Log;
import co.uk.isxander.mcstats.utils.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SerialGenerator {

    public static void main(String[] args) throws Exception {
        File week = new File("./week.txt");
        Files.write(week.toPath(), generateRandomSerials(License.LicenseLength.WEEK), Charset.defaultCharset());

        File month = new File("./month.txt");
        Files.write(month.toPath(), generateRandomSerials(License.LicenseLength.MONTH), Charset.defaultCharset());

        File year = new File("./year.txt");
        Files.write(year.toPath(), generateRandomSerials(License.LicenseLength.YEAR), Charset.defaultCharset());

//        FileWriter writer = new FileWriter("week.txt");
//        for (String str : generateRandomSerials(License.LicenseLength.WEEK)) {
//            writer.write(str + System.lineSeparator());
//        }
//        writer.close();
//
//        writer = new FileWriter("month.txt");
//        for (String str : generateRandomSerials(License.LicenseLength.MONTH)) {
//            writer.write(str + System.lineSeparator());
//        }
//        writer.close();
//
//        writer = new FileWriter("year.txt");
//        for (String str : generateRandomSerials(License.LicenseLength.YEAR)) {
//            writer.write(str + System.lineSeparator());
//        }
//        writer.close();
    }

    public static List<String> generateRandomSerials(License.LicenseLength length) {
        Date date = new Date();

        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        String day = new StringBuilder(dayFormat.format(date)).reverse().toString();

        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        String month = monthFormat.format(date);

        SimpleDateFormat yearFormat = new SimpleDateFormat("yy");
        String year = new StringBuilder(yearFormat.format(date)).reverse().toString();

        String parsedDate = month + year + day;

        int type = length.ordinal();
        List<String> lengthEncrypt = new ArrayList<>();
        long num = 0;
        while (num <= 9999L) {
            int sum = Long.toString(num)
                    .chars()
                    .map(Character::getNumericValue)
                    .sum();

            if (sum % 3 == type) {
                StringBuilder sb = new StringBuilder(Long.toString(num));
                while (sb.length() != 4) {
                    sb.insert(0, "0");
                }
                lengthEncrypt.add(sb.toString());
            }

            num++;
        }

        List<String> serials = new ArrayList<>();
        num = 12121212121212121L;
        int lengthSize = lengthEncrypt.size();
        while (num <= 99999999999999999L) {
            int sum = Long.toString(num)
                    .chars()
                    .map(Character::getNumericValue)
                    .sum();

            if (sum % 7 == 0) {
                for (String len : lengthEncrypt) {
                    String mod7 = Long.toString(num);
                    if (mod7.length() != 17 || mod7.contains("0"))
                        continue;
                    int index = StringUtils.hasDuplicateCharacters(mod7, 3);
                    if (index != -1) {
                        System.out.println("Before: " + num);
                        num += 10 ^ (index + 1);
                        System.out.println("After: " + num);
                        continue;
                    }

                    String serial = parsedDate + "-" + mod7 + "-" + len;
                    System.out.println(serial);
                    serials.add(serial);
                }
            }
            num++;
        }

        return serials;
    }

}
