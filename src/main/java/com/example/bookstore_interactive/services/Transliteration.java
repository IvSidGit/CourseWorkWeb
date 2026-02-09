package com.example.bookstore_interactive.services;

public abstract class Transliteration {
    public static String transliterateToLatin(String text) {
        return text.toLowerCase()
                .replace("а", "a").replace("б", "b").replace("в", "v").replace("г", "g")
                .replace("д", "d").replace("е", "e").replace("ё", "yo").replace("ж", "zh")
                .replace("з", "z").replace("и", "i").replace("й", "y").replace("к", "k")
                .replace("л", "l").replace("м", "m").replace("н", "n").replace("о", "o")
                .replace("п", "p").replace("р", "r").replace("с", "s").replace("т", "t")
                .replace("у", "u").replace("ф", "f").replace("х", "h").replace("ц", "ts")
                .replace("ч", "ch").replace("ш", "sh").replace("щ", "sch").replace("ъ", "")
                .replace("ы", "y").replace("ь", "").replace("э", "e").replace("ю", "yu")
                .replace("я", "ya");
    }
}
