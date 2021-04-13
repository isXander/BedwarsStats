package co.uk.isxander.mcstats.licensing;

public class License {

    private String serial;
    private long timeCreated;
    private boolean isLicenseValid;
    private LicenseLength length;

    public License(String serial, long timeCreated, boolean valid, LicenseLength length) {
        this.serial = serial;
        this.timeCreated = timeCreated;
        this.length = length;
        this.isLicenseValid = valid;
    }

    public String getSerialKey() {
        return serial;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public long getTimeRemaining() {
        return (System.currentTimeMillis() - (timeCreated + length.getTimeMillis()));
    }

    public boolean isValid() {
        return this.isLicenseValid && System.currentTimeMillis() - timeCreated <= length.getTimeMillis();
    }

    public LicenseLength getLength() {
        return this.length;
    }

    public enum LicenseLength {
        WEEK(604800000L),
        MONTH(2628000000L),
        YEAR(31540000000L);

        private final long timeMillis;
        LicenseLength(long timeMillis) {
            this.timeMillis = timeMillis;
        }

        public long getTimeMillis() {
            return timeMillis;
        }
    }

}
