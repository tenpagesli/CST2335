/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: it's the word object's bean file
 * **/
package com.cst2335.ryan;

import java.util.ArrayList;
import java.util.Objects;

public class Word {
    /** to save the word's content */
    private String word;
    // to save the definitions of the word
    private ArrayList<String> definitions;
    // to save the parts of speech
    private String partsOfSpeech;
     /** to save the example sentence */
    private String exampleSentence;

    /**
     *  Constructor
     */
    public Word() {

    }

    /**
     * Default constructor
     *
     * @param word
     * @param definitions
     * @param partsOfSpeech
     * @param exampleSentence
     */
    public Word(String word, ArrayList<String> definitions, String partsOfSpeech, String exampleSentence) {
        this.setWord(word);
        this.setDefinitions(definitions);
        this.setPartsOfSpeech(partsOfSpeech);
        this.setExampleSentence(exampleSentence);
    }

    /** get word's content
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     *  set word's content
     * @param word
     */
    private void setWord(String word) {
        this.word = word;
    }

    /**
     * get word's definition list
     *
     * @return
     */
    public ArrayList<String> getDefinitions() {
        return definitions;
    }

    /**
     *  set word's definition list
     * @param definitions
     */
    private void setDefinitions(ArrayList<String> definitions) {
        this.definitions = definitions;
    }

    /**
     * get word's parts of speech
     * @return
     */
    public String getPartsOfSpeech() {
        return partsOfSpeech;
    }

    /**
     * set word's parts of speech
     * @param partsOfSpeech
     */
    private void setPartsOfSpeech(String partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    /**
     * get example sentence
     * @return
     */
    public String getExampleSentence() {
        return exampleSentence;
    }

    /**
     * set example sentence
     * @param exampleSentence
     */
    private void setExampleSentence(String exampleSentence) {
        this.exampleSentence = exampleSentence;
    }

    /**
     * Override equals
     * @param o
     * @return
     */
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

    /**
     * Override hashCode
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getWord(), definitions, getPartsOfSpeech(), getExampleSentence());
    }

    /**
     * Override toString
     * @return
     */
    @Override
    public String toString() {
        return this.getWord();
    }
}
