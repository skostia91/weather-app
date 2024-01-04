package by.shylau.weatherapp.security;

import org.junit.jupiter.api.Test;

import static by.shylau.weatherapp.security.FoolProof.defenceForFool;
import static org.junit.jupiter.api.Assertions.*;

class FoolProofTest {
    @Test
    public void testDefenceForFool_WeakPassword() {
        String weakPassword = "password";

        String result = defenceForFool(weakPassword);

        assertEquals("Пароль - password??? Серьёзно? Он слишком простой, выбери что-то посложнее.", result);
    }

    @Test
    public void testDefenceForFool_AnotherWeakPassword() {
        String anotherWeakPassword = "111";

        String result = defenceForFool(anotherWeakPassword);

        assertEquals("Такой пароль легко взломать и бандиты могут узнать в каких городах ты смотришь погоду. Поменяй", result);
    }

    @Test
    public void testDefenceForFool_StrongPassword() {
        String strongPassword = "aBcD1234!";

        String result = defenceForFool(strongPassword);

        assertNull(result);
    }
}