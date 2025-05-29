package org.example.zentrio.service;

import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.model.Calendar;
import org.example.zentrio.model.Task;

import java.util.HashSet;
import java.util.UUID;

public interface CalendarService {
    HashSet<Task> getCalendarForCurrentUser(UUID board_id);


    Calendar CreateNoteInCalendar( CalendarRequest calendarRequest);

    Calendar getCalendarById(UUID notedId);

    HashSet<Calendar> getAllCalendarByTaskId(UUID taskId);

    Calendar UpdateCalendarByNoteId(UUID noteId, CalendarRequest calendarRequest);

    Void deleteCalendarByNoteId(UUID noteId, UUID taskId);
}
