package testools;

import com.google.common.collect.Sets;

import java.util.Collection;

import static java.util.stream.Collectors.joining;


public final class RendererManager {

    private static final Collection<Renderer> RENDERERS = Sets.newLinkedHashSet();
    private static RendererManager instance;

    private RendererManager() {
    }

    public static RendererManager instance() {
        synchronized (RendererManager.class) {
            if (instance == null) {
                instance = new RendererManager();
            }
            return instance;
        }
    }

    public static void registerRenderer(final Renderer renderer) {
        RENDERERS.add(renderer);
    }

    public static boolean accept(final Object object) {
        for (final Renderer renderer : RENDERERS) {
            if (renderer.handle(object)) {
                return true;
            }
        }
        return false;
    }

    public static void clear() {
        RENDERERS.clear();
    }

    public String render(final Object object) {
        if (object == null) {
            return "null";
        }
        if (object instanceof Collection) {
            final Collection<Object> collection = (Collection<Object>) object;
            return collection.stream().map(this::render).collect(joining(", ", "[", "]"));
        }
        for (final Renderer renderer : RENDERERS) {
            if (renderer.handle(object)) {
                return renderer.render(object);
            }
        }
        return object.toString();
    }
}
