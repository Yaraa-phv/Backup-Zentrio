package org.example.zentrio.service;

import org.example.zentrio.model.Board;
import org.example.zentrio.model.FavoriteBoard;

import java.util.HashSet;
import java.util.UUID;

public interface FavoriteBoardService {

    FavoriteBoard createFavoriteBoard(UUID boardId, UUID userId);

    void deleteFavoriteBoard(UUID boardId, UUID userId);

    HashSet<Board> getFavoriteBoards(UUID userId);
}
