/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: it's the word object's bean file
 * **/
package com.cst2335.ryan;

import java.util.ArrayList;
import java.util.Objects;

public class Word {
    // to save the word's content
    private String word;
    // to save the definitions of the word
    private ArrayList<String> definitions;
    // to save the parts of speech
    private String partsOfSpeech;
    // to save the example sentence
    private String exampleSentence;

    public Word(String word, ArrayList<String> definitions, String partsOfSpeech, String exampleSentence) {
        this.setWord(word);
        this.setDefinitions(definitions);
        this.setPartsOfSpeech(partsOfSpeech);
        this.setExampleSentence(exampleSentence);
    }

    public String getWord() {
        return word;
    }

    private void setWord(String word) {
        this.word = word;
    }

    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    private void setDefinitions(ArrayList<String> definitions) {
        this.definitions = definitions;
    }

    public String getPartsOfSpeech() {
        return partsOfSpeech;
    }

    private void setPartsOfSpeech(String partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    public String getExampleSentence() {
        return exampleSentence;
    }

    private void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return Objects.equals(getWord(), word1.getWord()) &&
                Objects.equals(definitions, word1.definitions) &&
                Objects.equals(getPartsOfSpeech(), word1.getPartsOfSpeech()) &&
                Objects.equals(getExampleSentence(), word1.getExampleSentence());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWord(), definitions, getPartsOfSpeech(), getExampleSentence());
    }

    @Override
    public String toString() {
        return this.getWord();
    }
}