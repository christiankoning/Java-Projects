import java.util.HashMap;
import java.util.Map;

public class Categories {
    private static final Map<String, String> FILE_CATEGORIES = new HashMap<>();

    // Static block to initialize categories
    static {
        FILE_CATEGORIES.put("jpg", "Images");
        FILE_CATEGORIES.put("png", "Images");
        FILE_CATEGORIES.put("gif", "Images");
        FILE_CATEGORIES.put("bmp", "Images");
        FILE_CATEGORIES.put("svg", "Images");

        FILE_CATEGORIES.put("pdf", "Documents");
        FILE_CATEGORIES.put("docx", "Documents");
        FILE_CATEGORIES.put("txt", "Documents");
        FILE_CATEGORIES.put("xlsx", "Documents");
        FILE_CATEGORIES.put("pptx", "Documents");

        FILE_CATEGORIES.put("mp4", "Videos");
        FILE_CATEGORIES.put("avi", "Videos");
        FILE_CATEGORIES.put("mov", "Videos");
        FILE_CATEGORIES.put("mkv", "Videos");

        FILE_CATEGORIES.put("mp3", "Music");
        FILE_CATEGORIES.put("wav", "Music");
        FILE_CATEGORIES.put("aac", "Music");
        FILE_CATEGORIES.put("flac", "Music");

        FILE_CATEGORIES.put("zip", "Archives");
        FILE_CATEGORIES.put("rar", "Archives");
        FILE_CATEGORIES.put("tar", "Archives");
        FILE_CATEGORIES.put("gz", "Archives");
        FILE_CATEGORIES.put("7z", "Archives");
    }

    public static String getCategory(String extension) {
        return FILE_CATEGORIES.getOrDefault(extension.toLowerCase(), null);
    }
}
