package uk.ac.aston.ip.myeyehealth.vision_tools.color_blindness_test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import uk.ac.aston.ip.myeyehealth.R;

public class ColorBlindQuestionGenerator {
    private Map<String, Integer> colorBlindQuestions;

    private Random random;

    public ColorBlindQuestionGenerator() {
        this.colorBlindQuestions = new HashMap<>();
        performHouseKeeping();
        this.random = new Random();
    }

    private void performHouseKeeping() {
        colorBlindQuestions.put("12", R.drawable.color_vision_12);
        colorBlindQuestions.put("8", R.drawable.color_vision_8);
        colorBlindQuestions.put("9", R.drawable.color_vision_9);
        colorBlindQuestions.put("15", R.drawable.color_vision_15);
        colorBlindQuestions.put("3", R.drawable.color_vision_3);
        colorBlindQuestions.put("5", R.drawable.color_vision_5);
        colorBlindQuestions.put("29", R.drawable.color_vision_29);
    }

    public int getColorBlindQuestionsResource(String value) {
        if(colorBlindQuestions.containsKey(value)) {
            return colorBlindQuestions.get(value);
        }

        return -1;
    }

    public List<String> getKeys() {
        List<String> keys = new ArrayList<>();

        for (String key : colorBlindQuestions.keySet()) {
            keys.add(key);
        }

        return keys;
    }

    public int getRandomColorBlindQuestionResource() {

        int random_value = random.nextInt(getKeys().size());
        if(getKeys().size() == 0) {
            random_value = 0;
        }
        String value = String.valueOf(getKeys().get(random_value));
        if(colorBlindQuestions.containsKey(value)) {
            return colorBlindQuestions.get(value);
        }

        return -1;
    }

    public String findKey(int value) {
        for (
                Map.Entry<String, Integer> entry
                :
                colorBlindQuestions.entrySet()) {
            if(entry.getValue() == value) {
                return entry.getKey();
            }
        }

        return null;
    }

    public int generateRandomCombination() {
        return random.nextInt(100);
    }
}
