package org.example.zentrio.service.impl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.zentrio.dto.request.CalendarRequest;
import org.example.zentrio.dto.request.CommentRequest;
import org.example.zentrio.exception.BadRequestException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.*;
import org.example.zentrio.repository.CalendarRepository;
import org.example.zentrio.repository.MemberRepository;
import org.example.zentrio.service.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final CalendarRepository calendarRepository;
    private final ChecklistService checklistService;
    private final BoardService boardService;
    private final CommentService commentService;
    private final TaskService taskService;
    private final MemberRepository memberRepository;

    UUID userId(){
        UUID userId = ((AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUserId();
        return userId;
    }


    private void validateNotedWithChecklist(CalendarRequest calendarRequest, Checklist checklist) {
        LocalDateTime now = LocalDateTime.now();

        //  Checklist start time cannot be in the past
        if (calendarRequest.getStartDate().isBefore(now)) {
            throw new BadRequestException("Checklist start time cannot be in the past.");
        }

            // Checklist finish time must be the same or after start time
            if (calendarRequest.getTillDate().isBefore(calendarRequest.getStartDate())) {
                throw new BadRequestException("Checklist finish time cannot be before start time.");
            }

            // Checklist finish time must not exceed Task's finish time
            if (calendarRequest.getTillDate().isAfter(checklist.getFinishedAt())) {
                throw new BadRequestException("Checklist finish time cannot exceed Task's finish time.");
            }

//        // Checklist start time must not be before Task's start time
            if (calendarRequest.getStartDate().isBefore(checklist.getStartedAt())) {
                throw new BadRequestException("Checklist start time cannot be before Task's start time.");
            }
        }



    @Override
    public HashSet<Checklist> getCalendarForCurrentUser(UUID boardId) {
        boardService.getBoardByBoardId(boardId);
        HashSet<Checklist>  calendar=new HashSet<>(calendarRepository.getCalendarTasksForUser(userId(), boardId))  ;
        if (calendar.isEmpty()){
            throw new NotFoundException("No calendar found for userId: " + userId());

        }
        return calendar;

    }

    @Override
    public Calendar CreateNoteInCalendar(UUID checkListId , CalendarRequest calendarRequest) {
    Checklist checklist = checklistService.getChecklistChecklistId(checkListId);
    validateNotedWithChecklist(calendarRequest, checklist);
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent(calendarRequest.getNoted());
     Comment comment= commentService.createComment(checkListId,commentRequest );
    Calendar calendar= calendarRepository.createCalendar(calendarRequest,LocalDateTime.now(), userId(), checkListId,comment.getId());

        return calendar;
    }

    @Override
    public Calendar getCalendarById(UUID notedId) {
        Calendar  calendar= calendarRepository.getCalendarById(notedId);
        if (calendar == null){
            throw new NotFoundException("No calendar found for userId: " + userId());
        }
        return calendar;
    }

    @Override
    public HashSet<Calendar> getAllCalendarByChecklistId(UUID checklistId) {
            checklistService.getChecklistChecklistId(checklistId);
        HashSet<Calendar> calendars= new HashSet<>(calendarRepository.getAllCalendarByChecklistId(checklistId));
        if (calendars.isEmpty()){
            throw new NotFoundException("No calendar found for userId: " + userId());
        }
        return calendars;
    }

    @Override
    public Calendar UpdateCalendarByNoteId(UUID noteId, CalendarRequest calendarRequest) {
      Calendar calendar=  getCalendarById(noteId);
      if (calendar == null){
          throw new NotFoundException("No calendar found for userId: " + userId());
      }

      UUID checkListId = calendar.getCheckListId();
        Checklist checklist = checklistService.getChecklistChecklistId(checkListId);
        validateNotedWithChecklist(calendarRequest, checklist);
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent(calendarRequest.getNoted());
        commentService.UpdateCommentByCommentId(calendar.getCommentId(),commentRequest );
        Calendar calendarUpdate= calendarRepository.UpdateCalendarByNoteId(calendarRequest,noteId);


        return calendarUpdate;
    }

    @Override
    public Void deleteCalendarByNoteId(UUID noteId) {
        Calendar calendar = getCalendarById(noteId);
        if (calendar == null) {
            throw new NotFoundException("No calendar found ");
        }
        commentService.deleteCommentByCommentId(calendar.getCommentId());
        calendarRepository.deleteCalendarByNoteId(noteId);

        return null;
    }
}
