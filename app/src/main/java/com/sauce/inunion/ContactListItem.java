package com.sauce.inunion;

/**
 * Created by 123 on 2018-07-26.
 */

public class ContactListItem {
    public String name_professor;
    public boolean isSection;
    public String getName() {
        return name_professor;
    }


    public ContactListItem(String name_professor,boolean isSection) {

        this.name_professor = name_professor;
        this.isSection = isSection;
    }
}
