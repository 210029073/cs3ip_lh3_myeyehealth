package uk.ac.aston.ip.myeyehealth.textwatcher;

import android.text.Editable;
import android.text.TextWatcher;


import com.google.android.material.textfield.TextInputLayout;

/**
 * This performs a presence check of the TextInputLayout by checking if the text box
 * is empty.
 * Uses the TextWatcher api to listen to the user's input when typing their details.
 *
 * @version 1.0.0
 * @author Ibrahim Ahmad
 * @see android.text.TextWatcher
 * @see TextInputLayout
 * */
public abstract class TextErrorChangeListener implements TextWatcher {

    private TextInputLayout textInput;

    private String errorMessage;

    /**
     * This constructs the {@link TextErrorChangeListener
     * }
     * @param errorMessage Represents the error message when the user's inputs fail the presence check.
     *
     * @param textInput - Represents the {@link TextInputLayout}
     * */
    public TextErrorChangeListener(TextInputLayout textInput, String errorMessage) {
        this.textInput = textInput;
        this.errorMessage = errorMessage;

    }

    /**
     * This gets the {@link TextInputLayout}*/
    private TextInputLayout getTextInput() {
        return textInput;
    }

    /**
     * An action listener designed to listen to the users input
     * before the user inputs the text.
     *
     * Please note that this is an optional implementation.
     * */
    @Override
    public abstract void beforeTextChanged(CharSequence s, int start, int count, int after);

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(textInput.getEditText().getText().toString().isEmpty()) {
            textInput.setError(this.getErrorMessage());
            textInput.setErrorEnabled(true);
        }

        else {
            textInput.setErrorEnabled(false);
        }
    }

    /**
     * This sets the error message.
     * @param errorMessage Represents the error message.
     * */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * This gets the error message
     * @return A {@link String} containing the error message*/
    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public abstract void afterTextChanged(Editable s);
}
