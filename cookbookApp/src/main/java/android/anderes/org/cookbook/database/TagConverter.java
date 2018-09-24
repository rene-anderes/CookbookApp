package android.anderes.org.cookbook.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Set;

public class TagConverter {

    private final static Gson GSON = new Gson();

    @TypeConverter
    public static Set<String> stringToStringSet(String data) {
        if (data == null) {
            return Collections.emptySet();
        }

        final Type listType = new TypeToken<Set<String>>() {}.getType();

        return GSON.fromJson(data, listType);
    }

    @TypeConverter
    public static String stringSetToString(Set<String> tags) {
        return GSON.toJson(tags);
    }
}
