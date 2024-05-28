package co.edu.uniquindio.edu.co.centroeventosuq.utils;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {

    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate unmarshal(String v) {
        return (v != null) ? LocalDate.parse(v, formatter) : null;
    }

    @Override
    public String marshal(LocalDate v) {
        return (v != null) ? v.format(formatter) : null;
    }
}
