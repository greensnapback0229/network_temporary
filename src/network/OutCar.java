package network;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class OutCar {
	private LocalDateTime time;
	private String carNum;
	private int floor;
	private int parkSpace;
	private String result;

	public OutCar(LocalDateTime time, String carNum, int floor, int parkSpace) {
		this.time = time;
		this.carNum = carNum;
		this.floor = floor;
		this.parkSpace = parkSpace;
	}
	public String processOut() {
        try (Socket socket = new Socket(NetworkSettings.srvIpAddr, NetworkSettings.portNum);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {
        	
        	String flag = Integer.toString(floor*10 +2);
        	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/HH/mm");
            String formattedTime = time.format(formatter);
            
            
//            // Header 생성
//        	byte[] header = new byte[8];
//        	ByteBuffer.wrap(header, 0, 4).put(flag.getBytes());
//            int bodySize = 26; // 22 + 4 
//            ByteBuffer.wrap(header, 4, 4).putInt(bodySize);
//
//            // Body 생성
//            byte[] body = new byte[bodySize];
//            ByteBuffer.wrap(body, 0, 22).put(formattedTime.getBytes());
//            ByteBuffer.wrap(body, 22, 4).putInt(parkSpace);
//
//            // 데이터 전송
//            byte[] request = new byte[bodySize + 8];
//            System.arraycopy(header, 0, request, 0, 8);
//            System.arraycopy(body, 0, request, 8, bodySize);
//            System.out.print("Request in Hexadecimal: ");
//            for (byte b : request) {
//                System.out.print(String.format("%02X ", b));
//            }
//            System.out.println();
//            out.write(request);
//
//            // 서버 응답 받기
//            byte[] response = new byte[31];
//            in.read(response);
//
//            // 응답 데이터 처리
//            String responseFlag = new String(Arrays.copyOfRange(response, 0, 4));
//            boolean isSuccess = response[4] == 1;
//            int responseBodySize = ByteBuffer.wrap(response, 5, 4).getInt();
//
//            // Body 데이터 처리
//            int price = ByteBuffer.wrap(response, 9, 4).getInt();
//            String carNum = new String(Arrays.copyOfRange(response, 13, 31));
//            
//            System.out.print("Response in Hexadecimal: ");
//            for (byte b : response) {
//                System.out.print(String.format("%02X ", b));
//            }
//
//            // 결과 문자열 생성
//            result = "!!출차되었습니다!!<br>차 번호: " + carNum + "<br>가격: " + price;
//            System.out.println(result);
            
            
         // Header 생성
        	byte[] header = new byte[6];
        	ByteBuffer.wrap(header, 0, 2).put(flag.getBytes());
            int bodySize = 15; // 11 + 4 
            ByteBuffer.wrap(header, 2, 4).putInt(bodySize);

            // Body 생성
            byte[] body = new byte[bodySize];
            ByteBuffer.wrap(body, 0, 11).put(formattedTime.getBytes());
            ByteBuffer.wrap(body, 11, 4).putInt(parkSpace);

            // 데이터 전송
            byte[] request = new byte[bodySize + 6];
            System.arraycopy(header, 0, request, 0, 6);
            System.arraycopy(body, 0, request, 6, bodySize);
            System.out.print("Request in Hexadecimal: ");
            for (byte b : request) {
                System.out.print(String.format("%02X ", b));
            }
            System.out.println();
            out.write(request);

            // 서버 응답 받기
            byte[] response = new byte[22];
            in.readFully(response);

            //응답 데이터 처리
            String responseFlag = new String(Arrays.copyOfRange(response, 0, 2));
            boolean isSuccess = response[2] == 1;
            int responseBodySize = ByteBuffer.wrap(response, 3, 4).getInt();

            // Body 데이터 처리
            int price = ByteBuffer.wrap(response, 7, 4).getInt();
            String carNum = new String(Arrays.copyOfRange(response, 11, 22));
            
            System.out.print("Response in Hexadecimal: ");
            for (byte b : response) {
                System.out.print(String.format("%02X ", b));
            }

            // 결과 문자열 생성
            result = "<html><body>!!출차되었습니다!!<br>차 번호: " + carNum + "<br>가격: " + price + "</body></html>";
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }

		return result;
	}
}