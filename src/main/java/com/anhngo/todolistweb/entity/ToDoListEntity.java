package com.anhngo.todolistweb.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToDoListEntity {
    private int id;
    private String task;
    private boolean completed;
}
