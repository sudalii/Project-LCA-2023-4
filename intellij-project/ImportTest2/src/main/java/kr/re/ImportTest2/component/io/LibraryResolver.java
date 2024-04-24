package kr.re.ImportTest2.component.io;

import org.openlca.core.DataDir;
import org.openlca.core.library.LibraryDir;
import org.openlca.jsonld.LibraryLink;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class LibraryResolver {

    private final LibraryDir libDir = DataDir.get().getLibraryDir();
    private final LinkedList<LibraryLink> links = new LinkedList<>();
    private final Set<String> handled = new HashSet<>();
    private final Consumer<Boolean> callback;

    private LibraryResolver(List<LibraryLink> links, Consumer<Boolean> callback) {
        this.links.addAll(links);
        this.callback = callback;
    }

    static void resolve(List<LibraryLink> links, Consumer<Boolean> callback) {
        if (links.isEmpty()) {
            callback.accept(true);
            return;
        }
        new LibraryResolver(links, callback).next();
    }

    private void next() {
        if (links.isEmpty()) {
            callback.accept(true);
            return;
        }
        var link = links.pop();
        var lib = libDir.getLibrary(link.id()).orElse(null);
        if (lib == null) {
            return;
        } else if (!handled.contains(lib.name())) {
            return;
        } else if (links.isEmpty()) {
            callback.accept(true);
        } else {
            next();
        }
    }

}
