package ru.practicum.shareit.comments;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.comments.dto.CommentDto;
import ru.practicum.shareit.comments.dto.CreateCommentDto;

import java.util.List;

@Service
public class CommentMapper {
    public CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .text(comment.getText())
                .authorName(comment.getAuthor().getName())
                .created(comment.getCreated())
                .id(comment.getId())
                .build();
    }

    public Comment toEntity(CreateCommentDto createCommentDto) {
        return Comment.builder()
                .text(createCommentDto.getText().trim())
                .build();
    }

    public List<CommentDto> toCommentDto(List<Comment> comments) {
        return comments.stream()
                .map(this::toCommentDto)
                .toList();
    }
}
