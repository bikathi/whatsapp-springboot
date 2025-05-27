package npc.bikathi.whatsappintg.service;

import com.google.gson.Gson;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.SystemMessage;
import com.theokanning.openai.service.OpenAiService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import npc.bikathi.whatsappintg.config.gpt.GptConfig;
import npc.bikathi.whatsappintg.entity.GptResponse;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GptPromptService {
    private final GptConfig gptConfig;
    private final Gson gson = new Gson();

    public GptResponse promptGpt(@NotNull String broadcastMessage, @NotNull String clientResponse) {
        GptResponse parsedGptResponse;
        try {
            // prompt the Chatbot using the wrapper library
            OpenAiService service = new OpenAiService(gptConfig.getApiKey() , Duration.ofSeconds(60));
            List<ChatMessage> messages = new ArrayList<>();
            ChatMessage systemMessage = new SystemMessage(this.constructPrompt(broadcastMessage, clientResponse));
            messages.add(systemMessage);
            ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model(gptConfig.getModel())
                .messages(messages)
                .n(1)
                .maxTokens(4000)
            .build();

            ChatCompletionResult chatCompletion = service.createChatCompletion(chatCompletionRequest);
            String gptResponse = chatCompletion.getChoices().get(0).getMessage().getContent();

            // covert the response JSON string to a GPTResponseStructure using GSON
            parsedGptResponse = gson.fromJson(gptResponse, GptResponse.class);
        } catch(Exception ex) {
            log.warn("GPT-based summary failed due to: {}. Falling back to default settings!", ex.getMessage());
            parsedGptResponse = new GptResponse();
        }

        return parsedGptResponse;
    }

    private String constructPrompt(@NotNull String broadcastMessage, @NotNull String clientResponse) {
        StringBuilder prompt = new StringBuilder();

        prompt.append("You are a helpful assistant that analyzes client responses for an inquiry sent to them via WhatsApp, for relevancy. ");
        prompt.append("The client responds to the message via paragraph or a sentence, but their response can contain a cost value of whatever is in the inquiry. ");
        prompt.append("Carefully analyze the response and determine if it is relevant to the inquiry they're responding to. ");
        prompt.append("Here is the inquiry that was sent to them: \n");
        prompt.append(broadcastMessage);
        prompt.append("\n");
        prompt.append("Here is the client's response: \n");
        prompt.append(clientResponse);
        prompt.append("\n");
        prompt.append("You are strictly only to respond with a JSON string that looks like this: \n");
        prompt.append("{ \n");
        prompt.append("    \"isRelevant\": <true or false>, \n");
        prompt.append("    \"partCost\": \"<cost of the part or null if none can be determined>\", \n");
        prompt.append("    \"thoughtProcess\": \"<your thought process when deciding if the response is relevant>\", \n");
        prompt.append("} \n");
        prompt.append("For any further explanations, append them to the 'thoughtProcess' field.");
        prompt.append("Ensure that your only response is only that JSON structure, as your response is going to be parsed to a Java object.");
        return prompt.toString();
    }
}
