package model;
import es.usal.genai.*;
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
            String prompt = "Crea una en español pregunta tipo test sobre el siguiente tema: " + topic + ". La pregunta debe ser clara y concisa. Debe tener 4 respuestas posibles, de las cuales solo una es correcta. Además, debe incluir un campo 'explanation' que justifique por qué la respuesta correcta es la adecuada. El autor de la pregunta debe ser 'Gemini AI'.";
            Schema schema = SimpleSchemas.from(Question.class);
            Question question = genai.generateJson(prompt, schema, Question.class);
            return question;
        } catch (Exception e) {
            throw new QuestionCreatorException("Error creating question: " + e.getMessage(), e);
        }
    }
}
