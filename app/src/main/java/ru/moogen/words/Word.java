package ru.moogen.words;

import android.content.res.Resources;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Word implements Comparable<Word> {

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy",
            new Locale("ru"));
    private static SimpleDateFormat SHORT_DATE_FORMAT = new SimpleDateFormat("d MMMM",
            new Locale("ru"));

    private int id;
    private String date;
    private String name;
    private String additionalName;
    private String etim;
    private String description;
    private String example;



    private String searchName;
    private String descriptionSend;
    private boolean favourite;

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public Date getDateClassDate() {
        return dateClassDate;
    }

    public void setDateClassDate(Date dateClassDate) {
        this.dateClassDate = dateClassDate;
    }

    private Date dateClassDate;


    public static SimpleDateFormat getDateFormat() {
        return DATE_FORMAT;
    }

    public static SimpleDateFormat getShortDateFormat() {
        return SHORT_DATE_FORMAT;
    }

    public static void setDateFormat(Resources res){
        DATE_FORMAT = new SimpleDateFormat(res.getString(R.string.date_format),
                new Locale(res.getString(R.string.locale)));
        SHORT_DATE_FORMAT = new SimpleDateFormat(res.getString(R.string.short_date_format),
                new Locale(res.getString(R.string.locale)));
    }

    public String getDescriptionSend() {
        return descriptionSend;
    }

    public void setDescriptionSend(String descriptionSend) {
        this.descriptionSend = descriptionSend;
    }

    public Word(int id, String date, String name, String additionalName, String etim,
                String description, String example, boolean favourite) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.additionalName = additionalName;
        this.etim = etim;
        this.description = description;
        this.example = example;
        this.favourite = favourite;

        try {
            dateClassDate = DATE_FORMAT.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!(name == null)) {
            searchName = name.replace("\u0301", "");
            descriptionSend = description.replace("<p>", "")
                    .replace("</p>", "");

        }

    }

    @Override
    public int compareTo(Word o) {
        return this.id - o.getId();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("id = ").append(id)
                .append("; date = ").append(date)
                .append("; name = ").append(name)
                .append("; additionalName = ").append(additionalName)
                .append("; etim = ").append(etim)
                .append("; description = ").append(description)
                .append("; example = ").append(example)
                .append("; favourite = ").append(favourite);
        return stringBuilder.toString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalName() {
        return additionalName;
    }

    public void setAdditionalName(String additionalName) {
        this.additionalName = additionalName;
    }

    public String getEtim() {
        return etim;
    }

    public void setEtim(String etim) {
        this.etim = etim;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }
}
