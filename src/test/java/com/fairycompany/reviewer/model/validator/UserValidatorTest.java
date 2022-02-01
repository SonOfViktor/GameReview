package com.fairycompany.reviewer.model.validator;

import org.testng.annotations.*;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class UserValidatorTest {
    UserValidator userValidator;

    @BeforeClass
    public void init(){
        userValidator = UserValidator.getInstance();
    }

    @DataProvider (name = "forLoginCheck")
    private Object[][] dataForLogin() {
        return new Object[][]{
                {"777_punksim@mail.ru", true},
                {"puk@de.de", true},
                {"asdf_a889_asdf_QWE2_AS34d@yombatumba.gamer", true},
                {"maks@aa.ru", true},
                {"a@mail.ru", false},
                {"_asdfjqwerizxc-vjiuyewaqwr@mail.ru", false},
                {"maks.ru", false},
                {"maks@a.ru", false},
                {"maks@mail", false},
                {"mask@mail.r", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider (name = "forPasswordCheck")
    private Object[][] dataForPassword() {
        return new Object[][]{
                {"zxcASD12", true},
                {"1aZ2sXXa10qqqqiiEE20ZCBdj3zc30", true},
                {"zSC8zz3", false},
                {"zxcvbnmad", false},
                {"ASDFQWER", false},
                {"12345789", false},
                {"123asdf8d", false},
                {"DFG2349DR", false},
                {"sdfgQWER", false},
                {"1aZ2sXXa10qqqqiiEE20ZCBdj3zc30Q", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider (name = "forNameCheck")
    private Object[][] dataForName() {
        return new Object[][]{
                {"Тё", true},
                {"Максимммельянусищеще", true},
                {"Li", true},
                {"Maksimmmelianusisess", true},
                {"Максимммельянусищещее", false},
                {"Maksimmmellianusisess", false},
                {"Ц", false},
                {"Q", false},
                {"OlGa", false},
                {"МаКсим", false},
                {"Макс5", false},
                {"Olga5", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider (name = "forPasswordDoubleCheck")
    private Object[][] dataForPasswordDoubleCheck() {
        return new Object[][]{
                {"zxcASD123", "ASD123zxc"},
                {"zxcASD123", "ZXCASD123"},
                {"zxcASD123", "<script>alert(123)</script>"},
                {"zxcASD123", ""},
                {"zxcASD123", null}
        };
    }

    @DataProvider (name = "forPhoneCheck")
    private Object[][] dataForPhone() {
        return new Object[][]{
                {"291234567", true},
                {"33123458", false},
                {"2909876543", false},
                {"29123II88", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false},
        };
    }

    @DataProvider (name = "forDateCheck")
    private Object[][] dataForDate() {
        return new Object[][]{
                {LocalDate.now().minusYears(16), true},
                {LocalDate.now().minusYears(100).plusDays(1), true},
                {LocalDate.now().minusYears(16).plusDays(1), false},
                {LocalDate.now().minusYears(100), false},
                {null, false},
        };
    }

    @DataProvider (name = "forRoleCheck")
    private Object[][] dataForRole() {
        return new Object[][]{
                {"GUEST", true},
                {"USER", true},
                {"ADMIN", true},
                {"user", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider (name = "forStatusCheck")
    private Object[][] dataForStatus() {
        return new Object[][]{
                {"NOT_CONFIRMED", true},
                {"BANNED", true},
                {"ACTIVE", true},
                {"banned", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @DataProvider (name = "forTokenCheck")
    private Object[][] dataForToken() {
        return new Object[][]{
                {"9bd9361a-e25c-401b-8099-cffdeab5433b", true},
                {"sbdqwrxa-eyuc-adfb-ergc-cffdeabdfxcb", true},
                {"94493615-8259-4017-8099-132455433643", true},
                {"9bd9361a3-e25c-401b-8099-cffdeab5433b", false},
                {"9bd9361a-e25c-401b-8099-cffdeab5433be", false},
                {"9bd9361a-e25c5-401b-8099-cffdeab5433b", false},
                {"9bd9361a-e2-5c-401b-8099-cffdeab5433b", false},
                {"D9d9361a-e25c-401b-8099-cFFdeab5433b", false},
                {"<script>alert(123)</script>", false},
                {"", false},
                {null, false}
        };
    }

    @Test (dataProvider = "forLoginCheck")
    public void testIsLoginValid(String login, boolean expected) {
        boolean actual = userValidator.isLoginValid(login);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forPasswordCheck")
    public void testIsPasswordValid(String password, boolean expected) {
        boolean actual = userValidator.isPasswordValid(password);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forNameCheck")
    public void testIsNameValid(String name, boolean expected) {
        boolean actual = userValidator.isNameValid(name);

        assertEquals(actual, expected);
    }

    @Test
    public void testPasswordCheckPositive() {
        String password = "zxcASD123";
        String passwordChecker = "zxcASD123";

        assertTrue(userValidator.passwordCheck(password, passwordChecker));
    }

    @Test (dataProvider = "forPasswordDoubleCheck")
    public void testPasswordCheckNegative(String password, String passwordChecker) {

        assertFalse(userValidator.passwordCheck(password, passwordChecker));
    }

    @Test (dataProvider = "forPhoneCheck")
    public void testIsPhoneValid(String phone, boolean expected) {
        boolean actual = userValidator.isPhoneValid(phone);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forDateCheck")
    public void testIsDateValid(LocalDate date, boolean expected) {
        boolean actual = userValidator.isBirthdayValid(date);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forRoleCheck")
    public void testIsRoleValid(String role, boolean expected) {
        boolean actual = userValidator.isRoleValid(role);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forStatusCheck")
    public void testIsStatusValid(String status, boolean expected) {
        boolean actual = userValidator.isStatusValid(status);

        assertEquals(actual, expected);
    }

    @Test (dataProvider = "forTokenCheck")
    public void testIsTokenValid(String token, boolean expected) {
        boolean actual = userValidator.isTokenValid(token);

        assertEquals(actual, expected);
    }
}