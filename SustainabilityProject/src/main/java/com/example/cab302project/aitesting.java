package com.example.cab302project;

import com.example.cab302project.model.Goal;
import com.example.cab302project.model.Habit;
import com.example.cab302project.model.HabitTemplate;
import com.example.cab302project.model.enums.Category;
import com.example.cab302project.model.enums.RepeatFrequencyType;
import com.example.cab302project.model.enums.TaskType;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.request.ChatRequest;
import dev.langchain4j.model.chat.request.ResponseFormat;
import dev.langchain4j.model.chat.request.ResponseFormatType;
import dev.langchain4j.model.chat.request.json.JsonObjectSchema;
import dev.langchain4j.model.chat.request.json.JsonSchema;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.data.message.SystemMessage;
import javafx.application.Application;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class aitesting {
    static void run(){
//
//        System.out.println(generateHabitFromPartialGoal("Get Fit", Category.BODY));
//        System.out.println(generateHabitFromPartialGoal("Get Fit", Category.BODY));
//        System.out.println(generateHabitFromPartialGoal("Get Fit", Category.BODY));
//        String test = generateHabitFromPartialGoal("Improve my mental fitness and health", Category.MIND).toString();
//        System.out.println(test);
//        System.out.println(generateHabitFromPartialGoal("Improve my mental fitness and health", Category.MIND,test));
//        System.out.println(generateHabitFromPartialGoal("Improve my mental fitness and health", Category.MIND,test));
//        System.exit(0);
    }
    public static HabitTemplate generateHabitFromPartialGoal(String goalTitle, Category goalCatagory){
        return generateHabitFromPartialGoal(goalTitle,goalCatagory,null);
    }
    public static HabitTemplate generateHabitFromPartialGoal(String goalTitle, Category goalCatagory, String doNotGenerateThis){
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("http://localhost:8080/")
                .apiKey("dummyKey")
                .modelName("dummy")
                .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                .strictJsonSchema(true)
                .build();

        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormatType.JSON)
                .jsonSchema(JsonSchema.builder()
                        .name("Habit")
                        .rootElement(JsonObjectSchema.builder()
                                .addStringProperty("title","Describe a beneficial task that can be repeated. e.g. Save $2,000, Walk 10,000 steps")
                                .addEnumProperty("taskType", List.of("BINARY", "PROGRESSIVE"), "Do you progressively work towards completing the task or is completion binary.")
                                .addIntegerProperty("target", "If the task is Progressive what should be the target number for task completion. If the task is Binary this is 1.")
                                .addEnumProperty("repeatFrequencyType", List.of("DAILY","WEEKLY","MONTLY","YEARLY"), "How often should the habit repeat.")
                                .addIntegerProperty("repeatFrequency", "How long should the interval between repeats be. e.g. if repeatFrequencyType is Daily and repeatFrequency is 1 the habit will repeat each day, if repeatFrequency is 2 the habit will repeat every two days.")
                                .build())
                        .build())
                .build();
        SystemMessage systemMessage = SystemMessage.from("""
                Generate a habit from a given goal. The habit should be a beneficial task that can be repeated. e.g. Save $2,000, Walk 10,000 steps.
                Do not generate NULL or null;
                
                BAD:
                {
                  "title": "Daily Movement Minimum", // WRONG: the title does not describe what the user must do
                  "taskType": "PROGRESSIVE",
                  "target": 15,
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 7 // WRONG: this means this will repeat every seven days instead of every 1 day
                }
                {
                  "title": "Write down three things you are grateful for each day",
                  "taskType": "BINARY", 
                  "target": 3, // WRONG: the target should be 1 if the task is binary
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 1
                }
                GOOD:
                Catagory: BODY
                {
                  "title": "Move for 15 minutes each day",
                  "taskType": "PROGRESSIVE",
                  "target": 15,
                  "repeatFrequencyType": "DAILY",
                  "repeatFrequency": 1
                }
                Catagory: MIND
                {
                  "title": "Complete a random act of kindness each month",
                  "taskType": "BINARY",
                  "target": 1,
                  "repeatFrequencyType": "MONTHLY",
                  "repeatFrequency": 1
                }
                Catagory: WORLD
                {
                  "title": "Swap 5 car trips each week for walking or public transport",
                  "taskType": "PROGRESSIVE",
                  "target": 5,
                  "repeatFrequencyType": "WEEKLY",
                  "repeatFrequency": 1
                }
                """);
        ChatRequest chatRequest;
        UserMessage userMessage = UserMessage.from(String.format("Generate a Habit from the goal '%s', the catagory of the goal is '%s'",goalTitle,goalCatagory));

        if (doNotGenerateThis != null){
            SystemMessage preventDuplicates = new SystemMessage(String.format("""
                    Do NOT duplicate the following habit/s as they have already been presented to the user. Please think of a distinct habit.
                    %s
                    """,doNotGenerateThis));
            chatRequest = ChatRequest.builder()
                    .responseFormat(responseFormat)
                    .messages(systemMessage,preventDuplicates,userMessage)
                    .build();
        } else {
            chatRequest = ChatRequest.builder()
                    .responseFormat(responseFormat)
                    .messages(systemMessage,userMessage)
                    .build();
        }

        ChatResponse response = model.chat(chatRequest);
        String output = response.aiMessage().text();
//        System.out.println(output);
        try {
            PartialHabitTemplate partialHabitTemplate = new ObjectMapper().readValue(output, PartialHabitTemplate.class);
            return new HabitTemplate(partialHabitTemplate.title(), goalCatagory,partialHabitTemplate.taskType(), partialHabitTemplate.taskType() == TaskType.PROGRESSIVE ? partialHabitTemplate.target() : 1, partialHabitTemplate.repeatFrequencyType(),partialHabitTemplate.repeatFrequency());
        } catch (Exception e){
            System.err.println(e.toString());
            return null;
//            throw e;
        }
    }


}
