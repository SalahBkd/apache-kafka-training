package me.boukadi.kafkaspringapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor @NoArgsConstructor
public class PageEvent {
    private String page;
    private Date date;
    private int duration;
}
