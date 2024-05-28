package co.edu.uniquindio.edu.co.centroeventosuq.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
public class LocalTimeAdapter extends XmlAdapter<String, LocalTime> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;

    @Override
    public LocalTime unmarshal(String v) {
        return (v != null) ? LocalTime.parse(v, formatter) : null;
    }

    @Override
    public String marshal(LocalTime v) {
        return (v != null) ? v.format(formatter) : null;
    }
}
