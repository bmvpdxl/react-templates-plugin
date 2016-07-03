package com.wix.rt.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wix.rtk.cli.RTRunner;
import org.jetbrains.annotations.Nullable;

@State(name = "RTProjectComponent",
        storages = {
                @Storage(id = "default", file = StoragePathMacros.PROJECT_FILE),
                @Storage(id = "dir", file = StoragePathMacros.PROJECT_CONFIG_DIR + "/reactTemplatesPlugin.xml", scheme = StorageScheme.DIRECTORY_BASED)})
public class Settings implements PersistentStateComponent<Settings> {
    public String builtinRulesPath = "";
    public String rtExecutable = "";
    public String nodeInterpreter;
    public String targetVersion;
    public boolean treatAllIssuesAsWarnings;
    public boolean pluginEnabled;
    public String modules = RTRunner.AMD;
    public boolean groupController = true;
    public boolean groupOther = true;
    public boolean watchAndCompileRT = true;
    public boolean reactNative;
    /**
     * @deprecated
     */
    @Deprecated
    public boolean commonJS;

    public static Settings getInstance(Project project) {
        return ServiceManager.getService(project, Settings.class);
    }

    @Nullable
    @Override
    public Settings getState() {
        return this;
    }

    @Override
    public void loadState(Settings state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    public String getVersion() {
        return nodeInterpreter + rtExecutable + builtinRulesPath + modules + groupController + groupOther + targetVersion + reactNative + watchAndCompileRT;
    }
}
