package com.example.cab302project;

import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

public class ModelConnection {

    private static ModelConnection instance = new ModelConnection();
    private boolean isAvailable = false;

    private ChatModel JSONModel;
    private ChatModel model;

    public boolean isAvailable(){
        return instance.isAvailable;
    }

    public void initialise(String apiUrl){
        initialise(apiUrl, "dummy", "dummy");
    }
    public void initialise(String apiUrl, String apiKey){
        initialise(apiUrl, apiKey, "dummy");
    }
    public void initialise(String apiUrl, String apiKey, String modelName){
        try {
            instance.JSONModel = OpenAiChatModel.builder()
                    .baseUrl(apiUrl)
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                    .strictJsonSchema(true)
                    .build();
            instance.model = OpenAiChatModel.builder()
                    .baseUrl(apiUrl)
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .build();
            instance.model.chat("test");
            instance.JSONModel.chat("test");
        } catch (Exception e) {
            isAvailable = false;
            System.err.println("AI model connection is unavailable");
            return;
//            throw new RuntimeException(e);
        }
        isAvailable = true;

    }

    public static ModelConnection getInstance() {
        if (instance == null) {
            instance = new ModelConnection();
        }
            return instance;
    }

    public ChatModel getJSONModel() {
        return JSONModel;
    }

    public ChatModel getModel() {
        return model;
    }
}
