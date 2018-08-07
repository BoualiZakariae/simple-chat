package com.simplechat.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

public class StringHelperTest {

    @Test
    public void generateRandomNumber_return_requested_length()
    {
        int length = String.valueOf(StringHelper.generateRandomNumber(6)).length();
        assertEquals(6, length);
    }

    @Test
    public void generateRandomNumber_two_random_not_equal()
    {
        assertNotEquals(StringHelper.generateRandomNumber(5), StringHelper.generateRandomNumber(6));
    }

    @Test
    public void emptyStringSlugify()
    {
        assertThat(StringHelper.slugify("")).isEqualTo(new String());
    }

    @Test
    public void singleCharStringSlugify()
    {
        assertThat(StringHelper.slugify("a")).isEqualTo(new String("a"));
    }

    @Test
    public void whithoneSpaceStringSlugify()
    {
        assertThat(StringHelper.slugify("a b")).isEqualTo(new String("a-b"));
    }

    @Test
    public void whithtwoSlashStringSlugify()
    {
        assertThat(StringHelper.slugify("abc/dsef/")).isEqualTo(new String("abc-dsef-"));
    }

    @Test
    public void multiSpaceAndSlashStringSlugify()
    {
        assertThat(StringHelper.slugify("abc/dsef/ fgdg gf-/gfd/ w")).isEqualTo(new String("abc-dsef--fgdg-gf--gfd--w"));
    }

    @Test
    public void randomString_checkIfRandomAndUnique() {

        String str1 = StringHelper.randomString(15);
        String str2 = StringHelper.randomString(15);
        String str3 = StringHelper.randomString(15);
        String str4 = StringHelper.randomString(15);

        assertNotEquals(str1, str2);
        assertNotEquals(str1, str3);
        assertNotEquals(str1, str4);
        assertNotEquals(str2, str4);
    }

    @Test
    public void randomString_returnRequestedLenght() {

        String str1 = StringHelper.randomString(15);


        assertEquals(str1.length(), 15);
    }

    @Test
    public void randomString_returnAlphanumeric() {
        String str1 = StringHelper.randomString(15);

        assertTrue(str1.matches("[A-Za-z0-9]+"));
    }
}
