package com.cobra.tmp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobParameter
{
    private String name;
    private String group;
    private String cron;
    private String description;
}
