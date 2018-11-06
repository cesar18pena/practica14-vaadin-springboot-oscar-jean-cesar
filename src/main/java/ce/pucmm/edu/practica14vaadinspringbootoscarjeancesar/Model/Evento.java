package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model;

import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.EditableCalendarEvent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.context.annotation.ApplicationScope;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@ApplicationScope
public class Evento implements Serializable, CalendarEvent, EditableCalendarEvent, CalendarEvent.EventChangeNotifier {

    @Id
    @GeneratedValue
    private Long id;

    private String caption;
    private String description;
    private String styleName;
    private boolean isAllDay;
    private boolean notified;

    @DateTimeFormat
    private Date start;

    @DateTimeFormat
    private Date end;

    @Transient
    private List<EventChangeListener> listeners = new ArrayList<EventChangeListener>();

    public Evento() {
    }

    public Evento(String caption, String description, boolean isAllDay, Date start, Date end) {
        this.caption = caption;
        this.description = description;
        this.isAllDay = isAllDay;
        this.start = start;
        this.end = end;
    }

    @Override
    public void addEventChangeListener(EventChangeListener listener) {
        getListeners().add(listener);
    }

    @Override
    public void removeEventChangeListener(EventChangeListener listener) {
        getListeners().remove(listener);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getStyleName() {
        return styleName;
    }

    @Override
    public void setStyleName(String styleName) {
        this.styleName = styleName;
    }

    @Override
    public boolean isAllDay() {
        return isAllDay;
    }

    @Override
    public void setAllDay(boolean allDay) {
        isAllDay = allDay;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public Date getStart() {
        return start;
    }

    @Override
    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public Date getEnd() {
        return end;
    }

    @Override
    public void setEnd(Date end) {
        this.end = end;
    }

    public List<EventChangeListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<EventChangeListener> listeners) {
        this.listeners = listeners;
    }
}
