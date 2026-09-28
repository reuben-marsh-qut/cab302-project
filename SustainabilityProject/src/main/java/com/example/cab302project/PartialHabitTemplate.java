package com.example.cab302project;

import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;

public record PartialHabitTemplate(String title, TaskType taskType, int target, RepeatFrequencyType repeatFrequencyType, int repeatFrequency){}
