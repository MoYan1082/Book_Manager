package com.moyan.common.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserHistory {
    private Long userId;
    BookRespond bookRespond;
}
