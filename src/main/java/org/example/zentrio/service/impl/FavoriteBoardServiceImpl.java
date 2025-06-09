package org.example.zentrio.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.zentrio.exception.ConflictException;
import org.example.zentrio.exception.NotFoundException;
import org.example.zentrio.model.AppUser;
import org.example.zentrio.model.Board;
import org.example.zentrio.model.FavoriteBoard;
import org.example.zentrio.repository.AppUserRepository;
import org.example.zentrio.repository.BoardRepository;
import org.example.zentrio.repository.FavouriteBoardRepository;
import org.example.zentrio.service.FavoriteBoardService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class FavoriteBoardServiceImpl implements FavoriteBoardService {

    private final AppUserRepository appUserRepository;
    private final BoardRepository boardRepository;
    private final FavouriteBoardRepository favoriteBoardRepository;


    @Override
    public FavoriteBoard createFavoriteBoard(UUID boardId, UUID userId) {
        Board board = boardRepository.getBoardByBoardId(boardId);
        if(board == null) {
            throw new NotFoundException("Board with ID " + boardId + " not found");
        }
        AppUser user = appUserRepository.getUserById(userId);
        if(user == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        FavoriteBoard favoriteBoard = favoriteBoardRepository.getByUserIdAndBoardId(userId, boardId);
        if(favoriteBoard != null) {
            throw new ConflictException("Board with ID " + boardId + " already marked as favorite");
        }
        return favoriteBoardRepository.insertFavoriteBoard(LocalDateTime.now(),board.getBoardId(),user.getUserId());
    }

    @Override
    public void deleteFavoriteBoard(UUID boardId, UUID userId) {
        Board board = boardRepository.getBoardByBoardId(boardId);
        if(board == null) {
            throw new NotFoundException("Board with ID " + boardId + " not found");
        }
        AppUser user = appUserRepository.getUserById(userId);
        if(user == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        favoriteBoardRepository.deleteFavoriteBoard(boardId,userId);
    }

    @Override
    public HashSet<Board> getFavoriteBoards(UUID userId) {
        AppUser user = appUserRepository.getUserById(userId);
        if(user == null) {
            throw new NotFoundException("User with ID " + userId + " not found");
        }
        return favoriteBoardRepository.getFavoriteBoards(userId);
    }
}
