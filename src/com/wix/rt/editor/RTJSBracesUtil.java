package com.wix.rt.editor;

import com.intellij.lang.Language;
import com.intellij.lang.javascript.JavascriptLanguage;
//import com.intellij.lang.javascript.psi.impl.JSOffsetBasedImplicitElement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Dennis.Ushakov
 */
public final class RTJSBracesUtil {
    public static final String DEFAULT_START = "{";
    public static final String DEFAULT_END = "}";

    private static final Set<String> DEFAULT_CONFLICTS = new HashSet<String>(Arrays.asList("DjangoTemplate", "Jinja2", "Web2Py",
            "Handlebars", "Twig", "Blade"));

    private RTJSBracesUtil() {
    }

    public static String getInjectionStart(Project project) {
        return getInjectionDelimiter(project, "startSymbol", DEFAULT_START);
    }

    public static String getInjectionEnd(Project project) {
        return getInjectionDelimiter(project, "endSymbol", DEFAULT_END);
    }

    private static String getInjectionDelimiter(Project project, final String id, final String defaultDelimiter) {
//        final JSOffsetBasedImplicitElement start = AngularIndexUtil.resolve(project, AngularInjectionDelimiterIndex.INDEX_ID, id);
//        if (start != null) {
//            return start.getTypeString();
//        }
        return defaultDelimiter;
    }

    public static boolean hasConflicts(String start, String end, PsiElement element) {
        final Language elementLanguage = element.getLanguage();
        // JSP contains two roots that contain XmlText, don't inject anything in JSP root to prevent double injections
        if ("JSP".equals(elementLanguage.getDisplayName())) {
            return true;
        }

        PsiFile file = element.getContainingFile();
        if (DEFAULT_START.equals(start) || DEFAULT_END.equals(end)) {
            // JSX attributes don't contain AngularJS injections, {{}} is JSX injection with object inside
            if (elementLanguage.isKindOf(JavascriptLanguage.INSTANCE)) return true;

            for (Language language : file.getViewProvider().getLanguages()) {
                if (DEFAULT_CONFLICTS.contains(language.getDisplayName())) {
                    return true;
                }
            }
        }
        return false;
    }
}
