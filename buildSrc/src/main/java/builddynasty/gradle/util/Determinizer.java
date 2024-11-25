
package builddynasty.gradle.util;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;


public class Determinizer {

    public static void determinize(String inputPath, String outputPath, List<File> toInclude, boolean doForgeReplacementOfMetaInf) throws IOException {
        System.out.println("Running Determinizer");
        System.out.println(" Input path: " + inputPath);
        System.out.println(" Output path: " + outputPath);
        System.out.println(" Shade: " + toInclude);

        try (
                JarFile jarFile = new JarFile(new File(inputPath));
                JarOutputStream jos = new JarOutputStream(new FileOutputStream(new File(outputPath)))
        ) {

            List<JarEntry> entries = jarFile.stream()
                    .sorted(Comparator.comparing(JarEntry::getName))
                    .collect(Collectors.toList());

            for (JarEntry entry : entries) {
                if (entry.getName().equals("META-INF/fml_cache_annotation.json")) {
                    continue;
                }
                if (entry.getName().equals("META-INF/fml_cache_class_versions.json")) {
                    continue;
                }
                JarEntry clone = new JarEntry(entry.getName());
                clone.setTime(42069);
                jos.putNextEntry(clone);
                if (entry.getName().endsWith(".json")) {
                    JsonElement json = new JsonParser().parse(new InputStreamReader(jarFile.getInputStream(entry)));
                    jos.write(writeSorted(json).getBytes());
                } else if (entry.getName().equals("META-INF/MANIFEST.MF") && doForgeReplacementOfMetaInf) { // only replace for forge jar
                    ByteArrayOutputStream cancer = new ByteArrayOutputStream();
                    copy(jarFile.getInputStream(entry), cancer);
                    String manifest = new String(cancer.toByteArray());
                    if (!manifest.contains("builddynasty.launch.BuildDynastyTweaker")) {
                        throw new IllegalStateException("unable to replace");
                    }
                    manifest = manifest.replace("builddynasty.launch.BaritoneTweaker", "org.spongepowered.asm.launch.MixinTweaker");
                    jos.write(manifest.getBytes());
                } else {
                    copy(jarFile.getInputStream(entry), jos);
                }
            }
            for (File file : toInclude) {
                try (JarFile mixin = new JarFile(file)) {
                    for (JarEntry entry : mixin.stream().sorted(Comparator.comparing(JarEntry::getName)).collect(Collectors.toList())) {
                        if (entry.getName().startsWith("META-INF") && !entry.getName().startsWith("META-INF/services")) {
                            continue;
                        }
                        jos.putNextEntry(entry);
                        copy(mixin.getInputStream(entry), jos);
                    }
                }
            }
            jos.finish();
        }
        System.out.println("Done with determinizer");
    }

    private static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
    }

    private static String writeSorted(JsonElement in) throws IOException {
        StringWriter writer = new StringWriter();
        JsonWriter jw = new JsonWriter(writer);
        ORDERED_JSON_WRITER.write(jw, in);
        return writer.toString() + "\n";
    }


    private static final TypeAdapter<JsonElement> ORDERED_JSON_WRITER = new TypeAdapter<JsonElement>() {

        @Override
        public JsonElement read(JsonReader in) {
            return null;
        }

        @Override
        public void write(JsonWriter out, JsonElement value) throws IOException {
            if (value == null || value.isJsonNull()) {
                out.nullValue();
            } else if (value.isJsonPrimitive()) {
                JsonPrimitive primitive = value.getAsJsonPrimitive();
                if (primitive.isNumber()) {
                    out.value(primitive.getAsNumber());
                } else if (primitive.isBoolean()) {
                    out.value(primitive.getAsBoolean());
                } else {
                    out.value(primitive.getAsString());
                }

            } else if (value.isJsonArray()) {
                out.beginArray();
                for (JsonElement e : value.getAsJsonArray()) {
                    write(out, e);
                }
                out.endArray();

            } else if (value.isJsonObject()) {
                out.beginObject();

                List<Map.Entry<String, JsonElement>> entries = new ArrayList<>(value.getAsJsonObject().entrySet());
                entries.sort(Comparator.comparing(Map.Entry::getKey));
                for (Map.Entry<String, JsonElement> e : entries) {
                    out.name(e.getKey());
                    write(out, e.getValue());
                }
                out.endObject();

            } else {
                throw new IllegalArgumentException("Couldn't write " + value.getClass());
            }
        }
    };
}
