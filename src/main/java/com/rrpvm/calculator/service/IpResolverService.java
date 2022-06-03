package com.rrpvm.calculator.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Scanner;

public class IpResolverService {
    private String getIpAddress() {
        URL url = null;
        HttpURLConnection connection = null;
        Scanner scanner = null;
        String result = "";
        try {
            url = new URL("https://api.ipify.org?format=json");
            connection = (HttpURLConnection) url.openConnection();
            scanner = new Scanner(connection.getInputStream());
            if (scanner.hasNextLine()) result = scanner.nextLine();
        } catch (MalformedURLException malformedURLException) {
            malformedURLException.printStackTrace();
        } catch (UnknownHostException unknownHostException) {
            return "localhost";
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (scanner != null)
                scanner.close();
            if (connection != null)
                connection.disconnect();
            if (result.length() > 7) {
                result = result.substring(7, result.length() - 2);
            }
        }
        return result;
    }

    public String resolveIp() {
        return this.getIpAddress();
    }
}
