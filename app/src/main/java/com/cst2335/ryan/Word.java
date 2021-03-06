/****
 * Author: Zhe (Ryan) Li
 * Last modified: Mar 25, 2019
 * Description: it's the word object's bean file
 * **/
package com.cst2335.ryan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Word {
    /** id */
    long id;
    /** to save the word's content */
    private String word;

    /** to save the definitions of the word.
     * One definition may match with more than 0 record of example sentences
     * structure: HashMap<String definition, ArrayList<String> exampleSentences> definitions*/
    private HashMap<String, ArrayList<String>> definitions;

     /** to save the parts of speech */
    private String partsOfSpeech;

    /**
     *  Constructor
     */
    public Word() {

    }

    /**
     * this is for non-database use, because id is 0
     * @param word
     * @param definitions
     * @param partsOfSpeech
     */
    public Word(String word, HashMap<String, ArrayList<String>> definitions, String partsOfSpeech) {
        this(0, word, definitions, partsOfSpeech);
    }

    /**
     * this is for database use
     *
     * @param id
     * @param word
     * @param definitions
     * @param partsOfSpeech
     */
    public Word(long id, String word, HashMap<String, ArrayList<String>> definitions, String partsOfSpeech) {
        this.id = id;
        this.word = word;
        this.definitions = definitions;
        this.partsOfSpeech = partsOfSpeech;
    }

    /**
     * get id
     * @return
     */
    public long getId() {
        return id;
    }

    /**
     * set id
     * @param id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * getWord
     *
     * @return
     */
    public String getWord() {
        return word;
    }

    /**
     * setWord
     *
     * @param word
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * getDefinitions
     *
     * @return
     */
    public HashMap<String, ArrayList<String>> getDefinitions() {
        return definitions;
    }

    /**
     * setDefinitions
     *
     * @param definitions
     */
    public void setDefinitions(HashMap<String, ArrayList<String>> definitions) {
        this.definitions = definitions;
    }

    /**
     * getPartsOfSpeech
     *
     * @return
     */
    public String getPartsOfSpeech() {
        return partsOfSpeech;
    }

    /**
     * setPartsOfSpeech
     *
     * @param partsOfSpeech
     */
    public void setPartsOfSpeech(String partsOfSpeech) {
        this.partsOfSpeech = partsOfSpeech;
    }

    @Override
    public String toString() {
        return this.getWord();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word1 = (Word) o;
        return getId() == word1.getId() &&
                Objects.equals(getWord(), word1.getWord()) &&
                Objects.equals(getDefinitions(), word1.getDefinitions()) &&
                Objects.equals(getPartsOfSpeech(), word1.getPartsOfSpeech());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getWord(), getDefinitions(), getPartsOfSpeech());
    }
}
