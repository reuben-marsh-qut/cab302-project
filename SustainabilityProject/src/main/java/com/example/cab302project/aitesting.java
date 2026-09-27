package com.example.cab302project;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.HabitTemplate;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class aitesting {
    static void run(){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://localhost:8080/")
                .apiKey("dummyKey")
                .modelName("dummy")
                .build();
        String response = model.chat("Say 'Hello World'");
        System.out.println(response);
    }
    public static List<HabitTemplate> generateHabitsFromGoal(Goal goal, int number){

        return new ArrayList<HabitTemplate>();
    }


}
