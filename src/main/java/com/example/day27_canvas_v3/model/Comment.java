package com.example.day27_canvas_v3.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Comment {
    private String cId;
    private String user;
    private Integer rating;
    private String comment;
    private Integer gid;
}
