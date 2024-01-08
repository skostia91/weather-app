package by.shylau.weatherapp.security;

public class FoolProof {
    /**
     * Метод, который запрещает пользователю устанавливать некоторые определённые слабые пароли
     */
    public static String defenceForFool(String password) {
        return switch (password) {
            case "password" -> "Пароль - password??? Серьёзно? Он слишком простой, выбери что-то посложнее.";
            case "пароль" -> "Пароль - пароль??? Серьёзно? Он слишком простой, выбери что-то посложнее.";
            case "111" ->
                    "Такой пароль легко взломать и бандиты могут узнать в каких городах ты смотришь погоду. Поменяй";
            case "1111" -> "Серьёзно? 1111? У нас серьёзная организация. Выбери другой пароль.";
            case "11111" ->
                    "Я уже видел много паролей, но этот точно один из самых слабых. Посложней просто не запомнишь?";
            case "222" -> "Усложни пароль хотя бы до 333.";
            case "333" -> "Молодец, а теперь установи нормальный пароль.";
            case "444" -> "Оригинально. Можно до вечера писать 555, 666 .... Установи нормальный пароль.";
            case "555" -> "Я никуда не спешу. Установи нормальный пароль.";
            case "666" -> "666? Я сейчас материться начну после таких паролей. Установи нормальный пароль.";
            case "777" ->
                    "Что за детский сад, просто установи нормальный пароль, а то я сейчас вылезу из компуктера и дам ремня";
            case "888" -> "888? Я понял - это вызов кто первым сломается после попыток ввести слабый пароль. " +
                    "Интересно, кто же победит: СОВЕРШЕНСТВО или кожаный мешок";
            case "999" -> "999? Ладно, твоя взяла, незнакомец, ты победил и я понимаю что ты будешь издеваться такими паролями," +
                    " но чтобы зайти в систему надо установить нормальный пароль";
            case "000" -> "Я понял, мы наверное начали с 111 и дошли до 000 и ты хочешь увидеть секретную концовку. " +
                    "Хорошо, вам будет представлен отрывок из классического произведения Кровь и бетон: *******, " +
                    "(чёрт побери), а ну, иди сюда, ***** собачье, а? Сдуру решил ко мне лезть, ты? " +
                    "(Нехороший человек), (чёрт побери), а? Ну, иди сюда, попробуй меня ******** — я тебя сам ******,"
                    + " *******, ******* чёртов, будь ты проклят! Иди, идиот, ******* тебя и всю твою семью! " +
                    "***** собачье, жлоб (плохо пахнущий), ******, ****, (нехороший человек)! " +
                    "Иди сюда, мерзавец, негодяй, гад! Иди сюда, ты, *****, (попа)!...... Но пароль надо поменять";
            case "0000" -> "Всё, я звоню твоим родителям, чтобы они всыпали тебе ремня. Может после этого ты установишь нормальный пароль";
            case "123" -> "Пароль такой простой, что даже моя бабушка его взломает за минуту. Поменяй";
            case "1234" ->
                    "Эй, пароль! Ты такой слабый, что даже мой кофе успевает его угадать до того, как я его допью. Поменяй";
            case "12345" -> "Ой, ой, ой! Ваш пароль такой слабенький, что даже моему роботу не составит труда его " +
                    "угадать. Попробуйте что-то посложнее, а то я уже засыпать начал от скучных паролей!";
            case "123456" -> "Серьезно? Такой пароль можно угадать за секунду, даже не задумываясь. Поменяй";
            case "qqq" -> "Вот это да, такой пароль можно угадать даже без всяких специальных программ. Поменяй";
            case "www" ->
                    "Вы серьезно думаете, что такой пароль защитит ваши данные? Лучше бы попробовали что-то посложнее.";
            case "eee" ->
                    "Сложно поверить, что кто-то действительно использует такие простые пароли в наше время. Поменяй";
            case "aaa" ->
                    "Я думал, что у меня плохая память, но ваш пароль такой простой, что я его даже заполнил. Поменяй";
            case "sss" -> "Пароль такой слабый, что даже мой смартфон его взломает, не напрягаясь. Поменяй";
            case "ddd" -> "Пароль такой слабый, что даже моя кошка его угадает, играя на клавиатуре. Поменяй";
            case "qwe" -> "Этот пароль слишком распространен, выберите уникальный пароль.";
            case "qwer" -> "Этот пароль слишком распространен, выберите уникальный пароль.";
            case "qwert" -> "Этот пароль слишком распространен, выберите уникальный пароль.";
            case "qwerty" -> "Такой пароль легко угадать, выберите что-то более надежное.";
            default -> null;
        };
    }
}
