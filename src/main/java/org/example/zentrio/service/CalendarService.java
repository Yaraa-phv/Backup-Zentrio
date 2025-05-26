package org.example.zentrio.service;

import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.model.Calendar;
import org.example.zentrio.model.Checklist;

import java.util.HashSet;
import java.util.UUID;

public interface CalendarService {
    HashSet<Checklist> getCalendarForCurrentUser(UUID board_id);


    Calendar CreateNoteInCalendar(UUID checkListId, CalendarRequest calendarRequest);

    Calendar getCalendarById(UUID notedId);

    HashSet<Calendar> getAllCalendarByChecklistId(UUID checklistId);

    Calendar UpdateCalendarByNoteId(UUID noteId, CalendarRequest calendarRequest);

    Calendar deleteCalendarByNoteId(UUID noteId);
}
