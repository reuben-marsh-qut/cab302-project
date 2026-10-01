package com.example.cab302project;

import dev.langchain4j.model.chat.Capability;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

/**
 * A singleton class that holds the references for the structured output and normal chat AI models.
 * <p>
 * isAvailable() must be checked prior to using either of the models otherwise langchain4j will throw an error.
 * <p>
 * Call initialise() to setup the connection with your choice of AI provider.
 */
public class ModelConnection {

    private static ModelConnection instance = new ModelConnection();
    private boolean isAvailable = false;

    private ChatModel JSONModel;
    private ChatModel chatModel;

    /**
     * True if AI models can be accessed, False otherwise
     * @return the availability of the AI models
     */
    public boolean isAvailable(){
        return instance.isAvailable;
    }

    /**
     * Initialises the connection with the AI provider
     * @param apiUrl url of an OpenAI compatible API
     */
    public void initialise(String apiUrl){
        initialise(apiUrl, "dummy", "dummy");
    }
    /**
     * Initialises the connection with the AI provider
     * @param apiUrl url of an OpenAI compatible API
     * @param apiKey api key
     */
    public void initialise(String apiUrl, String apiKey){
        initialise(apiUrl, apiKey, "dummy");
    }

    /**
     * Initialises the connection with the AI provider
     * @param apiUrl url of an OpenAI compatible API
     * @param apiKey api key
     * @param modelName desired model name
     */
    public void initialise(String apiUrl, String apiKey, String modelName){
        try {
            instance.JSONModel = OpenAiChatModel.builder()
                    .baseUrl(apiUrl)
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .supportedCapabilities(Capability.RESPONSE_FORMAT_JSON_SCHEMA)
                    .strictJsonSchema(true)
                    .build();
            instance.chatModel = OpenAiChatModel.builder()
                    .baseUrl(apiUrl)
                    .apiKey(apiKey)
                    .modelName(modelName)
                    .build();
            instance.chatModel.chat("test");
            instance.JSONModel.chat("test");
        } catch (Exception e) {
            isAvailable = false;
            System.err.println("AI model connection is unavailable");
            return;
//            throw new RuntimeException(e);
        }
        isAvailable = true;

    }

    /**
     * @return the singleton instance.
     */
    public static ModelConnection getInstance() {
        if (instance == null) {
            instance = new ModelConnection();
        }
            return instance;
    }

    /**
     * @return the structured output (JSON) ChatModel
     */
    public ChatModel getJSONModel() {
        return JSONModel;
    }

    /**
     * @return the ChatModel without structured output enabled.
     */
    public ChatModel getChatModel() {
        return chatModel;
    }
}
