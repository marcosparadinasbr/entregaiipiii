package model;
import es.usal.genai.*;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.genai.types.Schema;

public class GeminiQuestionCreator implements QuestionCreator {
    private GenAiConfig config;
    private final String questionCreatorDescription = "Creador de preguntas basado en Gemini AI de Google.";
    GenAiFacade genai;

    public GeminiQuestionCreator(String API_KEY, String modelId) {
        this.config = GenAiConfig.forGemini(modelId, API_KEY);
    }
    public String getQuestionCreatorDescription(){
        return questionCreatorDescription;
    }
    public Question createQuestion(String topic) throws QuestionCreatorException {
        GenAiConfig.setDevelopmentMode();
        try (GenAiFacade genai = new GenAiFacade(config)) {
            String prompt = """
            Genera **únicamente un JSON válido** compatible con el siguiente esquema:

            {
            "author": "Gemini",
            "topics": ["%s"] (en mayúsculas),
            "statement": "Texto de la pregunta en español",
            "options": [
                {
                "text": "Texto de la opción",
                "rationale": "Justificación de la opción",
                "correct": boolean
                }
            ]
            }

            Reglas:
            - EXACTAMENTE 4 opciones
            - EXACTAMENTE 1 opción correcta
            - El campo "topics" debe ser una lista de Strings
            - El campo "correct" debe ser un booleano (true/false), NO un texto
            - No incluyas texto antes ni después del JSON
            - No incluyas comentarios
            - El campo "author" debe ser "Gemini"
            - La pregunta y las opciones deben estar en español

            Tema: %s
            """.formatted(topic.toUpperCase(), topic);

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
