package com.mcs.du.noorul.smas;

/**
 * Created by Noorul on 18-10-2017.
 */

public class MarksDataModel {
    private String subject_id;
    private String subject_name;
    private String marks_minor;

    public MarksDataModel(String _subject_id, String _subject_name, String marks) {
        this.subject_id = _subject_id;
        this.subject_name = _subject_name;
        this.marks_minor = marks;
    }

    public String getSubject_id() {
        return subject_id;
    }
    public String getSubject_name() { return subject_name; }
    public String getMarks(){ return marks_minor; }
}
