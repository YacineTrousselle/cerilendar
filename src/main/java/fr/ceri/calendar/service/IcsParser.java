package fr.ceri.calendar.service;

import biweekly.Biweekly;
import biweekly.ICalVersion;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalWriter;
import fr.ceri.calendar.MainApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class IcsParser {

    public static List<VEvent> parseIcsFile(Path filepath) throws URISyntaxException, IllegalArgumentException, IOException {
        URL url = MainApplication.class.getResource(filepath.toString());
        if (null == url) {
            throw new IllegalArgumentException(String.format("File not found at path %s", filepath));
        }
        File file = new File(url.toURI());

        ICalendar iCalendar = Biweekly.parse(file).first();

        return iCalendar.getEvents();
    }

    public static List<VEvent> parseIcsFile(String filename) throws URISyntaxException, IllegalArgumentException, IOException {
        URL url = MainApplication.class.getResource("ics/" + (filename.endsWith(".ics") ? filename : filename + ".ics"));
        if (null == url) {
            throw new IllegalArgumentException(String.format("File not found: %s", filename));
        }
        File file = new File(url.toURI());

        ICalendar iCalendar = Biweekly.parse(file).first();

        return iCalendar.getEvents();
    }

    public static List<VEvent> parseIcsUrl(String url) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        httpURLConnection.setRequestMethod("GET");

        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);

        int status = httpURLConnection.getResponseCode();
        System.out.println("Response Status Code: " + status);

        if (httpURLConnection.getResponseCode() > 200) {
            throw new IOException(String.format("Failed to get calendar. Error status : %s", status));
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        ICalendar iCalendar = Biweekly.parse(in).first();
        in.close();

        return iCalendar.getEvents();
    }

    public static void saveIcsFromUrl(String url, Path icsPath) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) URI.create(url).toURL().openConnection();
        httpURLConnection.setRequestMethod("GET");

        httpURLConnection.setConnectTimeout(5000);
        httpURLConnection.setReadTimeout(5000);

        int status = httpURLConnection.getResponseCode();
        System.out.println("Response Status Code: " + status);

        if (httpURLConnection.getResponseCode() > 200) {
            throw new IOException(String.format("Failed to get calendar. Error status : %s", status));
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        ICalendar iCalendar = Biweekly.parse(in).first();
        in.close();

        File file = Files.createFile(icsPath).toFile();
        ICalWriter iCalWriter = new ICalWriter(file, ICalVersion.V2_0);
        iCalWriter.write(iCalendar);
        iCalWriter.close();
    }

}
