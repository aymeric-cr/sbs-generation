package fr.femtost.sbs.alteration.core.incident;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Parameter {

    public static final String CHARAC_ICAO = "icao";
    public static final String CHARAC_CALLSIGN = "callsign";
    public static final String CHARAC_SQUAWK = "squawk";
    public static final String CHARAC_ALTITUDE = "altitude";
    public static final String CHARAC_GROUNDSPEED = "groundSpeed";
    public static final String CHARAC_TRACK = "track";
    public static final String CHARAC_LATITUDE = "latitude";
    public static final String CHARAC_LONGITUDE = "longitude";
    public static final String CHARAC_VERTICALRATE = "verticalRate";
    public static final String CHARAC_ALERT = "alert";
    public static final String CHARAC_TIMESTAMP = "timestamp";
    public static final String CHARAC_EMERGENCY = "emergency";
    public static final String CHARAC_SPI = "SPI";
    public static final String CHARAC_ISONGROUND = "isOnGround";

    @JacksonXmlProperty(isAttribute = true)
    private boolean offset;

    @JacksonXmlProperty(localName = "key")
    private String characteristic;

    private String value;

    private int number = 0;

    public boolean isOffset() {
        return offset;
    }

    public void setOffset(final boolean offset) {
        this.offset = offset;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(final String characteristic) {
        this.characteristic = characteristic;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(final int number) {
        this.number = number;
    }

    public interface CharacteristicSwitch<T> {

        T visitIcao();

        default T visitCallSign() {
            return null;
        }

        default T visitSquawk() {
            return null;
        }

        default T visitAltitude() {
            return null;
        }

        default T visitGroundSpeed() {
            return null;
        }

        default T visitTrack() {
            return null;
        }

        default T visitLatitude() {
            return null;
        }

        default T visitLongitude() {
            return null;
        }

        default T visitVerticalRate() {
            return null;
        }

        default T visitAlert() {
            return null;
        }

        default T visitEmergency() {
            return null;
        }

        default T visitSpi() {
            return null;
        }

        default T visitIsOnGround() {
            return null;
        }

        default T visitDefault() {
            return null;
        }

        default T doSwitch(final String characteristic) throws UnknownCharacteristicException {
            if (characteristic.compareTo(CHARAC_ICAO) == 0) {
                return visitIcao();
            }
            if (characteristic.compareTo(CHARAC_CALLSIGN) == 0) {
                return visitCallSign();
            }
            if (characteristic.compareTo(CHARAC_SQUAWK) == 0) {
                return visitSquawk();
            }
            if (characteristic.compareTo(CHARAC_ALTITUDE) == 0) {
                return visitAltitude();
            }
            if (characteristic.compareTo(CHARAC_GROUNDSPEED) == 0) {
                return visitGroundSpeed();
            }
            if (characteristic.compareTo(CHARAC_TRACK) == 0) {
                return visitTrack();
            }
            if (characteristic.compareTo(CHARAC_LATITUDE) == 0) {
                return visitLatitude();
            }
            if (characteristic.compareTo(CHARAC_LONGITUDE) == 0) {
                return visitLongitude();
            }
            if (characteristic.compareTo(CHARAC_VERTICALRATE) == 0) {
                return visitVerticalRate();
            }
            if (characteristic.compareTo(CHARAC_ALERT) == 0) {
                return visitAlert();
            }
            if (characteristic.compareTo(CHARAC_EMERGENCY) == 0) {
                return visitEmergency();
            }
            if (characteristic.compareTo(CHARAC_SPI) == 0) {
                return visitSpi();
            }
            if (characteristic.compareTo(CHARAC_ISONGROUND) == 0) {
                return visitIsOnGround();
            }
            throw new UnknownCharacteristicException(characteristic);
        }
    }
}