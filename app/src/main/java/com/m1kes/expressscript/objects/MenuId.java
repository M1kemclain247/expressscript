package com.m1kes.expressscript.objects;

public enum MenuId {

    QUOTES_MENU(false),
    ORDERS_MENU(false),
    CREATE_QUOTE_MENU(false),
    HEALTH_TIPS_MENU(false),
    BRANCH_LOCATOR(false),
    CONTACT_US_MENU(false),
    LANDING_MAIN_MENU(false),

    MEDICAL_AID(false),
    MY_PROFILE(false)

    ;

    final boolean validate;

    MenuId(final boolean validate) {
        this.validate = validate;
    }

    public boolean isValidate() {
        return validate;
    }
}


