package out.production.bienjava1;

import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

public class GalacticToHorizon {
    // Hằng số
    public static final double PI = Math.PI; // Hằng số PI

    // Hàm chuyển đổi tọa độ Galactic sang tọa độ Horizon
    public static void galacticToHorizon(double l, double b, double latitude, double longitude, int utcOffset, double[] result) {
        // Chuyển đổi tọa độ Galactic sang tọa độ Equatorial
        double raHours = 12 + (l / 15.0); // RA tính bằng giờ
        double raDegrees = raHours * 15; // Chuyển RA sang độ
        double dec = b; // Độ lệch (declination) chính là vĩ độ Galactic

        // Lấy thời gian hiện tại
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault()); // Lấy TimeZone của máy
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        // Tính LST (Local Sidereal Time)
        double currentTime = hour + minute / 60.0 + second / 3600.0; // Thời gian hiện tại
        double lst = (currentTime * 15) + longitude + (utcOffset * 15); // LST tính bằng độ
        lst = (lst + 360) % 360; // Đảm bảo LST trong khoảng [0, 360)

        // Tính Hour Angle (HA)
        double ha = lst - raDegrees; // Giờ thiên thể (HA) tính bằng độ
        ha = (ha + 360) % 360; // Đảm bảo HA trong khoảng [0, 360)
        double haRad = Math.toRadians(ha);
        double latitudeRad = Math.toRadians(latitude);
        double decRad = Math.toRadians(dec);

        // Công thức tính Altitude (độ cao)
        double sinAltitude = Math.sin(latitudeRad) * Math.sin(decRad) +
                Math.cos(latitudeRad) * Math.cos(decRad) * Math.cos(haRad);
        double altitude = Math.asin(sinAltitude) * (180.0 / PI);
        result[0] = altitude;

        // Công thức tính Azimuth (phương vị)
        double cosAltitude = Math.cos(Math.asin(sinAltitude)); // cos(altitude)
        double sinAzimuth = -Math.cos(decRad) * Math.sin(haRad) / cosAltitude;
        double cosAzimuth = (Math.sin(decRad) - Math.sin(latitudeRad) * sinAltitude) /
                (Math.cos(latitudeRad) * cosAltitude);

        double azimuth = Math.toDegrees(Math.atan2(sinAzimuth, cosAzimuth));
        azimuth = (azimuth + 360) % 360; // Đảm bảo azimuth trong khoảng [0, 360)
        result[1] = azimuth;
    }

    public static void printLocalTime() {
        Calendar calendar = Calendar.getInstance();
        System.out.printf("Thời gian địa phương hiện tại: %tF %tT%n", calendar, calendar);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Nhập tọa độ người dùng
        System.out.print("Nhập kinh độ Galactic (l): ");
        double l = scanner.nextDouble();
        System.out.print("Nhập vĩ độ Galactic (b): ");
        double b = scanner.nextDouble();

        // Vĩ độ và kinh độ hiện tại của Hà Nội
        double latitude = 21.0285; // Vĩ độ của Hà Nội
        double longitude = 105.8542; // Kinh độ của Hà Nội
        int utcOffset = 7; // UTC +7

        // Kiểm tra tọa độ nhập vào
        if (l < 0 || l > 360 || b < -90 || b > 90) {
            System.out.println("Giá trị kinh độ (l) phải trong khoảng [0, 360] và vĩ độ (b) phải trong khoảng [-90, 90].");
            return;
        }

        double[] result = new double[2]; // result[0] là Altitude, result[1] là Azimuth

        // Tính toán tọa độ Horizon
        galacticToHorizon(l, b, latitude, longitude, utcOffset, result);

        // In ra thời gian địa phương
              printLocalTime();
              // Xuất kết quả
              System.out.printf("Altitude: %.2f°%n", result[0]);
              System.out.printf("Azimuth: %.2f°%n", result[1]);s
           }
        }