package com.orangehrm.test;

import com.orangehrm.base.BaseClass;
import org.testng.annotations.Test;

public class DummyClass extends BaseClass {

    @Test
    public void dummyTest() {
        String title = getDriver().getTitle();
        assert title.equals("OrangeHRM"):"Test Failed - title is not matching";
    }
}
