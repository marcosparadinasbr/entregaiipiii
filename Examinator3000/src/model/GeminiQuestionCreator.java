package model;
import es.usal.genai.*;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.genai.types.Schema;

public class GeminiQuestionCreator implements QuestionCreator {
    private final String API_KEY;
    private final String modelId;
    private GenAiConfig config;
    private final String questionCreatorDescription = "Creador de preguntas basado en Gemini AI de Google.";
    GenAiFacade genai;

    public GeminiQuestionCreator(String API_KEY, String modelId) {
        this.API_KEY = API_KEY;
        this.modelId = modelId;
        config = GenAiConfig.fromEnv(modelId);
    }
    public String getQuestionCreatorDescription(){
        return questionCreatorDescription;
    }
    public Question createQuestion(String topic) throws QuestionCreatorException {
        GenAiConfig.setSilentMode();
        try (GenAiFacade genai = new GenAiFacade(config)) {
            String prompt = "Crea una pregunta tipo test en español sobre el tema: " + topic + ".\n" +
            """
            Requisitos obligatorios:
            - EXACTAMENTE 4 opciones.
            - Cada opción debe tener:
                * "text": texto de la opción
                * "rationale": explicación del porqué
                * "correct": true/false
            - Debe haber EXACTAMENTE UNA opción correcta.
            - El campo "author" debe ser "Gemini".
            - El campo "topics" debe ser una lista con un único elemento: " + topic.toUpperCase() + " en mayúsculas.
            - El campo "statement" debe contener la pregunta.
            """;
            Schema schema = SimpleSchemas.from(QuestionDTO.class);
            QuestionDTO questionGenerated = genai.generateJson(prompt, schema, QuestionDTO.class);
            HashSet<String> realTopics = new HashSet<>(questionGenerated.topics);
            ArrayList<Option> realOptions = new ArrayList<>();
            for (OptionDTO o : questionGenerated.options) {
                realOptions.add(new Option(o.text, o.rationale, o.correct));
            }
            Question question = new Question(
                questionGenerated.author,
                realTopics,
                questionGenerated.statement,
                realOptions
            );
            return question;
        } catch (Exception e) {
            throw new QuestionCreatorException("Error creating question: " + e.getMessage(), e);
        }
    }
}
