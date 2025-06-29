package com.llamalad7.mixintests.service;

import com.google.auto.service.AutoService;
import com.llamalad7.mixintests.harness.util.FileUtil;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.launch.platform.container.ContainerHandleVirtual;
import org.spongepowered.asm.launch.platform.container.IContainerHandle;
import org.spongepowered.asm.logging.ILogger;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.IMixinTransformerFactory;
import org.spongepowered.asm.service.*;
import org.spongepowered.asm.service.modlauncher.LoggerAdapterLog4j2;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;

@AutoService(IMixinService.class)
public class TestMixinService extends MixinServiceAbstract implements IClassBytecodeProvider, IClassProvider {
    private static final System.Logger LOGGER = System.getLogger("TestMixinService");

    static IMixinTransformer transformer;

    @Override
    public String getName() {
        return "MixinTests Service";
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public void offer(IMixinInternal internal) {
        if (internal instanceof IMixinTransformerFactory factory) {
            transformer = factory.createTransformer();
        }
    }

    @Override
    protected ILogger createLogger(String name) {
        return new LoggerAdapterLog4j2(name);
    }

    @Override
    public IClassProvider getClassProvider() {
        return this;
    }

    @Override
    public IClassBytecodeProvider getBytecodeProvider() {
        return this;
    }

    @Override
    public ITransformerProvider getTransformerProvider() {
        return null;
    }

    @Override
    public IClassTracker getClassTracker() {
        return null;
    }

    @Override
    public IMixinAuditTrail getAuditTrail() {
        return null;
    }

    @Override
    public Collection<String> getPlatformAgents() {
        return List.of();
    }

    @Override
    public IContainerHandle getPrimaryContainer() {
//        try {
//            URI uri = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI();
//            return new ContainerHandleURI(uri);
//        } catch (URISyntaxException ex) {
//            LOGGER.log(System.Logger.Level.WARNING, ex);
//        }
        return new ContainerHandleVirtual(this.getName());
    }

    @Override
    public InputStream getResourceAsStream(String name) {
        return getClass().getClassLoader().getResourceAsStream(name);
    }

    @Override
    public ClassNode getClassNode(String name) throws ClassNotFoundException, IOException {
        return getClassNode(name, true);
    }

    @Override
    public ClassNode getClassNode(String name, boolean runTransformers) throws ClassNotFoundException, IOException {
        return getClassNode(name, runTransformers, 0);
    }

    @Override
    public ClassNode getClassNode(String name, boolean runTransformers, int readerFlags) throws ClassNotFoundException, IOException {
        try (InputStream is = this.getResourceAsStream(FileUtil.getClassFileName(name))) {
            if (is == null) {
                throw new ClassNotFoundException(name);
            }
            ClassNode node = new ClassNode();
            new ClassReader(is).accept(node, readerFlags);
            return node;
        }
    }

    @Override
    public URL[] getClassPath() {
        // Unused on 0.8.x
        return new URL[0];
    }

    @Override
    public Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    @Override
    public Class<?> findClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, TransformingClassLoader.getInstance());
    }

    @Override
    public Class<?> findAgentClass(String name, boolean initialize) throws ClassNotFoundException {
        return Class.forName(name, initialize, getClass().getClassLoader());
    }
}
