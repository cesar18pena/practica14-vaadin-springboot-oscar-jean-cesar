package ce.pucmm.edu.practica14vaadinspringbootoscarjeancesar.Model;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.context.annotation.ApplicationScope;
import org.vaadin.addon.calendar.item.CalendarItem;
import org.vaadin.addon.calendar.item.EditableCalendarItem;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@ApplicationScope
public class Evento implements Serializable, CalendarItem, EditableCalendarItem, EditableCalendarItem.ItemChangeNotifier {
    @Id
    @GeneratedValue
    private Long id;

    private String caption;
    private String description;
    private String styleName;
    private boolean isAllDay;
    private boolean notified;

    @DateTimeFormat
    private ZonedDateTime start;

    @DateTimeFormat
    private ZonedDateTime end;

    @Transient
    private List<ItemChangeListener> listeners = new ArrayList<ItemChangeListener>();

    @Transient
    ItemChangeNotifier notifier;

    public Evento() {
    }

    public Evento(String caption, String description, boolean isAllDay, ZonedDateTime start, ZonedDateTime end) {
        this.caption = caption;
        this.description = description;
        this.isAllDay = isAllDay;
        this.start = start;
        this.end = end;
    }

    @Override
    public void addListener(ItemChangeListener listener) {
        getListeners().add(listener);
    }

    @Override
    public void removeListener(ItemChangeListener listener) {
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

    @Override
    public ItemChangeNotifier getNotifier() {
        return notifier;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    @Override
    public ZonedDateTime getStart() {
        return start;
    }

    @Override
    public void setStart(ZonedDateTime start) {
        this.start = start;
    }

    @Override
    public ZonedDateTime getEnd() {
        return end;
    }

    @Override
    public void setEnd(ZonedDateTime end) {
        this.end = end;
    }

    public List<ItemChangeListener> getListeners() {
        return listeners;
    }

    public void setListeners(List<ItemChangeListener> listeners) {
        this.listeners = listeners;
    }
}
